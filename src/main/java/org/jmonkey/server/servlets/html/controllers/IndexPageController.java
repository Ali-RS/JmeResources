package org.jmonkey.server.servlets.html.controllers;

import org.jmonkey.database.asset.AssetInfo;
import org.jmonkey.database.asset.AssetManager;
import org.jmonkey.database.user.User;
import org.jmonkey.database.user.UserManager;
import org.jmonkey.server.servlets.html.WebPage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @author jayfella
 */

@Controller
public class IndexPageController extends WebPage {
    
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView displayIndex(@CookieValue(name = "session", required = false) String cookieSession, ModelAndView model) {

        User user = UserManager.fromSession(cookieSession);

        model.setViewName("index");
        this.addDefaultPageVariables(model, user, "Store");

        List<AssetInfo> randomAssets = new AssetManager().getRandomAssets(20);
        model.addObject("assets", randomAssets);

        return model;
    }

}
