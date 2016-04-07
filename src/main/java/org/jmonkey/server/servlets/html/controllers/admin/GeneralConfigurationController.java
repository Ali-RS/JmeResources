package org.jmonkey.server.servlets.html.controllers.admin;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.jmonkey.JmeResourceWebsite;
import org.jmonkey.database.configuration.GeneralConfiguration;
import org.jmonkey.database.permission.AdminPermission;
import org.jmonkey.database.permission.PermissionManager;
import org.jmonkey.database.user.User;
import org.jmonkey.database.user.UserManager;
import org.jmonkey.server.InsufficientPermissionException;
import org.jmonkey.server.UserNotFoundException;
import org.jmonkey.server.servlets.html.WebPage;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author jayfella
 */

@Controller
@RequestMapping("/admin/")
public class GeneralConfigurationController extends WebPage {

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView displayAdminIndex(@CookieValue(name = "session", required = false) String cookieSession, ModelAndView model) {

        User user = UserManager.fromSession(cookieSession);

        if (user == null) {
            throw new InsufficientPermissionException("You must log in to view this page.");
        }

        if (!user.hasPermission(AdminPermission.class)) {
            throw new InsufficientPermissionException("You do not have permission to view this page.");
        }

        model.setViewName("/admin/general_config");
        this.addDefaultPageVariables(model, user, "Configuration");

        List<User> administrators = new PermissionManager().getUsers(AdminPermission.class);
        model.addObject("admins", administrators);
        
        return model;
    }

    @RequestMapping(value = "/admins/add/", method = RequestMethod.POST)
    public void addAdmin(
            @CookieValue(name = "session", required = false) String cookieSession,
            @RequestParam String adminUsername,
            HttpServletResponse response) throws IOException {

        User user = UserManager.fromSession(cookieSession);

        if (user == null) {
            throw new InsufficientPermissionException("You must login to carry out this action.");
        }

        if (!user.hasPermission(AdminPermission.class)) {
            throw new InsufficientPermissionException("You do not have permission to add administrators.");
        }

        User newAdminUser = new UserManager().getUser(adminUsername);

        if (newAdminUser == null) {
            throw new UserNotFoundException("User '" + adminUsername + "' not found");
        }

        PermissionManager permissionManager = new PermissionManager();
        permissionManager.givePermission(newAdminUser, AdminPermission.class);
        
        response.sendRedirect("/admin/");
    }

    @RequestMapping(value = "/admins/remove/", method = RequestMethod.POST)
    public void removeAdmin(
            @CookieValue(name = "session", required = false) String cookieSession,
            @RequestParam String adminUsername,
            HttpServletResponse response) throws IOException {

        User user = UserManager.fromSession(cookieSession);

        if (user == null) {
            throw new InsufficientPermissionException("You must login to carry out this action.");
        }

        if (!user.hasPermission(AdminPermission.class)) {
            throw new InsufficientPermissionException("You do not have permission to remove administrators.");
        }

        User oldAdminUser = new UserManager().getUser(adminUsername);

        if (oldAdminUser == null) {
            throw new UserNotFoundException("User '" + adminUsername + "' not found");
        }

        PermissionManager permissionManager = new PermissionManager();
        
        List<User> admins = permissionManager.getUsers(AdminPermission.class);
        
        if (admins.size() == 1) {
            throw new InsufficientPermissionException("There must be at least one administrator.");
        }
        
        if (user.getUsername().equals(oldAdminUser.getUsername())) {
            
            throw new InsufficientPermissionException("You cannot remove yourself as admin.");
        }
        
        permissionManager.removePermission(oldAdminUser, AdminPermission.class);
        
        response.sendRedirect("/admin/"); 
    }

    @RequestMapping(method = RequestMethod.POST)
    public void modifyGeneralSettings(@CookieValue(name = "session", required = false) String cookieSession, @RequestBody GeneralConfiguration newConfig) {

        User user = UserManager.fromSession(cookieSession);

        if (user == null) {
            throw new InsufficientPermissionException("You must login to carry out this action.");
        }

        if (!user.hasPermission(AdminPermission.class)) {
            throw new InsufficientPermissionException("You do not have permission to modify settings.");
        }

        JmeResourceWebsite.getInstance().getConfiguration().getGeneralConfig().setUserAgent(newConfig.getUserAgent());
        JmeResourceWebsite.getInstance().getConfiguration().getGeneralConfig().setPageTitlePrepend(newConfig.getPageTitlePrepend());
        JmeResourceWebsite.getInstance().getConfiguration().getGeneralConfig().setPageTitleAppend(newConfig.getPageTitleAppend());

        JmeResourceWebsite.getInstance().getDatabaseManager().saveConfiguration();
    }

}
