package com.nichesoftware.giftlist.presenters;

import android.support.annotation.NonNull;
import android.util.Log;

import com.nichesoftware.giftlist.BuildConfig;
import com.nichesoftware.giftlist.contracts.StartContract;
import com.nichesoftware.giftlist.dataproviders.DataProvider;
import com.nichesoftware.giftlist.utils.StringUtils;

/**
 * Created by n_che on 08/06/2016.
 */
public class StartPresenter extends AbstractPresenter implements StartContract.UserActionListener {
    private static final String TAG = StartPresenter.class.getSimpleName();

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
