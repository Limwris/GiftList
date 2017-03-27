package com.nichesoftware.giftlist.repository.datasource;

import com.nichesoftware.giftlist.model.User;

import io.reactivex.Observable;

/**
 * Interface that represents a data source for authentication
 */
public interface AuthDataSource {
    /**
     * Get an {@link Observable} which will emit the {@link User} after it was authenticated.
     *
     * @param user   {@link User} to authenticate
     * @return       The registered {@link User} with a valid token
     */
    Observable<User> authenticate(User user);

    /**
     * Get an {@link Observable} which will emit the {@link User} after it was registered.
     *
     * @param user   {@link User} to register
     * @return       The registered {@link User} with a valid token
     */
    Observable<User> register(User user);

    /**
     * Disconnect the user.
     *
     * @param user   {@link User} to disconnect
     */
    Observable<Void> disconnect(User user);
}
