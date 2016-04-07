package org.jmonkey.database.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.Session;
import org.jmonkey.JmeResourceWebsite;
import org.jmonkey.database.permission.JmePermission;
import org.jmonkey.database.permission.PermissionManager;
import org.jmonkey.external.discourse.DiscourseLoginResult;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @author jayfella
 */

@Entity
@Table(name = "users")
public class User implements Serializable {

    private long id;

    private String username;
    private String session;
    private long discourseId;

    protected User() { }

    public User(DiscourseLoginResult loginResult) {

        this.id = -1;
        this.username = loginResult.getUser().getUsername();
        this.discourseId = loginResult.getUser().getId();
        this.session = loginResult.getSession();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    public long getId() { return this.id; }
    protected void setId(long id) { this.id = id; }

    @Column(name = "username")
    public String getUsername() { return this.username; }
    protected void setUsername(String username) { this.username = username; }

    @JsonIgnore // never send this information via json
    @Column(name = "session", length = 1024)
    public String getSession() { return session; }
    protected void setSession(String session) { this.session = session; }

    @Column(name = "discourse_id")
    @JsonIgnore
    public long getDiscourseId() { return this.discourseId; }
    protected void setDiscourseId(long discourseId) { this.discourseId = discourseId; }

    @Transient
    public boolean hasPermission(Class<? extends JmePermission> permission) {
        return PermissionManager.hasPermission(this, permission);
    }

    @Transient
    public void invalidateSession() {

        try(Session session = JmeResourceWebsite.getInstance().getDatabaseManager().openSession()) {

            this.setSession(null);
            session.beginTransaction();
            session.update(this);
            session.getTransaction().commit();
            session.flush();
        }
    }
}
