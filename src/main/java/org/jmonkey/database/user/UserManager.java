package org.jmonkey.database.user;

import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.jmonkey.JmeResourceWebsite;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jayfella
 */
public class UserManager {

    public List<User> findUser(String partial) {

        if (partial.length() < 3) {
            return new ArrayList<>();
        }

        try (Session session = JmeResourceWebsite.getInstance().getDatabaseManager().openSession()) {

            @SuppressWarnings("unchecked")
            List<User> users = session.createCriteria(User.class)
                    .add(Restrictions.like("username", partial, MatchMode.ANYWHERE))
                    .list();

            return users;
        }
    }

    public User getUser(String username) {

        if (username.isEmpty()) {
            return null;
        }

        try (Session session = JmeResourceWebsite.getInstance().getDatabaseManager().openSession()) {


            User user = (User) session.createCriteria(User.class)
                    .add(Restrictions.eq("username", username))
                    .uniqueResult();

            return user;
        }
    }

    public User getUser(long discourseId) {

        try (Session session = JmeResourceWebsite.getInstance().getDatabaseManager().openSession()) {


            User user = (User) session.createCriteria(User.class)
                    .add(Restrictions.eq("discourseId", discourseId))
                    .uniqueResult();

            return user;
        }
    }

}
