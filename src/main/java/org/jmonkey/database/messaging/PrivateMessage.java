package org.jmonkey.database.messaging;

import org.jmonkey.database.user.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @author jayfella
 */

@Entity
@Table(name = "messages")
public class PrivateMessage implements Serializable {

    private long id;
    private long parentId;

    private long recipientId;
    private long authorId;


    private String title;
    private String message;

    private User author;

    private List<PrivateMessage> replies;

    public PrivateMessage() {}

    public PrivateMessage(User author, User recipient, String title, String message) {

        this.authorId = author.getDiscourseId();
        this.recipientId = recipient.getDiscourseId();

        this.title = title;
        this.message = message;
    }

    public PrivateMessage(User author, PrivateMessage parent, String message) {

        this.authorId = author.getDiscourseId();
        this.parentId = parent.getId();
        this.message = message;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() { return this.id; }
    protected void setId(long id) { this.id = id; }

    @Column(name = "parentId")
    public long getParentId() { return this.parentId; }
    protected void setParentId(long parentId) { this.parentId = parentId; }

    @Column(name = "authorId")
    public long getAuthorId() { return this.authorId; }
    protected void setAuthorId(long authorId) { this.authorId = authorId; }

    @Column(name = "recipientId")
    public long getRecipientId() { return this.recipientId; }
    protected void setRecipientId(long recipientId) { this.recipientId = recipientId; }

    @Column(name = "title")
    public String getTitle() { return this.title; }
    protected void setTitle(String title) { this.title = title; }

    @Column(name = "message", length = 16384)
    public String getMessage() { return this.message; }
    protected void setMessage(String message) { this.message = message; }

    @Transient
    public User getAuthor() { return author; }
    public void setAuthor(User author) { this.author = author; }

    @Transient
    public List<PrivateMessage> getReplies() { return this.replies; }
    public void setReplies(List<PrivateMessage> replies) { this.replies = replies; }
}
