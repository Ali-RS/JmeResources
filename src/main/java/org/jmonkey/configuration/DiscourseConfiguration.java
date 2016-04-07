package org.jmonkey.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author family
 */

public class DiscourseConfiguration {
    
    private String baseUrl;
    
    @JsonProperty("base-url")
    public String getBaseUrl() { return this.baseUrl; }
    protected void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }
    
}
