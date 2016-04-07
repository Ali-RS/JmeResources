package org.jmonkey.server.servlets.html.controllers.admin;

import org.jmonkey.configuration.Configuration;
import org.jmonkey.database.asset.AssetManager;
import org.jmonkey.database.asset.PotentialAssetInfo;
import org.jmonkey.database.permission.AdminPermission;
import org.jmonkey.database.user.User;
import org.jmonkey.database.user.UserManager;
import org.jmonkey.server.InsufficientPermissionException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @author jayfella
 */

@Controller
@RequestMapping("/admin/potentialassets/")
public class PotentialAssetReviewerController {

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView displayPotentialAssets(@CookieValue(name = "session", required = false) String cookieSession, ModelAndView model) {

        User user = UserManager.fromSession(cookieSession);

        if (user == null) {
            throw new InsufficientPermissionException("You must be logged in to view this page.");
        }

        if (!user.hasPermission(AdminPermission.class)) {
            throw new InsufficientPermissionException("You do not have the required permission to view this page.");
        }

        AssetManager assetManager = new AssetManager();
        List<PotentialAssetInfo> infos = assetManager.getPotentialAssetInfos();

        model.setViewName("/admin/potential_assets");
        model.addObject("pageTitle", Configuration.formatPageTitle("Potential Assets"));
        model.addObject("user", user);
        model.addObject("loggedIn", true);
        model.addObject("admin", true);
        model.addObject("potentialAssets", infos);

        return model;
    }
}
