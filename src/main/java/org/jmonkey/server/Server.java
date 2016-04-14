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
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.jar.Manifest;
import org.eclipse.jetty.webapp.WebAppClassLoader;

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
        this.setClassLoader(contextHandler);
        
        contextHandler.setAttribute("javax.servlet.context.tempdir", this.getScratchDir());
        contextHandler.setAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern", ".*/[^/]*servlet-api-[^/]*\\.jar$|.*/javax.servlet.jsp.jstl-.*\\.jar$|.*/.*taglibs.*\\.jar$");
        contextHandler.setAttribute("org.eclipse.jetty.containerInitializers", jspInitializers());
        contextHandler.setAttribute(InstanceManager.class.getName(), new SimpleInstanceManager());
        contextHandler.addBean(new ServletContainerInitializersStarter(contextHandler), true);

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

    // @see http://stackoverflow.com/a/29037344
    private void setClassLoader(WebAppContext context) throws MalformedURLException, IOException {
        
        //========== create WebAppClassloader with a new parent classloader with all URLs in manifests=================== 
        //search for any secondary class path entries
        Vector<URL> secondayClassPathURLVector = new Vector<>();
        ClassLoader parentClassLoader = this.getClass().getClassLoader();
        if(parentClassLoader instanceof URLClassLoader)
        {
            URL[] existingURLs = ((URLClassLoader)parentClassLoader).getURLs(); 
            for (URL parentURL : existingURLs)
            {
                //if it doesn't end in .jar, then it's probably not going to have Manifest with a Class-Path entry 
                if(parentURL.toString().endsWith(".jar"))
                {
                    JarURLConnection jarURLConnection = (JarURLConnection) new URL("jar:" + parentURL.toString() + "!/").openConnection();
                    Manifest jarManifest = jarURLConnection.getManifest();
                    String classPath = jarManifest.getMainAttributes().getValue("Class-Path");
                    if(classPath != null)
                    {
                        //Iterate through all of the class path entries and create URLs out of them                                                
                        for (String part : classPath.split(" "))
                        {
                            //add our full path to the jar file to our classpath list
                            secondayClassPathURLVector.add(new URL(parentURL,part));                     
                        }
                    }
                }                
            }
        }
        //use our class path entries to create a parent for the webappclassloader that knows how to use or referenced class paths
        URLClassLoader internalClassPathUrlClassLoader = new URLClassLoader(secondayClassPathURLVector.toArray(new URL[secondayClassPathURLVector.size()]), parentClassLoader);
        //create a new webapp class loader as a child of our  internalClassPathUrlClassLoader. For whatever reason Jetty needs to have a WebAppClassLoader be it's main class loader,
        //while all of our classpath entries need to be added to the parent class loader   
        WebAppClassLoader webAppClassLoader = new WebAppClassLoader(internalClassPathUrlClassLoader,context);
        context.setClassLoader(webAppClassLoader);
    }
    
}
