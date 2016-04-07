package org.jmonkey.server.servlets.html.controllers;

import org.jmonkey.JmeResourceWebsite;
import org.jmonkey.database.asset.AssetManager;
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

/**
 * @author jayfella
 */

@Controller
@RequestMapping("/potentialasset/")
public class PotentialAssetController extends WebPage {

    @RequestMapping(value = "/{username}/{packageName}/edit/", method = RequestMethod.GET)
    public ModelAndView editPotentialAsset(
            @CookieValue(name = "session", required = false) String cookieSession,
            ModelAndView model,
            @PathVariable String username,
            @PathVariable String packageName) {

        User user = UserManager.fromSession(cookieSession);

        if (user == null) {
            throw new InsufficientPermissionException("You must log in to edit an asset.");
        }

        AssetManager assetManager = new AssetManager();
        PotentialAsset potentialAsset = assetManager.getPotentialAsset(user, packageName);

        if (potentialAsset == null) {
            throw new ResourceNotFoundException("You not have an asset with package name '" + packageName + "'.");
        }

        if (potentialAsset.getDiscourseAuthorId() != user.getDiscourseId()) {
            throw new InsufficientPermissionException("You cannot edit assets that do not belong to you.");
        }

        model.setViewName("/user/edit_potentialasset");
        this.addDefaultPageVariables(model, user, "Edit Potential Asset");
        model.addObject("potentialAsset", potentialAsset);

        return model;
    }

    @RequestMapping(value = "/{username}/{packageName}/edit/", method = RequestMethod.POST)
    public void editPotentialAsset(
            @CookieValue(name = "session", required = false) String cookieSession,
            @PathVariable String username,
            @PathVariable String packageName,
            @RequestParam String description,
            @RequestParam String showcaseImage,
            @RequestParam String readme,
            HttpServletResponse response) throws IOException {

        User user = UserManager.fromSession(cookieSession);

        if (user == null) {
            throw new InsufficientPermissionException("You must log in to edit an asset.");
        }

        AssetManager assetManager = new AssetManager();
        PotentialAsset potentialAsset = assetManager.getPotentialAsset(user, packageName);

        if (potentialAsset == null) {
            throw new ResourceNotFoundException("You not have an asset with package name '" + packageName + "'.");
        }

        if (potentialAsset.getDiscourseAuthorId() != user.getDiscourseId()) {
            throw new InsufficientPermissionException("You cannot edit assets that do not belong to you.");
        }

        if (!potentialAsset.getRejected()) {
            throw new InsufficientPermissionException("You cannot edit an asset that is awaiting approval.");
        }

        potentialAsset.setVersionDescription(description);
        potentialAsset.setShowcaseImage(showcaseImage);
        potentialAsset.setPackageReadme(readme);

        JmeResourceWebsite.getInstance().getDatabaseManager().updateAnnotatedObject(potentialAsset);

        response.sendRedirect("/asset/user/");
    }

    @RequestMapping(value = "/{username}/{packageName}/resubmit/", method = RequestMethod.POST)
    public void resubmitAsset(
            @CookieValue(name = "session", required = false) String cookieSession,
            @PathVariable String username,
            @PathVariable String packageName,
            HttpServletResponse response) throws IOException {

        User user = UserManager.fromSession(cookieSession);

        if (user == null) {
            throw new InsufficientPermissionException("You must log in to edit an asset.");
        }

        AssetManager assetManager = new AssetManager();
        PotentialAsset potentialAsset = assetManager.getPotentialAsset(user, packageName);

        if (potentialAsset == null) {
            throw new ResourceNotFoundException("You not have an asset with package name '" + packageName + "'.");
        }

        if (potentialAsset.getDiscourseAuthorId() != user.getDiscourseId()) {
            throw new InsufficientPermissionException("You cannot edit assets that do not belong to you.");
        }

        potentialAsset.setRejected(false);
        JmeResourceWebsite.getInstance().getDatabaseManager().updateAnnotatedObject(potentialAsset);

        response.sendRedirect("/asset/user/");
    }
}
