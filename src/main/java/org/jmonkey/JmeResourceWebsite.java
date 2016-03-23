package org.jmonkey;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.fabric.xmlrpc.base.Data;
import org.hibernate.Session;
import org.jmonkey.configuration.Configuration;
import org.jmonkey.configuration.ConfigurationException;
import org.jmonkey.database.DatabaseManager;
import org.jmonkey.database.configuration.BintrayConfiguration;
import org.jmonkey.database.configuration.DatabaseSavedConfiguration;
import org.jmonkey.database.configuration.DiscourseConfiguration;
import org.jmonkey.database.configuration.GeneralConfiguration;
import org.jmonkey.server.Server;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author jayfella
 */

public class JmeResourceWebsite {

    private static JmeResourceWebsite INSTANCE;
    
    private Configuration configuration;
    private DatabaseManager databaseManager;
    private Server server;
    
    private JmeResourceWebsite() {
        
    }
    
    public static JmeResourceWebsite getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new JmeResourceWebsite();
        }
        
        return INSTANCE;
    }
    
    public Configuration getConfiguration() {
        return this.configuration;
    }

    public void initializeJsonConfiguration() throws IOException, URISyntaxException, ConfigurationException {

        File jsonConfigFile = new File("config.json");

        if (!jsonConfigFile.exists()) {

            try (PrintWriter printWriter = new PrintWriter(jsonConfigFile)) {

                String sampleContent = new String(Files.readAllBytes(Paths.get(getClass().getResource("/config-example.json").toURI())));
                printWriter.println(sampleContent);
            }

            throw new ConfigurationException("unable to locate 'config.json' configuration file. A template file has been created for you. Please amend it accordingly and restart the application.");
        }

        ObjectMapper objectMapper = new ObjectMapper();
        this.configuration = objectMapper.readValue(jsonConfigFile, Configuration.class);
    }

    public void initializeDatabaseConfiguration() {

        try (Session session = this.databaseManager.openSession()) {

            DatabaseSavedConfiguration dbsConfig = (DatabaseSavedConfiguration) session.createCriteria(DatabaseSavedConfiguration.class).uniqueResult();
            configuration.setDatabaseSavedConfiguration(dbsConfig);
        }
    }


    public Server getServer() {
        return this.server;
    }

    public void initializeServer() throws ConfigurationException {

        if (configuration == null) {
            throw new ConfigurationException("configuration has not yet been specified! Unable to initialize server.");
        }
        
        if (server == null) {
            server = new Server();
        }
    }
    
    public DatabaseManager getDatabaseManager() { return this.databaseManager; }

    public void initializeDatabase() throws ConfigurationException {

        if (configuration == null) {
            throw new ConfigurationException("configuration has not yet been specified! Unable to initialize database.");
        }

        if (databaseManager == null) {
            databaseManager = new DatabaseManager();
        }
    }

}
