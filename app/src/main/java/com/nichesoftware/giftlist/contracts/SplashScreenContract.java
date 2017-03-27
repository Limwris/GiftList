package com.nichesoftware.giftlist.contracts;

import com.nichesoftware.giftlist.presenters.IPresenter;
import com.nichesoftware.giftlist.views.IView;

/**
 * Splash screen contract
 */
public interface SplashScreenContract {
    interface View extends IView {
        void doShowDisconnectedActivity();
        void doShowConnectedActivity();
    }
    interface Presenter extends IPresenter {
        void doRGSplashScreen();
    }
}
