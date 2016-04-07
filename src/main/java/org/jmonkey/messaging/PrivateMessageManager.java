package org.jmonkey.messaging;

import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.jmonkey.JmeResourceWebsite;
import org.jmonkey.database.messaging.PrivateMessage;
import org.jmonkey.database.user.User;

import java.util.List;

/**
 * @author jayfella
 */
public class PrivateMessageManager {

    public List<PrivateMessage> getPrivateMessages(User user) {

        try (Session session = JmeResourceWebsite.getInstance().getDatabaseManager().openSession()) {

            @SuppressWarnings("unchecked")
            List<PrivateMessage> privateMessages = session.createCriteria(PrivateMessage.class)
                    .add(Restrictions.eq("recipientId", user.getDiscourseId()))
                    .add(Restrictions.eq("parentId", 0L))
                    .list();

            privateMessages.forEach(pm -> {
                User messageAuthor = (User) session.createCriteria(User.class)
                        .add(Restrictions.eq("discourseId", pm.getAuthorId()))
                        .uniqueResult();

                pm.setAuthor(messageAuthor);
            });



            return privateMessages;
        }
    }

    public List<PrivateMessage> getReplies(PrivateMessage parent) {

        try (Session session = JmeResourceWebsite.getInstance().getDatabaseManager().openSession()) {

            @SuppressWarnings("unchecked")
            List<PrivateMessage> replies = session.createCriteria(PrivateMessage.class)
                    .add(Restrictions.eq("parentId", parent.getId()))
                    .addOrder(Order.desc("id"))
                    .list();

            return replies;
        }
    }

    public PrivateMessage getPrivateMessage(long messageId) {

        try (Session session = JmeResourceWebsite.getInstance().getDatabaseManager().openSession()) {

            PrivateMessage pm = session.get(PrivateMessage.class, messageId);

            User author = (User) session.createCriteria(User.class)
                    .add(Restrictions.eq("discourseId", pm.getAuthorId()))
                    .uniqueResult();

            pm.setAuthor(author);

            @SuppressWarnings("unchecked")
            List<PrivateMessage> replies = session.createCriteria(PrivateMessage.class)
                    .add(Restrictions.eq("parentId", pm.getId()))
                    .list();

            replies.forEach(reply -> {

                User replyAuthor = (User) session.createCriteria(User.class)
                        .add(Restrictions.eq("discourseId", reply.getAuthorId()))
                        .uniqueResult();

                reply.setAuthor(replyAuthor);
            });

            pm.setReplies(replies);

            return pm;
        }
    }
}
