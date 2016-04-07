package org.jmonkey.server.servlets.html;

import org.jmonkey.configuration.Configuration;
import org.jmonkey.database.permission.AdminPermission;
import org.jmonkey.database.user.User;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author jayfella
 */

public class WebPage {

    public void addDefaultPageVariables(ModelAndView model, User user, String pageTitle) {

        model.addObject("pageTitle", Configuration.formatPageTitle(pageTitle));
        model.addObject("user", user);
        model.addObject("loggedIn", user != null);
        model.addObject("admin", (user == null) ? null : user.hasPermission(AdminPermission.class));
    }

}
