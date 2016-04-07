package org.jmonkey.server.servlets.html.controllers;

import org.jmonkey.database.messaging.PrivateMessage;
import org.jmonkey.database.user.User;
import org.jmonkey.database.user.UserManager;
import org.jmonkey.messaging.PrivateMessageManager;
import org.jmonkey.server.InsufficientPermissionException;
import org.jmonkey.server.servlets.html.WebPage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @author jayfella
 */

@Controller
@RequestMapping("/messages/")
public class PrivateMessageController extends WebPage {


    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView displayMessages(@CookieValue(name = "session", required = false) String cookieSession, ModelAndView model) throws UnsupportedEncodingException {

        User user = UserManager.fromSession(cookieSession);

        if (user == null) {
            throw new InsufficientPermissionException("Please log in to view your messages.");
        }

        model.setViewName("/user/messages");
        this.addDefaultPageVariables(model, user, "Private Messages");

        PrivateMessageManager privateMessageManager = new PrivateMessageManager();
        List<PrivateMessage> messages = privateMessageManager.getPrivateMessages(user);

        model.addObject("messages", messages);

        return model;
    }

    @RequestMapping(value = "/{messageId}/", method = RequestMethod.GET)
    public ModelAndView displayMessage(@CookieValue(name = "session", required = false) String cookieSession, ModelAndView model, @PathVariable long messageId) {

        User user = UserManager.fromSession(cookieSession);

        if (user == null) {
            throw new InsufficientPermissionException("You must log in to view a message.");
        }

        PrivateMessageManager pmManager = new PrivateMessageManager();
        PrivateMessage message = pmManager.getPrivateMessage(messageId);

        if (message.getRecipientId() != user.getDiscourseId()) {
            throw new InsufficientPermissionException("You cannot view other users messages.");
        }

        model.setViewName("/user/message");
        this.addDefaultPageVariables(model, user, "View Message");
        model.addObject("message", message);

        return model;
    }

}
