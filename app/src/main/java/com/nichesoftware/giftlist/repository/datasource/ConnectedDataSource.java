package com.nichesoftware.giftlist.repository.datasource;

import com.nichesoftware.giftlist.model.User;
import com.nichesoftware.giftlist.service.Service;

/**
 * Instance of {@link CloudDataSource} for connected service
 */
public abstract class ConnectedDataSource<T> extends CloudDataSource<T> {

    /**
     * {@link User} token
     */
    protected final String mToken;

    /**
     * Public constructor
     *
     * @param token         {@link User} token
     * @param service       The bound {@link Service}
     */
    public ConnectedDataSource(String token, Service service) {
        super(service);
        mToken = token;
    }

    // region Getter
    /**
     * Getter for the {@link User} token
     *
     * @return  {@link User} token
     */
    public String getToken() {
        return mToken;
    }
    // endregion
}
