package com.nichesoftware.giftlist.service;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.nichesoftware.giftlist.BuildConfig;
import com.nichesoftware.giftlist.Injection;
import com.nichesoftware.giftlist.contracts.GcmContract;
import com.nichesoftware.giftlist.presenters.GcmPresenter;

/**
 * Created by n_che on 27/06/2016.
 * According to this Google official documentation, the instance ID server issues callbacks periodically (i.e. 6 months) to request apps to refresh their tokens.
 */
public class FirebaseInstanceIDListenerService extends FirebaseInstanceIdService {
    private static final String TAG = FirebaseInstanceIDListenerService.class.getSimpleName();

    /**
     * Constructeur
     */
    public FirebaseInstanceIDListenerService() {
        super();
    }
    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. This call is initiated by the
     * InstanceID provider.
     */
    @Override
    public void onTokenRefresh() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onTokenRefresh");
        }

        // Make a call to Instance API
        FirebaseInstanceId instanceID = FirebaseInstanceId.getInstance();
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onTokenRefresh - Instance retreived...");
        }

        // Request token that will be used by the server to send push notifications
        final String gcmToken = instanceID.getToken();

        if (BuildConfig.DEBUG) {
            Log.d(TAG, String.format("onTokenRefresh - Token retreived: token = %s", gcmToken));
        }

        GcmContract.ActionListener actionListener = new GcmPresenter(Injection.getDataProvider(getApplicationContext()));
        actionListener.registerGcm(gcmToken);
    }
}