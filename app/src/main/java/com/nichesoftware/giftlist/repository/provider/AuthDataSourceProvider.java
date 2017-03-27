package com.nichesoftware.giftlist.repository.provider;

import com.nichesoftware.giftlist.BaseApplication;
import com.nichesoftware.giftlist.model.User;
import com.nichesoftware.giftlist.repository.cache.Cache;
import com.nichesoftware.giftlist.repository.datasource.AuthDataSource;
import com.nichesoftware.giftlist.service.Service;
import com.nichesoftware.giftlist.session.SessionManager;
import com.nichesoftware.giftlist.utils.NetworkUtils;

import java.net.ConnectException;

import io.reactivex.Observable;

/**
 * Subset of the {@link com.nichesoftware.giftlist.repository.datasource.CloudDataSource} for authentication
 */
public class AuthDataSourceProvider implements AuthDataSource {
    /**
     * {@link Cache}
     */
    private final Cache<User> mCache;

    /**
     * REST API
     */
    private final Service mService;

    /**
     * Default constructor
     *
     * @param cache         {@link Cache}
     * @param service       The bound {@link Service}
     */
    public AuthDataSourceProvider(final Cache<User> cache, final Service service) {
        mService = service;
        mCache = cache;
    }

    @Override
    public Observable<User> authenticate(User user) {
        if (!NetworkUtils.isConnectionAvailable(BaseApplication.getAppContext())) {
            return Observable.error(new ConnectException());
        } else {
            // Cache is used for further database management and foreign key constraint
            return mService.authenticate(user).doOnNext(mCache::put).doOnNext(connectedUser ->
                    SessionManager.getInstance().setConnectedUser(connectedUser));
        }
    }

    @Override
    public Observable<User> register(User user) {
        if (!NetworkUtils.isConnectionAvailable(BaseApplication.getAppContext())) {
            return Observable.error(new ConnectException());
        } else {
            // Cache is used for further database management and foreign key constraint
            return mService.register(user).doOnNext(mCache::put).doOnNext(connectedUser ->
                    SessionManager.getInstance().setConnectedUser(connectedUser));
        }
    }

    @Override
    public Observable<Void> disconnect(User user) {
        throw new UnsupportedOperationException("This operation is not supported right now...");
    }
}
