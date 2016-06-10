package com.nichesoftware.giftlist.presenters;

import android.support.annotation.NonNull;
import android.util.Log;

import com.nichesoftware.giftlist.BuildConfig;
import com.nichesoftware.giftlist.contracts.StartContract;
import com.nichesoftware.giftlist.dataproviders.DataProvider;

/**
 * Created by n_che on 08/06/2016.
 */
public class StartPresenter implements StartContract.UserActionListener {
    private static final String TAG = StartPresenter.class.getSimpleName();

    /**
     * Data provider
     */
    private DataProvider dataProvider;

    /**
     * View
     */
    private StartContract.View view;

    /**
     * Constructeur
     * @param view
     * @param dataProvider
     */
    public StartPresenter(@NonNull StartContract.View view, @NonNull DataProvider dataProvider) {
        this.dataProvider = dataProvider;
        this.view = view;
    }

    @Override
    public void startApplication() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "startApplication");
        }
        view.showLoader("Log in...");

        dataProvider.logIn("ap", "pass", new DataProvider.Callback() {
            @Override
            public void onSuccess() {
                view.hideLoader();
                view.showRoomsActivity();
            }

            @Override
            public void onError() {
                view.hideLoader();
            }
        });
    }
}
