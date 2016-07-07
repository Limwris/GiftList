package com.nichesoftware.giftlist.service;

import android.content.Context;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.nichesoftware.giftlist.BuildConfig;
import com.nichesoftware.giftlist.Injection;
import com.nichesoftware.giftlist.contracts.GcmContract;
import com.nichesoftware.giftlist.presenters.GcmPresenter;

import java.util.Map;

/**
 * Created by n_che on 27/06/2016.
 */
public class FirebaseMessageHandler extends FirebaseMessagingService {
    private static final String TAG = FirebaseMessageHandler.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onMessageReceived");
        }
        Map<String, String> data = remoteMessage.getData();
        String from = remoteMessage.getFrom();
        if (BuildConfig.DEBUG) {
            Log.d(TAG, String.format("onMessageReceived - From %s, data %s", from, data));
        }

        Context context = getBaseContext();
        GcmContract.ActionListener actionListener
                = new GcmPresenter(Injection.getDataProvider(context));
        actionListener.doProcessInvitationNotification(context, data.get("notification"), data.get("data"));
    }
}
