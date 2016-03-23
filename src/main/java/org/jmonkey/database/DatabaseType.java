package org.jmonkey.database;

/**
 * @author jayfella
 */

public enum DatabaseType {

    MYSQL("com.mysql.jdbc.Driver", "mysql://", true),
    POSTGRESQL("org.postgresql.Driver", "postgresql://", true),
    SQLITE("org.sqlite.JDBC", "sqlite:", false);

    private final String driver;
    private final String prefix;
    private final boolean requiresPort;

    private DatabaseType(String driver, String prefix, boolean requiresPort) {

        this.driver = driver;
        this.prefix = prefix;
        this.requiresPort = requiresPort;

    }

    public String getDriver() {
        return this.driver;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public boolean requiresPort() {
        return this.requiresPort;
    }

    @Override
    public String toString() {
        return this.name();
    }

}
