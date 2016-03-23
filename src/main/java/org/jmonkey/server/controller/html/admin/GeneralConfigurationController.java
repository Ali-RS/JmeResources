package org.jmonkey.server.controller.html.admin;

import org.jmonkey.JmeResourceWebsite;
import org.jmonkey.database.configuration.GeneralConfiguration;
import org.jmonkey.database.permission.AdminPermission;
import org.jmonkey.database.permission.PermissionManager;
import org.jmonkey.database.user.SessionInspector;
import org.jmonkey.database.user.User;
import org.jmonkey.database.user.UserManager;
import org.jmonkey.server.InsufficientPermissionException;
import org.jmonkey.server.ResourceNotFoundException;
import org.jmonkey.server.UserNotFoundException;
import org.jmonkey.server.website.template.TemplateFileNotFoundException;
import org.jmonkey.server.website.template.TemplatedWebPage;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Paths;

/**
 * @author jayfella
 */

@Controller
@RequestMapping("/admin/")
public class GeneralConfigurationController {

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String displayAdminIndex(HttpServletRequest request, HttpServletResponse response) throws TemplateFileNotFoundException, IOException {

        File htmlFile = Paths.get(
                JmeResourceWebsite.getInstance().getConfiguration().getWebsiteConfig().getHtmlPath().toString(),
                "admin",
                "general_config.html")
                .toFile();

        if (!htmlFile.exists()) {
            throw new ResourceNotFoundException();
        }

        return new TemplatedWebPage(htmlFile)
                .setPageTitle("Admin Control Panel")
                .useSessionData(request.getCookies())
                .build();
    }

    @RequestMapping(value = "/admins/add/", method = RequestMethod.POST)
    @ResponseBody
    public void addAdmin(@RequestBody String username, HttpServletRequest request) throws InsufficientPermissionException, UnsupportedEncodingException, UserNotFoundException {

        User user = new SessionInspector().getUser(request.getCookies());
        PermissionManager permissionManager = new PermissionManager();

        if (!permissionManager.hasPermission(user, AdminPermission.class)) {
            throw new InsufficientPermissionException();
        }

        User newAdminUser = new UserManager().getUser(username);

        if (newAdminUser == null) {
            throw new UserNotFoundException("User '" + username + "' not found");
        }

        permissionManager.givePermission(newAdminUser, AdminPermission.class);
    }

    @RequestMapping(value = "/admins/remove", method = RequestMethod.POST)
    @ResponseBody
    public void removeAdmin(@RequestBody String username, HttpServletRequest request) throws IOException, InsufficientPermissionException, UserNotFoundException {

        User user = new SessionInspector().getUser(request.getCookies());
        PermissionManager permissionManager = new PermissionManager();

        if (!permissionManager.hasPermission(user, AdminPermission.class)) {
            throw new InsufficientPermissionException();
        }

        User oldAdminUser = new UserManager().getUser(username);

        if (oldAdminUser == null) {
            throw new UserNotFoundException("User '" + username + "' not found");
        }

        permissionManager.removePermission(oldAdminUser, AdminPermission.class);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public void modifyGeneralSettings(@RequestBody GeneralConfiguration newConfig, HttpServletRequest request) throws InsufficientPermissionException, IOException {

        User user = new SessionInspector().getUser(request.getCookies());
        PermissionManager permissionManager = new PermissionManager();

        if (!permissionManager.hasPermission(user, AdminPermission.class)) {
            throw new InsufficientPermissionException();
        }

        JmeResourceWebsite.getInstance().getConfiguration().getGeneralConfig().setUserAgent(newConfig.getUserAgent());
        JmeResourceWebsite.getInstance().getConfiguration().getGeneralConfig().setPageTitlePrepend(newConfig.getPageTitlePrepend());
        JmeResourceWebsite.getInstance().getConfiguration().getGeneralConfig().setPageTitleAppend(newConfig.getPageTitleAppend());

        JmeResourceWebsite.getInstance().getDatabaseManager().saveConfiguration();
    }

}
