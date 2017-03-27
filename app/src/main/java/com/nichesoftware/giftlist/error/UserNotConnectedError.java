package com.nichesoftware.giftlist.error;

/**
 * {@link Error} thrown when the {@link com.nichesoftware.giftlist.model.User} is not connected
 */
public class UserNotConnectedError extends Error {

    /**
     * Default constructor
     */
    public UserNotConnectedError() {
        super();
    }

    /**
     * Constructor with attached message
     *
     * @param message       Additional message to attach to the error
     */
    public UserNotConnectedError(final String message) {
        super(message);
    }
}
