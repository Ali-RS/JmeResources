package org.jmonkey.server;

import org.apache.http.HttpStatus;

/**
 * @author jayfella
 */

public class ServerResponseException extends Exception {

    private String url;
    private int statusCode;
    private String reason;

    public ServerResponseException(String url, int statusCode, String reason) {

        super();

        this.url = url;
        this.statusCode = statusCode;
        this.reason = reason;
    }

    public String getUrl() { return this.url; }
    public int getStatusCode() { return this.statusCode; }
    public String getReason() { return this.reason; }

}
