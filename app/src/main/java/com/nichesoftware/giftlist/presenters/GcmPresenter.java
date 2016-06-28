package com.nichesoftware.giftlist.presenters;

import android.util.Log;

import com.nichesoftware.giftlist.BuildConfig;
import com.nichesoftware.giftlist.contracts.GcmContract;
import com.nichesoftware.giftlist.dataproviders.DataProvider;

/**
 * Created by n_che on 27/06/2016.
 */
public class GcmPresenter extends AbstractPresenter implements GcmContract.ActionListener {
    private static final String TAG = GcmPresenter.class.getSimpleName();

    /**
     * Constructor
     * @param provider
     */
    public GcmPresenter(DataProvider provider) {
        this.dataProvider = provider;
    }

    /**
     * Registering with GCM and obtaining the gcm registration id
     */
    @Override
    public void registerGcm(final String gcmToken) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "registerGcm");
        }
        dataProvider.registerGcm(gcmToken);

    }
}
