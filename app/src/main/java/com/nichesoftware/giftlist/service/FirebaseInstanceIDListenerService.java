package com.nichesoftware.giftlist.service;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.nichesoftware.giftlist.BuildConfig;
import com.nichesoftware.giftlist.Injection;
import com.nichesoftware.giftlist.presenters.GcmPresenter;

/**
 * Firebase service listener
 * According to this Google official documentation, the instance ID server issues callbacks periodically (i.e. 6 months) to request apps to refresh their tokens.
 */
public class FirebaseInstanceIDListenerService extends FirebaseInstanceIdService {
    // Constants   ---------------------------------------------------------------------------------
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
        Log.d(TAG, "onTokenRefresh - Instance retreived...");


        // Request token that will be used by the server to send push notifications
        final String gcmToken = instanceID.getToken();

        Log.d(TAG, String.format("onTokenRefresh - Token retreived: token = %s", gcmToken));


        GcmPresenter presenter = new GcmPresenter(Injection.getDataProvider());
        presenter.registerGcm(gcmToken);
    }
}