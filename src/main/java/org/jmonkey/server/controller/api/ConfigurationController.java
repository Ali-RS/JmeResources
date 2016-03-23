package org.jmonkey.server.controller.api;

import org.jmonkey.JmeResourceWebsite;
import org.jmonkey.database.configuration.BintrayConfiguration;
import org.jmonkey.database.configuration.DiscourseConfiguration;
import org.jmonkey.database.permission.AdminPermission;
import org.jmonkey.database.permission.PermissionManager;
import org.jmonkey.database.user.SessionInspector;
import org.jmonkey.database.user.User;
import org.jmonkey.database.user.UserManager;
import org.jmonkey.server.InsufficientPermissionException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jayfella
 */

@Controller
@RequestMapping("/configuration/")
public class ConfigurationController {

    @RequestMapping(value = "/general/", method = RequestMethod.GET)
    @ResponseBody
    public Map<Object, Object> getGeneralConfig(HttpServletRequest request) throws UnsupportedEncodingException, InsufficientPermissionException {

        User user = new SessionInspector().getUser(request.getCookies());

        if (!new PermissionManager().hasPermission(user, AdminPermission.class)) {
            throw new InsufficientPermissionException();
        }

        Map<Object, Object> responseData = new HashMap<>();

        responseData.put("admins", new PermissionManager().getUsers(AdminPermission.class));
        responseData.put("general", JmeResourceWebsite.getInstance().getConfiguration().getGeneralConfig());

        return responseData;
    }

    @RequestMapping(value = "/bintray", method = RequestMethod.GET)
    @ResponseBody
    public BintrayConfiguration getBintrayConfig(HttpServletRequest request) throws InsufficientPermissionException, UnsupportedEncodingException {

        User user = new SessionInspector().getUser(request.getCookies());

        if (!new PermissionManager().hasPermission(user, AdminPermission.class)) {
            throw new InsufficientPermissionException();
        }

        return JmeResourceWebsite.getInstance().getConfiguration().getBintrayConfig();
    }

    @RequestMapping(value = "/discourse/", method = RequestMethod.GET)
    @ResponseBody
    public DiscourseConfiguration getDiscourseConfig(HttpServletRequest request) throws UnsupportedEncodingException, InsufficientPermissionException {

        User user = new SessionInspector().getUser(request.getCookies());

        if (!new PermissionManager().hasPermission(user, AdminPermission.class)) {
            throw new InsufficientPermissionException();
        }

        return JmeResourceWebsite.getInstance().getConfiguration().getDiscourseConfig();
    }

}
