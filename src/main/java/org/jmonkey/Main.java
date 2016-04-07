package org.jmonkey;

import org.jmonkey.configuration.ConfigurationException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jayfella
 */
public class Main {
    
    private static final Logger LOGGER = Logger.getLogger("org.jmonkey");
    
    public static void main(String[] args) throws ConfigurationException, IOException, URISyntaxException {

        // Initialize json-based configuration
        JmeResourceWebsite.getInstance().initializeJsonConfiguration();

        // Initialize the database
        JmeResourceWebsite.getInstance().initializeDatabase();

        // Verify the connection to the database
        if (!JmeResourceWebsite.getInstance().getDatabaseManager().checkConnection()) {
            throw new ConfigurationException("Error attempting to connect to to the database");
        }

        // Initialize the database-based configuration
        JmeResourceWebsite.getInstance().initializeDatabaseConfiguration();

        // Initialize the server
        JmeResourceWebsite.getInstance().initializeServer();

        // start the jetty server
        
        try {
            JmeResourceWebsite.getInstance().getServer().start();
            JmeResourceWebsite.getInstance().getServer().join();
        }
        catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Server Error:", ex);
        }
        
    }
    
}
