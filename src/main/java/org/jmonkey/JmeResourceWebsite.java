package org.jmonkey;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.Session;
import org.jmonkey.configuration.Configuration;
import org.jmonkey.configuration.ConfigurationException;
import org.jmonkey.database.DatabaseManager;
import org.jmonkey.database.configuration.DatabaseSavedConfiguration;
import org.jmonkey.server.Server;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.apache.commons.io.IOUtils;

/**
 * @author jayfella
 */

public class JmeResourceWebsite {

    private static JmeResourceWebsite INSTANCE;
    private static ObjectMapper OBJECTMAPPER = new ObjectMapper();
    
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
    
    public static ObjectMapper getObjectMapper() {
        return OBJECTMAPPER;
    }
    
    public Configuration getConfiguration() {
        return this.configuration;
    }

    public void initializeJsonConfiguration() throws IOException, URISyntaxException, ConfigurationException {

        File jsonConfigFile = new File("config.json");

        if (!jsonConfigFile.exists()) {

            try (PrintWriter printWriter = new PrintWriter(jsonConfigFile)) {

                    String sampleContent = new String(IOUtils.toByteArray(getClass().getResourceAsStream("/config-example.json")));
                    printWriter.println(sampleContent);
            }
            
            throw new ConfigurationException("unable to locate 'config.json' configuration file. A template file has been created for you. Please amend it accordingly and restart the application.");
        }

        this.configuration = JmeResourceWebsite.getObjectMapper().readValue(jsonConfigFile, Configuration.class);
    }

    public void initializeDatabaseConfiguration() {

        try (Session session = this.databaseManager.openSession()) {

            DatabaseSavedConfiguration dbsConfig = (DatabaseSavedConfiguration) session.createCriteria(DatabaseSavedConfiguration.class).uniqueResult();
            
            if (dbsConfig == null) {
                dbsConfig = new DatabaseSavedConfiguration();
                
                session.beginTransaction();
                session.save(dbsConfig);
                session.getTransaction().commit();
                session.flush();
            }
            
            configuration.setDatabaseSavedConfiguration(dbsConfig);
        }
    }

    public Server getServer() {
        return this.server;
    }

    public void initializeServer() throws ConfigurationException, IOException {

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
