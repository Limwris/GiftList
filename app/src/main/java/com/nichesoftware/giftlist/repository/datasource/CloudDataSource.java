package com.nichesoftware.giftlist.repository.datasource;

import com.nichesoftware.giftlist.service.Service;

/**
 * {@link DataSource} implementation based on connections to the REST api (Cloud).
 */
public abstract class CloudDataSource<T> implements DataSource<T> {

    /**
     * REST API
     */
    protected final Service mService;

    /**
     * Public constructor
     *
     * @param service       The bound {@link Service}
     */
    public CloudDataSource(final Service service) {
        mService = service;
    }

    // region Getters
    /**
     * Getter for the {@link Service}
     *
     * @return The bound {@link Service}
     */
    protected Service getService() {
        return mService;
    }
    // endregion
}
