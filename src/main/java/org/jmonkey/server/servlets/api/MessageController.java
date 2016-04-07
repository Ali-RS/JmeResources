package org.jmonkey.server.servlets.api;

import org.jmonkey.JmeResourceWebsite;
import org.jmonkey.database.messaging.PrivateMessage;
import org.jmonkey.database.user.User;
import org.jmonkey.database.user.UserManager;
import org.jmonkey.messaging.PrivateMessageManager;
import org.jmonkey.server.InsufficientPermissionException;
import org.jmonkey.server.request.MessageReplyRequest;
import org.springframework.web.bind.annotation.*;

/**
 * @author jayfella
 */

@RestController
@RequestMapping("/messages/")
public class MessageController {

    @RequestMapping(value = "/reply/{messageId}/", method = RequestMethod.POST)
    public void replyToMessage(@CookieValue(name = "session", required = false) String cookieSession, @PathVariable Long messageId, @RequestBody MessageReplyRequest replyRequest) {

        User user = UserManager.fromSession(cookieSession);

        if (user == null) {
            throw new InsufficientPermissionException("You must login to reply to a message.");
        }

        PrivateMessageManager pmManager = new PrivateMessageManager();
        PrivateMessage pm = pmManager.getPrivateMessage(messageId);

        if (pm.getRecipientId() != user.getDiscourseId() | pm.getAuthorId() != user.getDiscourseId()) {
            throw new InsufficientPermissionException("You do not have permission to participate in this conversation.");
        }

        PrivateMessage reply = new PrivateMessage(user, pm, replyRequest.getMessage());
        JmeResourceWebsite.getInstance().getDatabaseManager().saveAnnotatedObject(reply);

    }

}
