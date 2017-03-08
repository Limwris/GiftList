package com.nichesoftware.giftlist.contracts;

/**
 * Splash screen contract
 */
public interface SplashScreenContract {
    interface View extends AuthenticationContract.View {
        void doShowDisconnectedActivity();
        void doShowConnectedActivity();
    }
    interface Presenter extends AuthenticationContract.Presenter {
        void doRGSplashScreen();
    }
}
