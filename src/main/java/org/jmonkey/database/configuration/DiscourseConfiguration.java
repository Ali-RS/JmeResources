package org.jmonkey.database.configuration;

import javax.persistence.*;

/**
 * @author jayfella
 */

@Embeddable
public class DiscourseConfiguration extends DatabaseSavedConfiguration {

    private String baseUrl;

    @Column(name = "discourse_baseurl")
    public String getBaseUrl() { return this.baseUrl; }
    public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }

}
