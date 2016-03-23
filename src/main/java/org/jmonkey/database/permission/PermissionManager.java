package org.jmonkey.database.permission;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.jmonkey.JmeResourceWebsite;
import org.jmonkey.database.user.User;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jayfella
 */
public class PermissionManager {

    public PermissionManager() {

    }

    public List<User> getUsers(Class<? extends JmePermission> permission) {

        try (Session session = JmeResourceWebsite.getInstance().getDatabaseManager().openSession()) {

            @SuppressWarnings("unchecked")
            List<? extends JmePermission> permsList = session.createCriteria(permission).list();

            if (permsList == null || permsList.isEmpty()) {
                return new ArrayList<>();
            }

            Criteria criteria = session.createCriteria(User.class);

            permsList.stream()
                    .map(JmePermission::getDiscourseId)
                    .forEach(id -> criteria.add(Restrictions.eq("discourseId", id)));

            @SuppressWarnings("unchecked")
            List<User> users = criteria.list();

            return users;
        }
    }

    public boolean hasPermission(User user, Class<? extends JmePermission> permission) {

        if (user == null) {
            return false;
        }

        try (Session session = JmeResourceWebsite.getInstance().getDatabaseManager().openSession()) {

            JmePermission perm = (JmePermission) session.createCriteria(permission)
                    .add(Restrictions.eq("discourseId", user.getDiscourseId()))
                    .uniqueResult();

            return (perm != null);
        }
    }

    public void givePermission(User user, Class<? extends JmePermission> permission) {

        try (Session session = JmeResourceWebsite.getInstance().getDatabaseManager().openSession()) {

            switch(permission.getName()) {

                case "org.jmonkey.database.permission.AdminPermission": {

                    JmePermission perm = (JmePermission) session.createCriteria(permission)
                            .add(Restrictions.eq("discourseId", user.getDiscourseId()))
                            .uniqueResult();

                    if (perm == null) {
                        session.beginTransaction();

                        AdminPermission ap = new AdminPermission();
                        ap.setId(-1);
                        ap.setDiscourseId(user.getDiscourseId());

                        session.save(ap);
                        session.getTransaction().commit();
                        session.flush();
                    }

                }
                default: return;

            }

        }

    }

    public void removePermission(User user, Class<? extends JmePermission> permission) {

        try (Session session = JmeResourceWebsite.getInstance().getDatabaseManager().openSession()) {

            switch (permission.getName()) {

                case "org.jmonkey.database.permission.AdminPermission": {

                    AdminPermission ap = (AdminPermission) session.createCriteria(AdminPermission.class)
                            .add(Restrictions.eq("discourseId", user.getDiscourseId()))
                            .uniqueResult();

                    if (ap != null) {
                        session.beginTransaction();
                        session.delete(ap);
                        session.getTransaction().commit();
                        session.flush();
                    }
                }
            }
        }

    }

}
