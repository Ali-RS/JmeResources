package org.jmonkey.server.controller.html;

import org.jmonkey.JmeResourceWebsite;
import org.jmonkey.database.user.SessionInspector;
import org.jmonkey.database.user.User;
import org.jmonkey.external.discourse.DiscourseApiClient;
import org.jmonkey.external.discourse.DiscourseLoginResult;
import org.jmonkey.server.InsufficientPermissionException;
import org.jmonkey.server.ServerResponseException;
import org.jmonkey.server.request.LoginRequest;
import org.jmonkey.server.website.template.TemplateFileNotFoundException;
import org.jmonkey.server.website.template.TemplatedWebPage;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Paths;

/**
 * @author jayfella
 */

@Controller
@RequestMapping("/login/")
public class LoginController {

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String displayLoginPage(HttpServletRequest request, HttpServletResponse response) throws TemplateFileNotFoundException, IOException {

        File htmlFile = Paths.get(JmeResourceWebsite.getInstance().getConfiguration().getWebsiteConfig().getHtmlPath().toString(), "login.html").toFile();

        if (!htmlFile.exists()) {
            throw new TemplateFileNotFoundException("file 'login.html' does not exist.");
        }

        return new TemplatedWebPage(htmlFile)
                .useSessionData(request.getCookies())
                .setPageTitle("Login")
                .build();
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public void login(@RequestBody LoginRequest loginReq, HttpServletRequest request, HttpServletResponse response) throws IOException, ServerResponseException, InsufficientPermissionException {

        DiscourseApiClient apiClient = new DiscourseApiClient();
        DiscourseLoginResult loginResult;

        loginResult = apiClient.login(loginReq.getUser(), loginReq.getPassword());

        if (loginResult.hasError()) {
            throw new InsufficientPermissionException(loginResult.getError());
        }

        new SessionInspector().createOrUpdateLoginSession(loginResult);

        // @TODO: be more restrictive on this cookie?
        Cookie cookie = new Cookie("session", URLEncoder.encode(loginResult.getSession(), "UTF-8"));
        cookie.setPath("/");

        response.addCookie(cookie);
    }

    @RequestMapping(value = "/logout/", method = RequestMethod.POST)
    @ResponseBody
    public void logout(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {

        SessionInspector inspector = new SessionInspector();
        User user = inspector.getUser(request.getCookies());

        if (user != null) {
            inspector.deleteSession(user);
        }
    }

}
