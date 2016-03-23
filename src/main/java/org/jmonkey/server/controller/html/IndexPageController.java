package org.jmonkey.server.controller.html;

import org.jmonkey.JmeResourceWebsite;
import org.jmonkey.server.website.template.TemplateFileNotFoundException;
import org.jmonkey.server.website.template.TemplatedWebPage;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * @author jayfella
 */

@Controller
public class IndexPageController {
    
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String getIndex(HttpServletRequest request, HttpServletResponse response) throws IOException, TemplateFileNotFoundException {
        
        File htmlFile = Paths.get(JmeResourceWebsite.getInstance().getConfiguration().getWebsiteConfig().getHtmlPath().toString(), "index.html").toFile();
        
        if (!htmlFile.exists()) {
            throw new TemplateFileNotFoundException("file 'index.html' does not exist.");
        }

        return new TemplatedWebPage(htmlFile)
                .setPageTitle("Resources")
                .useSessionData(request.getCookies())
                .build();
    }
    
    @RequestMapping(value = "/view/{resource}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String displayResource(@PathVariable String resource, HttpServletRequest request, HttpServletResponse response) throws TemplateFileNotFoundException, IOException {
        
        File htmlFile = Paths.get(JmeResourceWebsite.getInstance().getConfiguration().getWebsiteConfig().getHtmlPath().toString(), "resource.html").toFile();
        
        if (!htmlFile.exists()) {
            throw new TemplateFileNotFoundException("file 'resource.html' does not exist.");
        }

        return new TemplatedWebPage(htmlFile)
                .setPageTitle(resource)
                .build();
    }
            
    
}
