package org.jmonkey.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author jayfella
 */
public class ServerConfiguration {
    
    private String bindPath;
    public int bindPort;
    
    @JsonProperty("bind-path")
    public String getBindPath() { return this.bindPath; }
    protected void setBindPath(String bindPath) { this.bindPath = bindPath; }
    
    @JsonProperty("bind-port")
    public int getBindPort() { return this.bindPort; }
    protected void setBindPort(int bindPort) { this.bindPort = bindPort; }
    
    
}
