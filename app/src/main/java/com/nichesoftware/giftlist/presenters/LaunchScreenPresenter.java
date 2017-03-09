package com.nichesoftware.giftlist.presenters;

import android.support.annotation.NonNull;
import android.util.Log;

import com.nichesoftware.giftlist.BuildConfig;
import com.nichesoftware.giftlist.contracts.LaunchScreenContract;
import com.nichesoftware.giftlist.dataproviders.DataProvider;

/**
 * Launch screen presenter
 */
public class LaunchScreenPresenter extends AbstractPresenter<LaunchScreenContract.View>
        implements LaunchScreenContract.Presenter {
    // Constants   ---------------------------------------------------------------------------------
    private static final String TAG = LaunchScreenPresenter.class.getSimpleName();

    /**
     * Constructor
     *
     * @param view         View to attach
     * @param dataProvider The data provider
     */
    public LaunchScreenPresenter(@NonNull LaunchScreenContract.View view, @NonNull DataProvider dataProvider) {
        super(view, dataProvider);
    }


    @Override
    public void startApplication() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "startApplication");
        }
        mAttachedView.showRoomsActivity();

    }

    @Override
    public void register(final String username, final String password) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "register");
        }
        mAttachedView.showLoader();

        mDataProvider.register(username, password, new DataProvider.Callback() {
            @Override
            public void onSuccess() {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "register - onSuccess");
                }
                mAttachedView.hideLoader();
                mAttachedView.showRoomsActivity();
            }

            @Override
            public void onError() {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "register - onError");
                }
                mAttachedView.hideLoader();
            }
        });
    }
}
