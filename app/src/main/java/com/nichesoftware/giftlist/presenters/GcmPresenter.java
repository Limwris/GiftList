package com.nichesoftware.giftlist.presenters;

import android.content.Context;
import android.util.Log;

import com.nichesoftware.giftlist.BuildConfig;
import com.nichesoftware.giftlist.contracts.GcmContract;
import com.nichesoftware.giftlist.dataproviders.DataProvider;

/**
 * Created by n_che on 27/06/2016.
 */
public class GcmPresenter implements GcmContract.ActionListener {
    private static final String TAG = GcmPresenter.class.getSimpleName();

    /**
     * Android context
     */
    private Context context;

    /**
     * Data provider
     */
    private DataProvider provider;

    /**
     * Constructor
     * @param context
     * @param provider
     */
    public GcmPresenter(Context context, DataProvider provider) {
        this.context = context;
        this.provider = provider;
    }

    /**
     * Registering with GCM and obtaining the gcm registration id
     */
    @Override
    public void registerGcm(final GcmContract.ActionListener.RegistrationCompleteCallback callback) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "registerGcm");
        }
        provider.registerGcm(new DataProvider.OnRegistrationCompleted() {
            @Override
            public void onSuccess(final String token) {
                if (callback != null) {
                    callback.onRegistrationCompleted(token);
                }
            }

            @Override
            public void onError() {

            }
        });

    }
}
