package org.jmonkey.server.servlets.api;

import org.jmonkey.database.permission.AdminPermission;
import org.jmonkey.database.user.User;
import org.jmonkey.database.user.UserManager;
import org.jmonkey.server.InsufficientPermissionException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author jayfella
 */

@RestController
@RequestMapping("/user/")
public class UserController {

    @RequestMapping(value = "/search/", method = RequestMethod.POST)
    public List<User> searchUsers(@CookieValue(name = "session", required = false) String cookieSession, @RequestBody String partial) {

        User user = UserManager.fromSession(cookieSession);

        if (!user.hasPermission(AdminPermission.class)) {
            throw new InsufficientPermissionException("Only administrators can perform a username search.");
        }

        List<User> foundUsers = new UserManager().findUser(partial);

        return foundUsers;
    }

}
