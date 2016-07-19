package com.nichesoftware.giftlist.presenters;

import android.support.annotation.NonNull;
import android.util.Log;

import com.nichesoftware.giftlist.BuildConfig;
import com.nichesoftware.giftlist.contracts.AuthenticationContract;
import com.nichesoftware.giftlist.dataproviders.DataProvider;
import com.nichesoftware.giftlist.utils.StringUtils;

/**
 * Created by Kattleya on 07/07/2016.
 */
public class AuthenticationPresenter extends AbstractPresenter implements AuthenticationContract.UserActionListener {
    private static final String TAG = AuthenticationPresenter.class.getSimpleName();

    /**
     * View
     */
    private AuthenticationContract.View view;

    /**
     * Constructeur
     * @param view
     * @param dataProvider
     */
    public AuthenticationPresenter(@NonNull AuthenticationContract.View view,
                                   @NonNull DataProvider dataProvider) {
        this.dataProvider = dataProvider;
        this.view = view;
    }

    @Override
    public void authenticate(final String username, final String password, final AuthenticationContract.OnAuthenticationCallback callback) {

        if (!StringUtils.isEmpty(username) && !StringUtils.isEmpty(password)) {
            view.showLoader();

            dataProvider.logIn(username, password, new DataProvider.Callback() {
                @Override
                public void onSuccess() {
                    if (BuildConfig.DEBUG) {
                        Log.d(TAG, "authenticate - connected - onSuccess");
                    }
                    view.hideLoader();
                    if (callback != null) {
                        callback.onSuccess();
                    }
                    view.dismiss();
                }

                @Override
                public void onError() {
                    if (BuildConfig.DEBUG) {
                        Log.d(TAG, "authenticate - connected - onError");
                    }
                    view.hideLoader();
                    if (callback != null) {
                        callback.onError();
                    }
                    view.dismiss();
                }
            });
        } else {
            if (callback != null) {
                callback.onError();
            }
        }
    }
}
