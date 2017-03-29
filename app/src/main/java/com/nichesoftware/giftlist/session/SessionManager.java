package com.nichesoftware.giftlist.session;

import android.support.annotation.Nullable;

import com.nichesoftware.giftlist.model.User;
import com.nichesoftware.giftlist.utils.StringUtils;

/**
 * Session manager
 */
public final class SessionManager implements TokenManager {

    // Connected user
    private User mConnectedUser;

    /**
     * Constructeur privé
     */
    private SessionManager() {
        // Nothing
    }

    /**
     * Holder
     * @see <a href="http://thecodersbreakfast.net/index.php?post/2008/02/25/26-de-la-bonne-implementation-du-singleton-en-java">Singleton implementation</a> on <em>on The Coders Breakfast</em> (FR)
     */
    private static class SessionHolder {
        private SessionHolder() {
            // Nothing
        }

        /**
         * Instance unique non préinitialisée
         */
        private final static SessionManager instance = new SessionManager();
    }

    /**
     * Point d'accès pour l'instance unique du singleton
     */
    public static SessionManager getInstance() {
        return SessionHolder.instance;
    }

    // region Getter & setters
    public @Nullable User getConnectedUser() {
        return mConnectedUser;
    }

    public void setConnectedUser(User connectedUser) {
        this.mConnectedUser = connectedUser;
    }
    // endregion

    // region TokenManager methods
    @Override
    public @Nullable String getToken() {
        return mConnectedUser == null ? StringUtils.EMPTY_STRING : mConnectedUser.getToken();
    }

    @Override
    public boolean hasToken() {
        return (mConnectedUser != null && StringUtils.isEmpty(mConnectedUser.getToken()));
    }
    // enregion

    // region Public utility methods
    /**
     * Indicates whether a {@link User} is connected or not
     * @return      true if a {@link User} is connected, false otherwise
     */
    public boolean isConnected() {
        return (mConnectedUser != null);
    }
    // endregion
}
