package org.jmonkey.database.user;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.jmonkey.JmeResourceWebsite;
import org.jmonkey.database.user.User;
import org.jmonkey.external.discourse.DiscourseLoginResult;

import javax.servlet.http.Cookie;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

/**
 * @author jayfella
 */
public class SessionInspector {

    public SessionInspector() {

    }

    public static String getForumSessionFromCookies(final Cookie[] cookies) {

        if (cookies == null || cookies.length == 0) {
            return "";
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("_forum_session")) {
                return cookie.getValue();
            }
        }

        return "";
    }

    private String getSessionFromCookies(final Cookie[] cookies) throws UnsupportedEncodingException {

        if (cookies == null || cookies.length == 0) {
            return "";
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("session")) {
                return URLDecoder.decode(cookie.getValue(), "UTF-8");
            }
        }

        return "";
    }

    public User getUser(final Cookie[] cookies) throws UnsupportedEncodingException {

        String forumSession = this.getSessionFromCookies(cookies);

        if (forumSession.isEmpty()) {
            return null;
        }

        try(Session session = JmeResourceWebsite.getInstance().getDatabaseManager().openSession()) {

            User user = (User) session.createCriteria(User.class)
                    .add(Restrictions.eq("session", forumSession))
                    .uniqueResult();

            return user;
        }
    }

    public boolean isLoggedIn(Cookie[] cookies) throws UnsupportedEncodingException {

        return (getUser(cookies) != null);
    }

    public void createOrUpdateLoginSession(DiscourseLoginResult loginResult) throws IOException {

        try(Session session = JmeResourceWebsite.getInstance().getDatabaseManager().openSession()) {

            // @TODO: provide the ability for multiple sessions
            // remove all previous sessions

            @SuppressWarnings("unchecked")
            List<User> activeSessions = session.createCriteria(User.class)
                    .add(Restrictions.eq("username", loginResult.getUser().getUsername()))
                    .list();

            session.beginTransaction();

            activeSessions.forEach(session::delete);

            User user = new User(loginResult);
            session.save(user);

            session.getTransaction().commit();
            session.flush();
        }
    }

    public void deleteSession(User user) {

        try(Session session = JmeResourceWebsite.getInstance().getDatabaseManager().openSession()) {

            session.beginTransaction();
            session.delete(user);
            session.getTransaction().commit();
            session.flush();
        }

    }

}
