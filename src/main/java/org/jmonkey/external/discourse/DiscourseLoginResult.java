package org.jmonkey.external.discourse;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.http.cookie.Cookie;

/**
 * @author jayfella
 */


@JsonIgnoreProperties(ignoreUnknown = true)
public class DiscourseLoginResult {

    private String error = "";
    private String session;
    private Cookie[] cookies;

    private User user;

    protected DiscourseLoginResult() { }

    @JsonProperty("error")
    public String getError() { return error; }
    protected void setError(String error) { this.error = error; }

    @JsonIgnore
    public boolean hasError() {
        return !this.error.isEmpty();
    }

    @JsonIgnore
    public String getSession() { return this.session; }
    protected void setSession(String session) { this.session = session; }

    @JsonProperty("user")
    public User getUser() { return this.user; }
    protected void setUser(User user) { this.user = user; }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class User {

        private long id;
        private String username;

        @JsonProperty("id")
        public long getId() { return this.id; }
        protected void setId(long id) { this.id = id; }

        @JsonProperty("username")
        public String getUsername() { return username; }
        protected void setUsername(String username) { this.username = username; }
    }

}
