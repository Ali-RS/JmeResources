package org.jmonkey.server.controller.html;

import org.jmonkey.JmeResourceWebsite;
import org.jmonkey.database.permission.AdminPermission;
import org.jmonkey.database.permission.PermissionManager;
import org.jmonkey.database.user.SessionInspector;
import org.jmonkey.database.user.User;
import org.jmonkey.server.InsufficientPermissionException;
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
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author jayfella
 */

@Controller
@RequestMapping("/install/")
public class InstallController {

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public String displayIndex(HttpServletRequest request, HttpServletResponse response) throws Exception {

        @SuppressWarnings("unchecked")
        List<User> admins = new PermissionManager().getUsers(AdminPermission.class);

        if (!admins.isEmpty()) {
            response.sendError(403, "INSTALL_COMPLETED");
        }

        User user = new SessionInspector().getUser(request.getCookies());

        if (user == null) {
            response.sendError(403, "GUEST_NOT_ALLOWED");
        }

        File indexFile = Paths.get(JmeResourceWebsite.getInstance().getConfiguration().getWebsiteConfig().getHtmlPath().toString(), "install.html").toFile();

        if (!indexFile.exists()) {
            throw new TemplateFileNotFoundException("file 'index.html' does not exist.");
        }

        return new TemplatedWebPage(indexFile)
                .setPageTitle("INSTALL")
                .useSessionData(request.getCookies())
                .build();
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public void validateInstall(@RequestBody String body, HttpServletRequest request, HttpServletResponse response) throws TemplateFileNotFoundException, IOException, InsufficientPermissionException {

        PermissionManager permissionManager = new PermissionManager();
        List<User> admins = permissionManager.getUsers(AdminPermission.class);

        // only one admin should be added in the install process.
        // If an administrator is already present, then this process should not proceed.
        if (!admins.isEmpty()) {
            throw new InsufficientPermissionException("Installation is already complete. Please use the Admin Control Panel to add more administrators.");
        }

        User user = new SessionInspector().getUser(request.getCookies());

        if (user == null) {
            throw new InsufficientPermissionException("You must be logged in to proceed.");
        }

        File secretFile = new File("secret.txt");

        if (!secretFile.exists()) {
            throw new InsufficientPermissionException("Place a file called 'secret.txt' in the application root folder that contains a secret phrase.");
        }

        String secret = new String(Files.readAllBytes(secretFile.toPath())).trim();
        String givenSecret = URLDecoder.decode(body.substring((7)).trim(), "UTF-8");

        if (!secret.equals(givenSecret)) {
            throw new InsufficientPermissionException("Incorrect pass-phrase");
        }

        permissionManager.givePermission(user, AdminPermission.class);
        response.sendRedirect("/admin/");
    }

}
