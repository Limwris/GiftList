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
    public void startApplication(final String username, final String password) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "startApplication");
        }
        view.showLoader("Log in...");

        if (StringUtils.isEmpty(username) && StringUtils.isEmpty(password)) {
            dataProvider.logInDisconnected(new DataProvider.Callback() {
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
        } else {
            dataProvider.logIn(username, password, new DataProvider.Callback() {
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

    @Override
    public void register(final String username, final String password) {
        dataProvider.register(username, password, new DataProvider.Callback() {
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
