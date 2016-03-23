package org.jmonkey.server.controller.html.admin;

import org.jmonkey.JmeResourceWebsite;
import org.jmonkey.database.asset.PotentialAsset;
import org.jmonkey.database.permission.AdminPermission;
import org.jmonkey.database.permission.PermissionManager;
import org.jmonkey.database.user.SessionInspector;
import org.jmonkey.database.user.User;
import org.jmonkey.server.InsufficientPermissionException;
import org.jmonkey.server.ResourceNotFoundException;
import org.jmonkey.server.website.template.TemplateFileNotFoundException;
import org.jmonkey.server.website.template.TemplatedWebPage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * @author jayfella
 */

@Controller
@RequestMapping("/admin/potentialassets/")
public class PotentialAssetController {

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public String displayPotentialAssets(HttpServletRequest request, HttpServletResponse response) throws IOException, InsufficientPermissionException, TemplateFileNotFoundException {

        File htmlFile = Paths.get(
                JmeResourceWebsite.getInstance().getConfiguration().getWebsiteConfig().getHtmlPath().toString(),
                "admin",
                "potential_assets.html")
                .toFile();

        if (!htmlFile.exists()) {
            throw new ResourceNotFoundException();
        }

        return new TemplatedWebPage(htmlFile)
                .setPageTitle("Potential Assets")
                .useSessionData(request.getCookies())
                .build();
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public void approvePotentialAsset(@RequestBody PotentialAsset potentialAsset, HttpServletRequest request) throws IOException, InsufficientPermissionException {

        User user = new SessionInspector().getUser(request.getCookies());
        PermissionManager permissionManager = new PermissionManager();

        if (!permissionManager.hasPermission(user, AdminPermission.class)) {
            throw new InsufficientPermissionException();
        }
    }
}
