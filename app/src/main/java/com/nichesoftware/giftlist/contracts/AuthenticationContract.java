package com.nichesoftware.giftlist.contracts;

import com.nichesoftware.giftlist.presenters.IPresenter;
import com.nichesoftware.giftlist.views.IView;

/**
 * Authentication contract
 */
public interface AuthenticationContract {
    interface View extends IView {
        void onAuthenticationError();
        void onAuthenticationSucceeded();
    }
    interface Presenter extends IPresenter {
        void onAuthentication(final String username, final String password);
        void doDisconnect();
        boolean isConnected();
    }
}
