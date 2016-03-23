/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jmonkey.server;

/**
 *
 * @author jayfella
 */

public class ResourceNotFoundException extends RuntimeException {
    
    public ResourceNotFoundException() {
        super();
    }
    
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
    public ResourceNotFoundException(Throwable cause) {
        super(cause);
    }
    
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
