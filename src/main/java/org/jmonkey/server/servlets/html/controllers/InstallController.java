package org.jmonkey.server.servlets.html.controllers;

import org.jmonkey.database.permission.AdminPermission;
import org.jmonkey.database.permission.PermissionManager;
import org.jmonkey.database.user.User;
import org.jmonkey.database.user.UserManager;
import org.jmonkey.server.InsufficientPermissionException;
import org.jmonkey.server.servlets.html.WebPage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.util.List;

/**
 * @author jayfella
 */

@Controller
@RequestMapping("/install/")
public class InstallController extends WebPage {

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView displayIndex(@CookieValue(name = "session", required = false) String cookieSession, ModelAndView model) {

        @SuppressWarnings("unchecked")
        List<User> admins = new PermissionManager().getUsers(AdminPermission.class);

        if (!admins.isEmpty()) {
            throw new InsufficientPermissionException("Install already completed.");
        }

        User user = UserManager.fromSession(cookieSession);

        if (user == null) {
            throw new InsufficientPermissionException("You must log in before you can begin installation.");
        }

        model.setViewName("/admin/install");
        this.addDefaultPageVariables(model, user, "Install");

        return model;
    }

    @RequestMapping(method = RequestMethod.POST)
    public void validateInstall(@CookieValue(name = "session", required = false) String cookieSession, @RequestBody String body, HttpServletResponse response) throws IOException {

        PermissionManager permissionManager = new PermissionManager();
        List<User> admins = permissionManager.getUsers(AdminPermission.class);

        if (!admins.isEmpty()) {
            throw new InsufficientPermissionException("Installation is already complete. Please use the Admin Control Panel to add more administrators.");
        }

        User user = UserManager.fromSession(cookieSession);

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
