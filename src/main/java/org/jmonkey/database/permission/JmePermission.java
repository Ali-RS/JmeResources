package org.jmonkey.database.permission;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author jayfella
 */

@MappedSuperclass
public abstract class JmePermission implements Serializable {

    private long id;
    private long discourseId;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() { return this.id; }
    protected void setId(long id) { this.id = id; }

    @Column(name = "discourse_id")
    public long getDiscourseId() { return discourseId; }
    protected void setDiscourseId(long discourseId) { this.discourseId = discourseId; }
}
