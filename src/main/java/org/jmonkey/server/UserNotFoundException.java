package org.jmonkey.server;

import org.jmonkey.database.user.User;

/**
 * @author jayfella
 */

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
        super("The specified user does not exist");
    }

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(Throwable cause) {
        super(cause);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
