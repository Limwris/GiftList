package com.nichesoftware.giftlist.session;

import android.support.annotation.Nullable;

import com.nichesoftware.giftlist.model.User;

/**
 * Provides and handles the token
 */
public interface TokenManager {
    /**
     * Returns the token if a {@link User} is connected
     * @return      {@link User} token if he is connected, null otherwise
     */
    @Nullable String getToken();

    /**
     * Indicates whether the token is set or not
     * @return      true if the token is set, false otherwise
     */
    boolean hasToken();
}
