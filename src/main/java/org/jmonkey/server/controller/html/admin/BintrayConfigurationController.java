package org.jmonkey.server.controller.html.admin;

import org.jmonkey.JmeResourceWebsite;
import org.jmonkey.database.configuration.BintrayConfiguration;
import org.jmonkey.database.permission.AdminPermission;
import org.jmonkey.database.permission.PermissionManager;
import org.jmonkey.database.user.SessionInspector;
import org.jmonkey.database.user.User;
import org.jmonkey.server.InsufficientPermissionException;
import org.jmonkey.server.ResourceNotFoundException;
import org.jmonkey.server.website.template.TemplateFileNotFoundException;
import org.jmonkey.server.website.template.TemplatedWebPage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jayfella
 */

@Controller
@RequestMapping("/admin/bintray/")
public class BintrayConfigurationController {

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public String displayIndex(HttpServletRequest request) throws IOException, TemplateFileNotFoundException {

        File htmlFile = Paths.get(
                JmeResourceWebsite.getInstance().getConfiguration().getWebsiteConfig().getHtmlPath().toString(),
                "admin",
                "bintray_config.html")
                .toFile();

        if (!htmlFile.exists()) {
            throw new ResourceNotFoundException();
        }

        return new TemplatedWebPage(htmlFile)
                .setPageTitle("Admin Control Panel")
                .useSessionData(request.getCookies())
                .build();
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public void modifySettings(@RequestBody BintrayConfiguration newConfig, HttpServletRequest request) throws UnsupportedEncodingException, InsufficientPermissionException {

        User user = new SessionInspector().getUser(request.getCookies());

        if (!new PermissionManager().hasPermission(user, AdminPermission.class)) {
            throw new InsufficientPermissionException();
        }

        JmeResourceWebsite.getInstance().getConfiguration().getBintrayConfig().setUser(newConfig.getUser());
        JmeResourceWebsite.getInstance().getConfiguration().getBintrayConfig().setApiKey(newConfig.getApiKey());
        JmeResourceWebsite.getInstance().getConfiguration().getBintrayConfig().setSubject(newConfig.getSubject());
        JmeResourceWebsite.getInstance().getConfiguration().getBintrayConfig().setRepo(newConfig.getRepo());

        JmeResourceWebsite.getInstance().getDatabaseManager().saveConfiguration();
    }

}
