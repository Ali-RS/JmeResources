package org.jmonkey.database.configuration;

import javax.persistence.*;

/**
 * @author jayfella
 */

@Embeddable
public class BintrayConfiguration extends DatabaseSavedConfiguration {

    private String user;
    private String apiKey;
    private String subject;
    private String repo;

    @Column(name = "bintray_user")
    public String getUser() { return (this.user == null) ? "not-set" : this.user; }
    public void setUser(String user) { this.user = user; }

    @Column(name = "bintray_api_key")
    public String getApiKey() { return (this.apiKey == null) ? "not-set" : this.apiKey; }
    public void setApiKey(String apiKey) { this.apiKey = apiKey; }

    @Column(name = "bintray_subject")
    public String getSubject() { return (this.subject == null) ? "not-set" : this.subject; }
    public void setSubject(String subject) { this.subject = subject; }

    @Column(name = "bintray_repo")
    public String getRepo() { return (this.repo == null) ? "not-set" : this.repo; }
    public void setRepo(String repo) { this.repo = repo; }

}
