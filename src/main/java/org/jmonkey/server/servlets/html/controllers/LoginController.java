package org.jmonkey.server.servlets.html.controllers;

import org.jmonkey.database.user.User;
import org.jmonkey.database.user.UserManager;
import org.jmonkey.external.discourse.DiscourseApiClient;
import org.jmonkey.external.discourse.DiscourseLoginResult;
import org.jmonkey.server.InsufficientPermissionException;
import org.jmonkey.server.ServerResponseException;
import org.jmonkey.server.request.LoginRequest;
import org.jmonkey.server.servlets.html.WebPage;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author jayfella
 */

@Controller
@RequestMapping("/login/")
public class LoginController extends WebPage {

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView displayIndex(@CookieValue(name = "session", required = false) String cookieSession, ModelAndView model, HttpServletRequest request) throws UnsupportedEncodingException {

        User user = UserManager.fromSession(cookieSession);

        model.setViewName("login");
        this.addDefaultPageVariables(model, user, "Login");

        return model;
    }

    @RequestMapping(method = RequestMethod.POST)
    public void login(@RequestBody LoginRequest loginReq, HttpServletResponse response) throws IOException, ServerResponseException {

        DiscourseApiClient apiClient = new DiscourseApiClient();
        DiscourseLoginResult loginResult;

        loginResult = apiClient.login(loginReq.getUser(), loginReq.getPassword());

        if (loginResult.hasError()) {
            throw new InsufficientPermissionException(loginResult.getError());
        }

        // new SessionInspector().createOrUpdateLoginSession(loginResult);
        UserManager userManager = new UserManager();
        userManager.commitSession(loginResult);

        // @TODO: be more restrictive on this cookie?
        Cookie cookie = new Cookie("session", URLEncoder.encode(loginResult.getSession(), "UTF-8"));
        cookie.setPath("/");

        response.addCookie(cookie);
    }

    @RequestMapping(value = "/logout/", method = RequestMethod.POST)
    public void logout(@CookieValue(name = "session", required = false) String cookieSession) throws UnsupportedEncodingException {

        User user = UserManager.fromSession(cookieSession);

        if (user != null) {
            user.invalidateSession();
        }
    }

}
