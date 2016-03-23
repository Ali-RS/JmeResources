package org.jmonkey.server.controller.api;

import org.jmonkey.database.permission.AdminPermission;
import org.jmonkey.database.permission.PermissionManager;
import org.jmonkey.database.user.SessionInspector;
import org.jmonkey.database.user.User;
import org.jmonkey.database.user.UserManager;
import org.jmonkey.server.InsufficientPermissionException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @author jayfella
 */

@Controller
@RequestMapping("/user/")
public class UserController {

    @RequestMapping(value = "/permission/admin/", method = RequestMethod.GET)
    @ResponseBody
    public List<User> getAdmins(HttpServletRequest request) throws InsufficientPermissionException, UnsupportedEncodingException {

        User user = new SessionInspector().getUser(request.getCookies());
        boolean isAdmin = new PermissionManager().hasPermission(user, AdminPermission.class);

        if (!isAdmin) {
            throw new InsufficientPermissionException();
        }

        @SuppressWarnings("unchecked")
        List<User> admins = new PermissionManager().getUsers(AdminPermission.class);

        return admins;
    }

    @RequestMapping(value = "/search/", method = RequestMethod.POST)
    @ResponseBody
    public List<User> searchUsers(@RequestBody String partial, HttpServletRequest request, HttpServletResponse response) throws IOException, InsufficientPermissionException {

        User user = new SessionInspector().getUser(request.getCookies());
        boolean isAdmin = new PermissionManager().hasPermission(user, AdminPermission.class);

        if (!isAdmin) {
            throw new InsufficientPermissionException();
        }

        List<User> foundUsers = new UserManager().findUser(partial);

        return foundUsers;
    }

}
