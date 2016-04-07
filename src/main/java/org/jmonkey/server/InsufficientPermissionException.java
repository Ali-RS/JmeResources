package org.jmonkey.server;

/**
 * @author jayfella
 */

public class InsufficientPermissionException extends RuntimeException {

    public InsufficientPermissionException() {
        super("You do not have the required permission to view this resource.");
    }

    public InsufficientPermissionException(String message) {
        super(message);
    }

    public InsufficientPermissionException(Throwable cause) {
        super(cause);
    }

    public InsufficientPermissionException(String message, Throwable cause) {
        super(message, cause);
    }
}
