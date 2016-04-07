package org.jmonkey.server.request;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author jayfella
 */

public class MessageReplyRequest {

    private String message;

    @JsonProperty("message")
    public String getMessage() { return this.message; }
    protected void setMessage(String message) { this.message = message; }
}
