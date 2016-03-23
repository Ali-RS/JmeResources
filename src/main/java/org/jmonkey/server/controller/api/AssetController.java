package org.jmonkey.server.controller.api;

import org.eclipse.jetty.server.session.JDBCSessionManager;
import org.jmonkey.database.asset.*;
import org.jmonkey.database.permission.AdminPermission;
import org.jmonkey.database.permission.PermissionManager;
import org.jmonkey.database.user.SessionInspector;
import org.jmonkey.database.user.User;
import org.jmonkey.database.user.UserManager;
import org.jmonkey.external.bintray.BintrayApiClient;
import org.jmonkey.server.InsufficientPermissionException;
import org.jmonkey.server.ResourceNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author jayfella
 */

@Controller
@RequestMapping("/asset/")
public class AssetController {

    @RequestMapping(value = "/user/pending/", method = RequestMethod.GET)
    @ResponseBody
    public List<PotentialAsset> getUserPotentialAssets(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, InsufficientPermissionException {

        SessionInspector inspector = new SessionInspector();
        User user = inspector.getUser(request.getCookies());

        if (user == null) {
            throw new InsufficientPermissionException("You must be logged in to view your assets pending approval.");
        }

        @SuppressWarnings("unchecked")
        List<PotentialAsset> potentialAssets = new AssetManager().getPotentialAssets(user);
        return potentialAssets;
    }

    @RequestMapping(value = "/potentials/", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> getPotentialAssets(HttpServletRequest request) throws UnsupportedEncodingException, InsufficientPermissionException {

        User user = new SessionInspector().getUser(request.getCookies());

        if (user == null) {
            throw new InsufficientPermissionException();
        }

        boolean isAdmin = new PermissionManager().hasPermission(user, AdminPermission.class);

        if (!isAdmin) {
            throw new InsufficientPermissionException();
        }

        @SuppressWarnings("unchecked")
        List<PotentialAsset> potentialAssets = new AssetManager().getPotentialAssets();

        List<Map<String, Object>> data = new ArrayList<>();
        UserManager userManager = new UserManager();

        potentialAssets.forEach(pa -> {

            User paUser = userManager.getUser(pa.getDiscourseAuthorId());

            Map<String, Object> paData = new HashMap<>();
            paData.put("potentialAsset", pa);
            paData.put("user", paUser);

            List<String> dirStructure = new ArrayList<>();

            try {
                JarFile jarFile = new JarFile(pa.getAssetFile());

                Enumeration<JarEntry> jarEntries = jarFile.entries();

                while (jarEntries.hasMoreElements()) {
                    JarEntry entry = jarEntries.nextElement();
                    dirStructure.add(entry.getName());
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            paData.put("structure", dirStructure);

            data.add(paData);
        });



        // return potentialAssets;
        return data;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public void approvePotentialAsset(@RequestBody long potentialId, HttpServletRequest request, HttpServletResponse response) throws IOException, InsufficientPermissionException {

        SessionInspector inspector = new SessionInspector();
        User user = inspector.getUser(request.getCookies());

        PermissionManager permissionManager = new PermissionManager();
        boolean isAdmin = permissionManager.hasPermission(user, AdminPermission.class);

        if (!isAdmin) {
            throw new InsufficientPermissionException();
        }

        AssetManager assetManager = new AssetManager();
        PotentialAsset potentialAsset = assetManager.getPotentialAsset(potentialId);

        if (potentialAsset == null) {
            throw new ResourceNotFoundException();
        }

        BintrayApiClient bintray = new BintrayApiClient();

        if (!bintray.createPackage(potentialAsset)) {
            throw new IOException("Unable to create bintray package");
        }

        String version = "1.0";

        Asset asset = new Asset(potentialAsset);
        asset.setReadme(potentialAsset.getPackageReadme());

        AssetVersion assetVersion = new AssetVersion(version);
        assetVersion.setShowcaseImage(potentialAsset.getShowcaseImage());
        assetVersion.setDescription(potentialAsset.getVersionDescription());
        assetVersion.setTimeUploaded(System.currentTimeMillis());

        if (!bintray.createPackageVersion(asset, assetVersion)) {
            throw new IOException("Unable to create bintray version");
        }

        if (!bintray.uploadPackage(potentialAsset, asset, assetVersion)) {
            throw new IOException("Unable to upload asset to bintray");
        }

        assetManager.saveOrUpdateAsset(asset);

        // set the associated asset ID after it has been saved.
        // it won't have an incremental ID set until it is saved.
        assetVersion.setAssetId(asset.getId());
        assetManager.saveOrUpdateAssetVersion(assetVersion);

        assetManager.deletePotentialAsset(potentialAsset);
    }

    @RequestMapping(value = "/user/", method = RequestMethod.GET)
    @ResponseBody
    public List<AssetInfo> getUserAssets(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, InsufficientPermissionException {

        SessionInspector inspector = new SessionInspector();
        User user = inspector.getUser(request.getCookies());

        if (user == null) {
            throw new InsufficientPermissionException("Please log in to view your assets, or ass /username/ to the URL.");
        }

        return new AssetManager().getAssets(user);
    }

    @RequestMapping(value = "/random/", method = RequestMethod.GET)
    @ResponseBody
    public List<AssetInfo> getRandomAssets(HttpServletRequest request) {

        return new AssetManager().getRandomAssets(20);
    }

    @RequestMapping(value = "/{user}/{packageName}/", method = RequestMethod.GET)
    @ResponseBody
    public AssetInfo getUserAsset(@PathVariable String user, @PathVariable String packageName, HttpServletRequest request, HttpServletResponse response) {

        return new AssetManager().getAssetInfo(user, packageName);
    }
}
