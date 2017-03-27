package com.nichesoftware.giftlist.presenters;

import android.support.annotation.NonNull;
import android.util.Log;

import com.nichesoftware.giftlist.contracts.SplashScreenContract;
import com.nichesoftware.giftlist.session.SessionManager;

/**
 * Splash screen presenter
 */
public class SplashScreenPresenter extends BaseBarePresenter<SplashScreenContract.View>
        implements SplashScreenContract.Presenter {
    // Constants   ---------------------------------------------------------------------------------
    private static final String TAG = LaunchScreenPresenter.class.getSimpleName();

    /**
     * Default constructor
     *
     * @param view View to attach
     */
    public SplashScreenPresenter(@NonNull SplashScreenContract.View view) {
        super(view);
    }

    @Override
    public void doRGSplashScreen() {
        Log.d(TAG, "doRGSplashScreen");
        if (SessionManager.getInstance().isConnected()) {
            mAttachedView.doShowConnectedActivity();
        } else {
            mAttachedView.doShowDisconnectedActivity();
        }
    }
}
