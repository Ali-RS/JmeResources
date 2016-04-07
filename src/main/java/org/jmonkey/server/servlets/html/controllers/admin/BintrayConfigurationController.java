package org.jmonkey.server.servlets.html.controllers.admin;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.jmonkey.JmeResourceWebsite;
import org.jmonkey.database.permission.AdminPermission;
import org.jmonkey.database.user.User;
import org.jmonkey.database.user.UserManager;
import org.jmonkey.server.InsufficientPermissionException;
import org.jmonkey.server.servlets.html.WebPage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author jayfella
 */

@Controller
@RequestMapping("/admin/bintray/")
public class BintrayConfigurationController extends WebPage {

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView displayIndex(@CookieValue(name = "session", required = false) String cookieSession, ModelAndView model) {

        User user = UserManager.fromSession(cookieSession);

        if (user == null) {
            throw new InsufficientPermissionException("You must log in to view this page.");
        }

        if (!user.hasPermission(AdminPermission.class)) {
            throw new InsufficientPermissionException("You do not have permission to view this page.");
        }

        model.setViewName("/admin/bintray_config");
        this.addDefaultPageVariables(model, user, "Bintray Configuration");
        model.addObject("config", JmeResourceWebsite.getInstance().getConfiguration().getBintrayConfig());
        
        return model;
    }

    @RequestMapping(method = RequestMethod.POST)
    public void modifySettings(
            @CookieValue(name = "session", required = false) String cookieSession,
            @RequestParam String bintrayUser,
            @RequestParam String bintrayApiKey,
            @RequestParam String bintraySubject,
            @RequestParam String bintrayRepo,
            HttpServletResponse response) throws IOException {

        User user = UserManager.fromSession(cookieSession);

        if (user == null) {
            throw new InsufficientPermissionException("You must login to view this page.");
        }

        if (!user.hasPermission(AdminPermission.class)) {
            throw new InsufficientPermissionException("You do not have permission to view this page.");
        }

        JmeResourceWebsite.getInstance().getConfiguration().getBintrayConfig().setUser(bintrayUser);
        JmeResourceWebsite.getInstance().getConfiguration().getBintrayConfig().setApiKey(bintrayApiKey);
        JmeResourceWebsite.getInstance().getConfiguration().getBintrayConfig().setSubject(bintraySubject);
        JmeResourceWebsite.getInstance().getConfiguration().getBintrayConfig().setRepo(bintrayRepo);

        JmeResourceWebsite.getInstance().getDatabaseManager().saveConfiguration();
        
        response.sendRedirect("/admin/bintray/");
    }

}
