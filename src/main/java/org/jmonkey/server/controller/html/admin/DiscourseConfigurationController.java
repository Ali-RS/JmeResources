package org.jmonkey.server.controller.html.admin;

import com.sun.org.apache.regexp.internal.RE;
import org.jmonkey.JmeResourceWebsite;
import org.jmonkey.database.configuration.DiscourseConfiguration;
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
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Paths;

/**
 * @author jayfella
 */

@Controller
@RequestMapping("/admin/discourse/")
public class DiscourseConfigurationController {

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public String displayIndex(HttpServletRequest request) throws IOException, TemplateFileNotFoundException {

        File htmlFile = Paths.get(
                JmeResourceWebsite.getInstance().getConfiguration().getWebsiteConfig().getHtmlPath().toString(),
                "admin",
                "discourse_config.html")
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
    public void ModifyDiscourseConfig(@RequestBody DiscourseConfiguration newConfig, HttpServletRequest request) throws UnsupportedEncodingException, InsufficientPermissionException {

        User user = new SessionInspector().getUser(request.getCookies());

        if (!new PermissionManager().hasPermission(user, AdminPermission.class)) {
            throw new InsufficientPermissionException();
        }

        JmeResourceWebsite.getInstance().getConfiguration().getDiscourseConfig().setBaseUrl(newConfig.getBaseUrl());
        JmeResourceWebsite.getInstance().getDatabaseManager().saveConfiguration();
    }

}
