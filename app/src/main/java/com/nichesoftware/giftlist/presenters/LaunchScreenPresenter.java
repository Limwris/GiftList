package com.nichesoftware.giftlist.presenters;

import android.support.annotation.NonNull;
import android.util.Log;

import com.nichesoftware.giftlist.BuildConfig;
import com.nichesoftware.giftlist.contracts.LaunchScreenContract;
import com.nichesoftware.giftlist.dataproviders.DataProvider;

/**
 * Created by n_che on 08/06/2016.
 */
public class LaunchScreenPresenter extends AbstractPresenter implements LaunchScreenContract.UserActionListener {
    private static final String TAG = LaunchScreenPresenter.class.getSimpleName();

    /**
     * View
     */
    private LaunchScreenContract.View view;

    /**
     * Constructeur
     * @param view
     * @param dataProvider
     */
    public LaunchScreenPresenter(@NonNull LaunchScreenContract.View view, @NonNull DataProvider dataProvider) {
        this.dataProvider = dataProvider;
        this.view = view;
    }

    @Override
    public void startApplication() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "startApplication");
        }
        view.showRoomsActivity();

    }

    @Override
    public void register(final String username, final String password) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "register");
        }
        view.showLoader();

        dataProvider.register(username, password, new DataProvider.Callback() {
            @Override
            public void onSuccess() {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "register - onSuccess");
                }
                view.hideLoader();
                view.showRoomsActivity();
            }

            @Override
            public void onError() {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "register - onError");
                }
                view.hideLoader();
            }
        });
    }
}
