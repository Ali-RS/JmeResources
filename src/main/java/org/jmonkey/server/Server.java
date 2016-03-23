package org.jmonkey.server;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.gzip.GzipHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.eclipse.jetty.servlets.DoSFilter;
import org.jmonkey.JmeResourceWebsite;
import org.jmonkey.configuration.ServerConfiguration;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * @author jayfella
 */

public class Server extends org.eclipse.jetty.server.Server {
    
    public Server() {
        super();
        
        ServerConfiguration config = JmeResourceWebsite.getInstance().getConfiguration().getServerConfig();

        DispatcherServlet apiServlet = new DispatcherServlet();
        apiServlet.setContextConfigLocation("classpath:spring-api.xml");
        apiServlet.setThrowExceptionIfNoHandlerFound(true);

        DispatcherServlet htmlServlet = new DispatcherServlet();
        htmlServlet.setContextConfigLocation("classpath:spring-html.xml");
        htmlServlet.setThrowExceptionIfNoHandlerFound(true);

        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        contextHandler.setContextPath(config.getBindPath());
        contextHandler.addServlet(new ServletHolder(apiServlet), "/api/*"); // api
        contextHandler.addServlet(new ServletHolder(htmlServlet), "/*"); // website
        contextHandler.addFilter(DoSFilter.class, "*", null);
        contextHandler.addFilter(CrossOriginFilter.class, "*", null);

        String[] mimetypes = {
                "text/html",
                "application/json",
                "text/plain",
                "text/css",
                "application/javascript"
        };

        GzipHandler gzipHandler = new GzipHandler();
        gzipHandler.setIncludedMethods("GET", "POST");
        gzipHandler.setIncludedMimeTypes(mimetypes);

        contextHandler.setGzipHandler(gzipHandler);

        this.setHandler(contextHandler);
        
        HttpConfiguration httpConfig = new HttpConfiguration();
        httpConfig.setSendServerVersion(false);
        httpConfig.setSendXPoweredBy(false);
        
        // @TODO: provide http2 support
        // @TODO: provide https support
        HttpConnectionFactory http1 = new HttpConnectionFactory(httpConfig);
        
        ServerConnector httpConnector = new ServerConnector(this, http1);
        httpConnector.setPort(config.getBindPort());
        this.setConnectors(new Connector[] { httpConnector });
    }
}
