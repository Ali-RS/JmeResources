package org.jmonkey.server;

import org.jmonkey.JmeResourceWebsite;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.Paths;

/**
 * @author jayfella
 */

public class CustomResourceHandler extends ResourceHttpRequestHandler {

    @Override
    protected Resource getResource(HttpServletRequest request) {

        String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        String requestedFile = path.replace("/theme/", "");

        String resource = Paths.get(
                JmeResourceWebsite.getInstance().getConfiguration().getWebsiteConfig().getActiveThemePath().toAbsolutePath().toString(),
                "resources",
                requestedFile)
                .toString();

        return new FileSystemResource(resource);
    }

}
