package org.jmonkey.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jmonkey.database.DatabaseType;

/**
 * @author jayfella
 */

public class DatabaseConfiguration {

    private String name;
    private String user;
    private String password;

    private String host;
    private int port;
    private DatabaseType databaseType;

    @JsonProperty("name")
    public String getName() { return name; }
    protected void setName(String name) { this.name = name; }

    @JsonProperty("user")
    public String getUser() { return user; }
    protected void setUser(String user) { this.user = user; }

    @JsonProperty("password")
    public String getPassword() { return password; }
    protected void setPassword(String password) { this.password = password; }

    @JsonProperty("host")
    public String getHost() { return host; }
    protected void setHost(String host) { this.host = host; }

    @JsonProperty("port")
    public int getPort() { return port; }
    protected void setPort(int port) { this.port = port; }

    @JsonProperty("type")
    public DatabaseType getDatabaseType() { return databaseType; }
    protected void setDatabaseType(String databaseType) { this.databaseType = DatabaseType.valueOf(databaseType); }

}
