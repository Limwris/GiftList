package com.nichesoftware.giftlist.service;

import android.util.Log;

import com.google.android.gms.iid.InstanceIDListenerService;
import com.nichesoftware.giftlist.BuildConfig;
import com.nichesoftware.giftlist.Injection;
import com.nichesoftware.giftlist.contracts.GcmContract;
import com.nichesoftware.giftlist.presenters.GcmPresenter;

/**
 * Created by n_che on 27/06/2016.
 * According to this Google official documentation, the instance ID server issues callbacks periodically (i.e. 6 months) to request apps to refresh their tokens.
 */
public class FirebaseInstanceIDListenerService extends InstanceIDListenerService {
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
        GcmContract.ActionListener actionListener = new GcmPresenter(getApplicationContext(), Injection.getDataProvider(getApplicationContext()));
        actionListener.registerGcm(new GcmContract.ActionListener.RegistrationCompleteCallback() {
            @Override
            public void onRegistrationCompleted(String token) {
                if (BuildConfig.DEBUG) {
                    if (token != null) {
                        Log.d(TAG, "Registrated token: " + token);
                    }
                }
            }
        });
    }
}