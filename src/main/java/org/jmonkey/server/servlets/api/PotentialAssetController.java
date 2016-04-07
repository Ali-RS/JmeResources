package org.jmonkey.server.servlets.api;

import org.jmonkey.JmeResourceWebsite;
import org.jmonkey.database.asset.Asset;
import org.jmonkey.database.asset.AssetManager;
import org.jmonkey.database.asset.AssetVersion;
import org.jmonkey.database.asset.PotentialAsset;
import org.jmonkey.database.messaging.PrivateMessage;
import org.jmonkey.database.permission.AdminPermission;
import org.jmonkey.database.user.User;
import org.jmonkey.database.user.UserManager;
import org.jmonkey.server.InsufficientPermissionException;
import org.jmonkey.server.ResourceNotFoundException;
import org.jmonkey.server.UserNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author jayfella
 */

@RestController
@RequestMapping("/potentialasset/")
public class PotentialAssetController {

    @RequestMapping(value = "/approve/", method = RequestMethod.POST)
    public void approvePotentialAsset(@CookieValue(name = "session", required = false) String cookieSession, @RequestBody long potentialAssetId) {

        User user = UserManager.fromSession(cookieSession);

        if (!user.hasPermission(AdminPermission.class)) {
            throw new InsufficientPermissionException("Only administrators can approve potential assets.");
        }

        AssetManager assetManager = new AssetManager();
        PotentialAsset potentialAsset = assetManager.getPotentialAsset(potentialAssetId);

        if (potentialAsset == null) {
            throw new ResourceNotFoundException("No potential asset found with id: " + potentialAssetId);
        }

        /*
        BintrayApiClient bintray = new BintrayApiClient();

        if (!bintray.createPackage(potentialAsset)) {
            throw new IOException("Unable to create bintray package");
        }
        */

        String version = "1.0";

        Asset asset = new Asset(potentialAsset);
        asset.setReadme(potentialAsset.getPackageReadme());

        AssetVersion assetVersion = new AssetVersion(version);
        assetVersion.setShowcaseImage(potentialAsset.getShowcaseImage());
        assetVersion.setDescription(potentialAsset.getVersionDescription());
        assetVersion.setTimeUploaded(System.currentTimeMillis());

        /*
        if (!bintray.createPackageVersion(asset, assetVersion)) {
            throw new IOException("Unable to create bintray version");
        }

        if (!bintray.uploadPackage(potentialAsset, asset, assetVersion)) {
            throw new IOException("Unable to upload asset to bintray");
        }
        */

        JmeResourceWebsite.getInstance().getDatabaseManager().saveAnnotatedObject(asset);

        // set the associated asset ID after it has been saved.
        // it won't have an incremental ID set until it is saved.
        assetVersion.setAssetId(asset.getId());
        JmeResourceWebsite.getInstance().getDatabaseManager().saveAnnotatedObject(assetVersion);

        // delete the potential asset, because we don't need it anymore.
        JmeResourceWebsite.getInstance().getDatabaseManager().deleteAnnotatedObject(potentialAsset);
    }

    @RequestMapping(value = "/reject/", method = RequestMethod.POST)
    public void rejectPotentialAsset(@CookieValue(name = "session", required = false) String cookieSession, @RequestParam Long potentialAssetId, @RequestParam String reason) {

        User user = UserManager.fromSession(cookieSession);

        if (user == null) {
            throw new InsufficientPermissionException("You must log in to view this page.");
        }

        if (!user.hasPermission(AdminPermission.class)) {
            throw new InsufficientPermissionException("Only administrators can approve potential assets.");
        }

        AssetManager assetManager = new AssetManager();
        PotentialAsset potentialAsset = assetManager.getPotentialAsset(potentialAssetId);

        if (potentialAsset == null) {
            throw new ResourceNotFoundException("No potential asset found with id: " + potentialAssetId);
        }

        UserManager userManager = new UserManager();

        User potentialAssetAuthor = userManager.getUser(potentialAsset.getDiscourseAuthorId());

        if (potentialAssetAuthor == null) {
            throw new UserNotFoundException("The author of this plugin does not appear to exist.");
        }

        PrivateMessage pm = new PrivateMessage(
                user,
                potentialAssetAuthor,
                "Asset '" + potentialAsset.getPackageName() + "' has been rejected.",
                reason);

        JmeResourceWebsite.getInstance().getDatabaseManager().saveAnnotatedObject(pm);

        potentialAsset.setRejected(true);
        JmeResourceWebsite.getInstance().getDatabaseManager().updateAnnotatedObject(potentialAsset);
    }

    @RequestMapping(value = "/delete/", method = RequestMethod.POST)
    public void deletePotentialAsset(@CookieValue(name = "session", required = false) String cookieSession, @RequestBody long potentialAssetId, String reason) {

        User user = UserManager.fromSession(cookieSession);

        if (!user.hasPermission(AdminPermission.class)) {
            throw new InsufficientPermissionException("Only administrators can delete potential assets.");
        }

        AssetManager assetManager = new AssetManager();
        PotentialAsset potentialAsset = assetManager.getPotentialAsset(potentialAssetId);

        if (potentialAsset == null) {
            throw new ResourceNotFoundException("No potential asset found with id: " + potentialAssetId);
        }

        UserManager userManager = new UserManager();
        User potentialAssetAuthor = userManager.getUser(potentialAsset.getDiscourseAuthorId());

        if (potentialAssetAuthor == null) {
            throw new UserNotFoundException("The author of this plugin does not appear to exist.");
        }

        PrivateMessage pm = new PrivateMessage(
                user,
                potentialAssetAuthor,
                "Asset '" + potentialAsset.getPackageName() + "' has been deleted.",
                reason);

        JmeResourceWebsite.getInstance().getDatabaseManager().saveAnnotatedObject(pm);
        JmeResourceWebsite.getInstance().getDatabaseManager().deleteAnnotatedObject(potentialAsset);
    }
}
