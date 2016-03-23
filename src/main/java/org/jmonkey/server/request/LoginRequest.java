package org.jmonkey.server.request;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author jayfella
 */

public class LoginRequest {

    private String user;
    private String password;

    @JsonProperty("user")
    public String getUser() { return this.user; }
    protected void setUser(String user) { this.user = user; }

    @JsonProperty("password")
    public String getPassword() { return this.password; }
    protected void setPassword(String password) { this.password = password; }

}
