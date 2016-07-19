package com.nichesoftware.giftlist.contracts;

/**
 * Created by Kattleya on 18/07/2016.
 */
public interface SplashScreenContract {
    interface View extends AbstractContract.View {
        void doShowDisconnectedActivity();
        void doShowConnectedActivity();
    }
    interface UserActionListener extends AbstractContract.UserActionListener {
        void doRGSplashScreen();
    }
}
