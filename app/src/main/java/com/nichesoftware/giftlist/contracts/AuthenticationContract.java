package com.nichesoftware.giftlist.contracts;

import com.nichesoftware.giftlist.presenters.IPresenter;
import com.nichesoftware.giftlist.views.IView;

/**
 * Authentication contract
 */
public interface AuthenticationContract {
    interface View extends IView {
        void displayAuthenticationLoader(boolean show);
        void onAuthenticationError();
        void onAuthenticationSucceeded();
        void goToLaunchScreen();
    }
    interface Presenter extends IPresenter {
        void onAuthentication(final String username, final String password);
        void cancelTask();
        void doDisconnect();
        boolean isConnected();
    }
}
