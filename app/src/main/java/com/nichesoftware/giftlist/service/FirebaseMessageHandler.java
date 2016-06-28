package com.nichesoftware.giftlist.service;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.nichesoftware.giftlist.BuildConfig;
import com.nichesoftware.giftlist.R;

import java.util.Map;

/**
 * Created by n_che on 27/06/2016.
 */
public class FirebaseMessageHandler extends FirebaseMessagingService {
    private static final String TAG = FirebaseMessageHandler.class.getSimpleName();
    public static final int MESSAGE_NOTIFICATION_ID = 435345;

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

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        createNotification(notification);
    }

    // Creates notification based on title and body received
    private void createNotification(RemoteMessage.Notification notification) {
        Context context = getBaseContext();
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher).setContentTitle(notification.getTitle())
                .setContentText(notification.getBody());
        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(MESSAGE_NOTIFICATION_ID, mBuilder.build());
    }
}
