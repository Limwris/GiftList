package com.nichesoftware.giftlist.presenters;

import android.support.annotation.NonNull;
import android.util.Log;

import com.nichesoftware.giftlist.BuildConfig;
import com.nichesoftware.giftlist.contracts.SplashScreenContract;
import com.nichesoftware.giftlist.dataproviders.DataProvider;

/**
 * Created by Kattleya on 18/07/2016.
 */
public class SplashScreenPresenter extends AbstractPresenter implements SplashScreenContract.UserActionListener {
    private static final String TAG = LaunchScreenPresenter.class.getSimpleName();

    /**
     * View
     */
    private SplashScreenContract.View view;

    /**
     * Constructeur
     * @param view
     * @param dataProvider
     */
    public SplashScreenPresenter(@NonNull SplashScreenContract.View view, @NonNull DataProvider dataProvider) {
        this.dataProvider = dataProvider;
        this.view = view;
    }

    @Override
    public void doRGSplashScreen() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "doRGSplashScreen");
        }
        if (dataProvider.isDisconnectedUser()) {
            view.doShowDisconnectedActivity();
        } else {
            view.doShowConnectedActivity();
        }
    }
}
