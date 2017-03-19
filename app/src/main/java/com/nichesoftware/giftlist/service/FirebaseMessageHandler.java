package com.nichesoftware.giftlist.service;

import android.content.Context;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.nichesoftware.giftlist.Injection;
import com.nichesoftware.giftlist.presenters.GcmPresenter;

import java.util.Map;

/**
 * Firebase message handler
 */
public class FirebaseMessageHandler extends FirebaseMessagingService {
    // Constants   ---------------------------------------------------------------------------------
    private static final String TAG = FirebaseMessageHandler.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "onMessageReceived");

        Map<String, String> data = remoteMessage.getData();
        String from = remoteMessage.getFrom();
        Log.d(TAG, String.format("onMessageReceived - From %s, data %s", from, data));


        Context context = getBaseContext();
        GcmPresenter presenter = new GcmPresenter(Injection.getDataProvider());
        presenter.doProcessInvitationNotification(context, data.get("notification"), data.get("data"));
    }
}
