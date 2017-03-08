package com.nichesoftware.giftlist.presenters;

import android.support.annotation.NonNull;
import android.util.Log;

import com.nichesoftware.giftlist.contracts.SplashScreenContract;
import com.nichesoftware.giftlist.dataproviders.DataProvider;

/**
 * Splash screen presenter
 */
public class SplashScreenPresenter extends AuthenticationPresenter<SplashScreenContract.View> implements SplashScreenContract.Presenter {
    private static final String TAG = LaunchScreenPresenter.class.getSimpleName();

    /**
     * Constructor
     *
     * @param view         View to attach
     * @param dataProvider The data provider
     */
    public SplashScreenPresenter(@NonNull SplashScreenContract.View view, @NonNull DataProvider dataProvider) {
        super(view, dataProvider);
    }

    @Override
    public void doRGSplashScreen() {
        Log.d(TAG, "doRGSplashScreen");
        if (mDataProvider.isDisconnectedUser()) {
            mAttachedView.doShowDisconnectedActivity();
        } else {
            mAttachedView.doShowConnectedActivity();
        }
    }
}
