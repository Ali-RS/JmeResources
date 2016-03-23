package org.jmonkey.configuration;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jmonkey.database.configuration.BintrayConfiguration;
import org.jmonkey.database.configuration.DatabaseSavedConfiguration;
import org.jmonkey.database.configuration.DiscourseConfiguration;
import org.jmonkey.database.configuration.GeneralConfiguration;

/**
 * @author jayfella
 */

public class Configuration {

    private DatabaseConfiguration databaseConfig;
    private ServerConfiguration serverConfig;
    private WebsiteConfiguration websiteConfig;

    // private GeneralConfiguration generalConfig;
    // private BintrayConfiguration bintrayConfig;
    // private DiscourseConfiguration discourseConfig;
    private DatabaseSavedConfiguration dbsConfig;

    @JsonProperty("database")
    public DatabaseConfiguration getDatabaseConfig() { return databaseConfig; }
    public void setDatabaseConfig(DatabaseConfiguration databaseConfig) { this.databaseConfig = databaseConfig; }

    @JsonProperty(value = "server")
    public ServerConfiguration getServerConfig() { return serverConfig; }
    public void setServerConfig(ServerConfiguration serverConfig) { this.serverConfig = serverConfig; }

    @JsonProperty(value = "website")
    public WebsiteConfiguration getWebsiteConfig() { return websiteConfig; }
    public void setWebsiteConfig(WebsiteConfiguration websiteConfig) { this.websiteConfig = websiteConfig; }

    @JsonIgnore
    public DatabaseSavedConfiguration getDatabaseSavedConfiguration() { return this.dbsConfig; }
    public void setDatabaseSavedConfiguration(DatabaseSavedConfiguration dbsConfig) { this.dbsConfig = dbsConfig; }

    @JsonIgnore
    public BintrayConfiguration getBintrayConfig() { return dbsConfig.getBintrayConfig(); }

    @JsonIgnore
    public DiscourseConfiguration getDiscourseConfig() { return dbsConfig.getDiscourseConfig(); }

    @JsonIgnore
    public GeneralConfiguration getGeneralConfig() { return this.dbsConfig.getGeneralConfig(); }

}
