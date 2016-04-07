package org.jmonkey.server.servlets.api;

import org.jmonkey.JmeResourceWebsite;
import org.jmonkey.database.asset.AssetInfo;
import org.jmonkey.database.asset.AssetManager;
import org.jmonkey.database.asset.PotentialAsset;
import org.jmonkey.database.user.User;
import org.jmonkey.database.user.UserManager;
import org.jmonkey.server.InsufficientPermissionException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author jayfella
 */

@RestController
@RequestMapping("/asset/")
public class AssetController {

    @RequestMapping(value = "/user/pending/", method = RequestMethod.GET)
    public List<PotentialAsset> getUserPotentialAssets(@CookieValue(name = "session", required = false) String cookieSession) {

        User user = UserManager.fromSession(cookieSession);

        if (user == null) {
            throw new InsufficientPermissionException("You must be logged in to view your assets pending approval.");
        }

        return new AssetManager().getPotentialAssets(user);
    }

    @RequestMapping(value = "/user/", method = RequestMethod.GET)
    public List<AssetInfo> getUserAssets(@CookieValue(name = "session", required = false) String cookieSession) {

        User user = UserManager.fromSession(cookieSession);

        if (user == null) {
            throw new InsufficientPermissionException("Please log in to view your assets, or ass /username/ to the URL.");
        }

        return new AssetManager().getAssets(user);
    }

    @RequestMapping(value = "/{user}/{packageName}/", method = RequestMethod.GET)
    public AssetInfo getUserAsset(@PathVariable String user, @PathVariable String packageName, HttpServletRequest request, HttpServletResponse response) {

        return new AssetManager().getAssetInfo(user, packageName);
    }

    @RequestMapping(value = "/upload/", method = RequestMethod.POST)
    public void uploadPotentialAsset(
            @RequestParam("packageName") String packageName,
            @RequestParam("licenseType") String licenseType,
            @RequestParam("showcaseImage") String showcaseImage,
            @RequestParam("versionDescription") String versionDescription,
            @RequestParam("packageReadme") String packageReadme,
            @RequestParam("assetFile") MultipartFile assetFile,
            @RequestParam("termsAccepted") boolean termsAccepted,
            @CookieValue(name = "session", required = false) String cookieSession,
            HttpServletResponse response) throws IOException {

        User user = UserManager.fromSession(cookieSession);

        if (user == null) {
            throw new InsufficientPermissionException("You must be logged in to upload an asset.");
        }

        if (packageName.isEmpty()) { response.sendError(403, "No package name specified."); }
        if (licenseType.isEmpty()) { response.sendError(403, "No license specified."); }
        if (showcaseImage.isEmpty()) { response.sendError(403, "No showcase image specified."); }
        if (versionDescription.isEmpty()) { response.sendError(403, "No description specified."); }
        if (packageReadme.isEmpty()) { response.sendError(403, "No readme specified."); }
        if (assetFile.isEmpty()) { response.sendError(403, "No file specified."); }
        if (!termsAccepted) { response.sendError(403, "You must accept the terms."); }

        Path uploadsPath = JmeResourceWebsite.getInstance().getConfiguration().getWebsiteConfig().getUploadsPath();

        if (!uploadsPath.toFile().exists()) {
            uploadsPath.toFile().mkdirs();
        }

        Path uploadsAssetsPath = Paths.get(uploadsPath.toString(), "assets");

        if (!uploadsAssetsPath.toFile().exists()) {
            uploadsAssetsPath.toFile().mkdirs();
        }

        Path serverFilePath = Paths.get(uploadsAssetsPath.toString(), user.getUsername() + "-" + packageName + ".jar");

        try (FileOutputStream fos = new FileOutputStream(serverFilePath.toFile()); BufferedOutputStream bos = new BufferedOutputStream(fos)) {
            bos.write(assetFile.getBytes());
        }

        PotentialAsset pa = new PotentialAsset(
                user.getDiscourseId(),
                packageName,
                licenseType,
                showcaseImage,
                versionDescription,
                packageReadme,
                serverFilePath.toString());

        JmeResourceWebsite.getInstance().getDatabaseManager().saveAnnotatedObject(pa);

        response.sendRedirect("/asset/user/");
    }

}
