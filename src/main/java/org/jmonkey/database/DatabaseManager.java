package org.jmonkey.database;

import java.io.Closeable;
import java.io.IOException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;
import org.jmonkey.JmeResourceWebsite;
import org.jmonkey.configuration.DatabaseConfiguration;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author jayfella
 */

public class DatabaseManager implements Closeable {

    private static final Logger LOG = Logger.getLogger("org.jmonkey.database");
    private final SessionFactory sessionFactory;

    public DatabaseManager() {

        DatabaseConfiguration config = JmeResourceWebsite.getInstance().getConfiguration().getDatabaseConfig();

        String connectionString = this.buildConnectionString(config.getDatabaseType(), config);

        org.hibernate.cfg.Configuration c = new org.hibernate.cfg.Configuration();
        c.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        c.setProperty("hibernate.connection.driver_class", config.getDatabaseType().getDriver());
        c.setProperty("hibernate.connection.url", connectionString);
        c.setProperty("hibernate.connection.username", config.getUser());
        c.setProperty("hibernate.connection.password", config.getPassword());
        c.setProperty("hibernate.connection.autoReconnect", "true");

        c.setProperty("connection.provider_class", "org.hibernate.connection.C3P0ConnectionProvider");
        c.setProperty("c3p0.breakAfterAcquireFailure", "false");
        c.setProperty("c3p0.idleConnectionTestPeriod", "30");
        c.setProperty("c3p0.min_size", "5");
        c.setProperty("c3p0.max_size", "120");
        c.setProperty("c3p0.acquireIncrement", "5");
        c.setProperty("c3p0.timeout", "40");
        c.setProperty("c3p0.maxStatementsPerConnection", "10");
        c.setProperty("hibernate.c3p0.testConnectionOnCheckout", "true");

        // @TODO: this line should be enabled during installation ONLY.
        // Creates tables and columns if they do not exist.
        c.setProperty("hibernate.hbm2ddl.auto", "update");

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(c.getProperties()).build();

        // add annotated classes here
        sessionFactory = c
                .addAnnotatedClass(org.jmonkey.database.user.User.class)

                // configuration
                .addAnnotatedClass(org.jmonkey.database.configuration.DatabaseSavedConfiguration.class)
                .addAnnotatedClass(org.jmonkey.database.configuration.BintrayConfiguration.class)
                .addAnnotatedClass(org.jmonkey.database.configuration.GeneralConfiguration.class)

                // permissions
                .addAnnotatedClass(org.jmonkey.database.permission.JmePermission.class)
                .addAnnotatedClass(org.jmonkey.database.permission.AdminPermission.class)

                // private messages
                .addAnnotatedClass(org.jmonkey.database.messaging.PrivateMessage.class)

                // resources
                .addAnnotatedClass(org.jmonkey.database.asset.PotentialAsset.class)
                .addAnnotatedClass(org.jmonkey.database.asset.Asset.class)
                .addAnnotatedClass(org.jmonkey.database.asset.AssetVersion.class)

                .buildSessionFactory(serviceRegistry);

        LOG.log(Level.INFO, "Built database connection to: {0}", connectionString);
        
    }

    private String buildConnectionString(DatabaseType dbType, DatabaseConfiguration config)
    {
        StringBuilder connectionString = new StringBuilder()
                .append("jdbc:")
                .append(dbType.getPrefix())
                .append(config.getHost());

        if (dbType.requiresPort())
        {
            connectionString
                    .append(":")
                    .append(config.getPort())
                    .append("/")
                    .append(config.getName());
        }
        else
        {
            connectionString
                    .append(config.getName());
        }

        // @TODO: set this as an option. this is presumed true if not specified.
        connectionString.append("?useSSL=false");

        return connectionString.toString();
    }

    public Session openSession()
    {
        return sessionFactory.openSession();
    }

    public boolean checkConnection()
    {

        List result = this.openSession().createSQLQuery("SELECT VERSION()").list();
        return !result.isEmpty();
    }

    public void saveConfiguration() {

        try(Session session = this.openSession()) {

            session.beginTransaction();
            session.update(JmeResourceWebsite.getInstance().getConfiguration().getDatabaseSavedConfiguration());
            session.getTransaction().commit();
            session.flush();
        }
    }

    public void saveAnnotatedObject(Object object) {

        try (Session session = openSession()) {

            session.beginTransaction();
            session.save(object);
            session.getTransaction().commit();
            session.flush();
        }
    }

    public void deleteAnnotatedObject(Object object) {

        try (Session session = openSession()) {

            session.beginTransaction();
            session.delete(object);
            session.getTransaction().commit();
            session.flush();
        }
    }

    public void updateAnnotatedObject(Object object) {

        try (Session session = openSession()) {

            session.beginTransaction();
            session.update(object);
            session.getTransaction().commit();
            session.flush();
        }
    }

    @Override
    public void close() throws IOException {
        if (this.sessionFactory != null) {
            sessionFactory.close();
        }
    }

}