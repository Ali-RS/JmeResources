package org.jmonkey.server.website.template;

/**
 *
 * @author jayfella
 */
public class TemplateFileNotFoundException extends Exception {
    
    public TemplateFileNotFoundException() {
        super();
    }
    
    public TemplateFileNotFoundException(String message) {
        super(message);
    }
    
    public TemplateFileNotFoundException(Throwable cause) {
        super(cause);
    }
    
    public TemplateFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
