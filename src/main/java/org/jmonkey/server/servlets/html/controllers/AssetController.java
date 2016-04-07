package org.jmonkey.server.servlets.html.controllers;

import org.jmonkey.JmeResourceWebsite;
import org.jmonkey.database.asset.AssetInfo;
import org.jmonkey.database.asset.AssetManager;
import org.jmonkey.database.asset.AssetVersion;
import org.jmonkey.database.asset.PotentialAsset;
import org.jmonkey.database.user.User;
import org.jmonkey.database.user.UserManager;
import org.jmonkey.server.InsufficientPermissionException;
import org.jmonkey.server.ResourceNotFoundException;
import org.jmonkey.server.servlets.html.WebPage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * @author jayfella
 */

@Controller
@RequestMapping("/asset/")
public class AssetController extends WebPage {

    @RequestMapping(value = "/upload/", method = RequestMethod.GET)
    public ModelAndView displayUploadPage(@CookieValue(name = "session", required = false) String cookieSession, ModelAndView model) {

        User user = UserManager.fromSession(cookieSession);

        if (user == null) {
            throw new InsufficientPermissionException("You must log in before you can upload an asset.");
        }

        model.setViewName("/asset/upload_asset");
        this.addDefaultPageVariables(model, user, "Upload Asset");

        return model;
    }

    @RequestMapping(value = "/user/", method = RequestMethod.GET)
    public ModelAndView displayUserAssets(@CookieValue(name = "session", required = false) String cookieSession, ModelAndView model) {

        User user = UserManager.fromSession(cookieSession);

        if (user == null) {
            throw new InsufficientPermissionException("Please log in to view your assets.");
        }

        model.setViewName("/user/my_assets");
        this.addDefaultPageVariables(model, user, "My Assets");

        AssetManager assetManager = new AssetManager();
        List<PotentialAsset> potentialAssets = assetManager.getPotentialAssets(user);
        List<AssetInfo> approvedAssetInfos = assetManager.getAssets(user);

        model.addObject("pendingAssets", potentialAssets);
        model.addObject("assets", approvedAssetInfos);

        return model;
    }

    @RequestMapping(value = "/{username}/{packageName}/{version}/", method = RequestMethod.GET)
    public ModelAndView displayUserAsset(
            @CookieValue(name = "session", required = false) String cookieSession,
            ModelAndView model,
            @PathVariable String username,
            @PathVariable String packageName,
            @PathVariable String version) {

        User user = UserManager.fromSession(cookieSession);

        AssetManager assetManager = new AssetManager();
        AssetInfo assetInfo = assetManager.getAssetInfo(username, packageName, version);

        if (assetInfo == null) {
            throw new ResourceNotFoundException("The requested asset does not exist.");
        }

        model.setViewName("/asset/view_asset");
        this.addDefaultPageVariables(model, user, assetInfo.getAsset().getPackageName());

        model.addObject("assetInfo", assetInfo);

        return model;
    }

    @RequestMapping(value = "/{username}/{packageName}/{versionNum}/edit/", method = RequestMethod.GET)
    public ModelAndView editUserAsset(
            @CookieValue(name = "session", required = false) String cookieSession,
            ModelAndView model,
            @PathVariable String username,
            @PathVariable String packageName,
            @PathVariable String versionNum) throws IOException {

        User user = UserManager.fromSession(cookieSession);

        if (user == null) {
            throw new InsufficientPermissionException("You must log in to edit an asset.");
        }

        AssetInfo assetInfo = new AssetManager().getAssetInfo(username, packageName, versionNum);

        if (assetInfo == null) {
            throw new ResourceNotFoundException("You not have an asset with package name '" + packageName + "'.");
        }

        if (assetInfo.getUser().getDiscourseId() != user.getDiscourseId()) {
            throw new InsufficientPermissionException("You cannot edit assets that do not belong to you.");
        }

        if (assetInfo.getVersions().isEmpty()) {
            throw new ResourceNotFoundException("The requested version does not exist.");
        }

        model.setViewName("/user/edit_asset");
        this.addDefaultPageVariables(model, user, "Edit Asset");

        model.addObject("assetInfo", assetInfo);

        return model;
    }

    @RequestMapping(value = "/{username}/{packageName}/{versionNum}/edit/", method = RequestMethod.POST)
    public void modifyUserAsset(
            @CookieValue(name = "session", required = false) String cookieSession,
            @PathVariable String username,
            @PathVariable String packageName,
            @PathVariable String versionNum,
            @RequestParam String versionDesc,
            @RequestParam String versionImage,
            @RequestParam String readme,
            HttpServletResponse response) throws IOException {

        User user = UserManager.fromSession(cookieSession);

        if (user == null) {
            throw new InsufficientPermissionException("You must log in to edit an asset.");
        }

        AssetManager assetManager = new AssetManager();
        AssetInfo assetInfo =  assetManager.getAssetInfo(username, packageName, versionNum);

        if (assetInfo == null) {
            throw new ResourceNotFoundException("You not have an asset with package name '" + packageName + "'.");
        }

        if (assetInfo.getUser().getDiscourseId() != user.getDiscourseId()) {
            throw new InsufficientPermissionException("You cannot edit assets that do not belong to you.");
        }

        assetInfo.getVersions().get(0).setDescription(versionDesc);
        assetInfo.getVersions().get(0).setShowcaseImage(versionImage);
        JmeResourceWebsite.getInstance().getDatabaseManager().updateAnnotatedObject(assetInfo.getVersions().get(0));

        assetInfo.getAsset().setReadme(readme);
        JmeResourceWebsite.getInstance().getDatabaseManager().updateAnnotatedObject(assetInfo.getAsset());

        response.sendRedirect("/asset/" + username + "/" + packageName + "/" + versionNum + "/");
    }


}
