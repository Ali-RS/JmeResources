package org.jmonkey.server;

import org.apache.tomcat.InstanceManager;
import org.apache.tomcat.SimpleInstanceManager;
import org.eclipse.jetty.annotations.ServletContainerInitializersStarter;
import org.eclipse.jetty.apache.jsp.JettyJasperInitializer;
import org.eclipse.jetty.plus.annotation.ContainerInitializer;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.gzip.GzipHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.eclipse.jetty.servlets.DoSFilter;
import org.eclipse.jetty.webapp.WebAppContext;
import org.jmonkey.JmeResourceWebsite;
import org.jmonkey.configuration.ServerConfiguration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.DispatcherServlet;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jayfella
 */

public class Server extends org.eclipse.jetty.server.Server {
    
    public Server() throws IOException {
        super();
        
        ServerConfiguration config = JmeResourceWebsite.getInstance().getConfiguration().getServerConfig();
        System.setProperty("org.apache.jasper.compiler.disablejsr199", "false");

        DispatcherServlet apiServlet = new DispatcherServlet();
        apiServlet.setContextConfigLocation("classpath:spring-api.xml");
        apiServlet.setThrowExceptionIfNoHandlerFound(true);

        DispatcherServlet htmlServlet = new DispatcherServlet();
        htmlServlet.setContextConfigLocation("classpath:spring-html.xml");
        htmlServlet.setThrowExceptionIfNoHandlerFound(true);

        WebAppContext contextHandler = new WebAppContext();
        contextHandler.setAttribute("javax.servlet.context.tempdir", this.getScratchDir());
        contextHandler.setAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern", ".*/[^/]*servlet-api-[^/]*\\.jar$|.*/javax.servlet.jsp.jstl-.*\\.jar$|.*/.*taglibs.*\\.jar$");
        contextHandler.setAttribute("org.eclipse.jetty.containerInitializers", jspInitializers());
        contextHandler.setAttribute(InstanceManager.class.getName(), new SimpleInstanceManager());
        contextHandler.addBean(new ServletContainerInitializersStarter(contextHandler), true);
        contextHandler.setClassLoader(getUrlClassLoader());

        contextHandler.setContextPath(config.getBindPath().isEmpty() ? "/" : config.getBindPath());
        contextHandler.setResourceBase(new ClassPathResource("webapp").getURI().toString());
        contextHandler.addServlet(new ServletHolder(apiServlet), "/api/*"); // api
        contextHandler.addServlet(new ServletHolder(htmlServlet), "/"); // website
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
        
        // @TODO: provide https + http2 support
        HttpConnectionFactory http1 = new HttpConnectionFactory(httpConfig);

        ServerConnector httpConnector = new ServerConnector(this, http1);
        httpConnector.setPort(config.getBindPort());
        this.setConnectors(new Connector[] { httpConnector });
    }

    private ClassLoader getUrlClassLoader()
    {
        ClassLoader jspClassLoader = new URLClassLoader(new URL[0], this.getClass().getClassLoader());
        return jspClassLoader;
    }

    private List<ContainerInitializer> jspInitializers()
    {
        JettyJasperInitializer sci = new JettyJasperInitializer();
        ContainerInitializer initializer = new ContainerInitializer(sci, null);
        List<ContainerInitializer> initializers = new ArrayList<>();
        initializers.add(initializer);
        return initializers;
    }

    private File getScratchDir() throws IOException
    {
        File tempDir = new File(System.getProperty("java.io.tmpdir"));
        File scratchDir = new File(tempDir.toString(), "jme-store");

        if (!scratchDir.exists())
        {
            if (!scratchDir.mkdirs())
            {
                throw new IOException("Unable to create scratch directory: " + scratchDir);
            }
        }
        return scratchDir;
    }

}
