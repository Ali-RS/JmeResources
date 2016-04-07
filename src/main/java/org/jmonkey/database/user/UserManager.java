package org.jmonkey.database.user;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.jmonkey.JmeResourceWebsite;
import org.jmonkey.external.discourse.DiscourseLoginResult;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.Cookie;

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

    public static User fromSession(final String cookieSession) {

        if (cookieSession == null || cookieSession.trim().isEmpty()) {
            return null;
        }

        try (Session session = JmeResourceWebsite.getInstance().getDatabaseManager().openSession()) {

            User user = (User) session.createCriteria(User.class)
                    .add(Restrictions.eq("session", cookieSession))
                    .uniqueResult();

            return user;
        }

    }

    public static User fromCookies(Cookie[] cookies) throws UnsupportedEncodingException {
        
        if (cookies == null || cookies.length == 0) {
            return null;
        }
        
        String cookieSession = "";
        
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("session")) {
                cookieSession = URLDecoder.decode(cookie.getValue(), "UTF-8");
                break;
            }
        }
        
        if (cookieSession.isEmpty()) {
            return null;
        }
        
        return UserManager.fromSession(cookieSession);
    }
    
    public void commitSession(DiscourseLoginResult loginResult) {

        try (Session session = JmeResourceWebsite.getInstance().getDatabaseManager().openSession()) {

            User user = (User) session.createCriteria(User.class)
                    .add(Restrictions.eq("discourseId", loginResult.getUser().getId()))
                    .uniqueResult();

            if (user == null) {
                user = new User(loginResult);
            }
            else {
                user.setSession(loginResult.getSession());
            }

            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
            session.flush();
        }
    }
}
