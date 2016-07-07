package com.nichesoftware.giftlist.presenters;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.gson.Gson;
import com.nichesoftware.giftlist.BuildConfig;
import com.nichesoftware.giftlist.R;
import com.nichesoftware.giftlist.android.NotificationId;
import com.nichesoftware.giftlist.contracts.GcmContract;
import com.nichesoftware.giftlist.dataproviders.DataProvider;
import com.nichesoftware.giftlist.dto.AcceptInvitationDto;
import com.nichesoftware.giftlist.dto.NotificationDto;
import com.nichesoftware.giftlist.views.inviteroom.InviteRoomActivity;


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

    @Override
    public void doProcessInvitationNotification(final Context context,
                                                final String notification, final String data) {
        Gson gson = new Gson();
        AcceptInvitationDto invitationDto = gson.fromJson(data, AcceptInvitationDto.class);
        NotificationDto notificationDto = gson.fromJson(notification, NotificationDto.class);

        Intent intent = new Intent(context, InviteRoomActivity.class);
        intent.putExtra(InviteRoomActivity.EXTRA_ROOM_ID, invitationDto.getRoomId());
        intent.putExtra(InviteRoomActivity.EXTRA_TOKEN, invitationDto.getToken());

        // Build Notification , setOngoing keeps the notification always in status bar
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(notificationDto.getTitle())
                        .setContentText(notificationDto.getBody())
                        .setOngoing(true);

        // Create pending intent
        // Mention the Activity which needs to be triggered when user clicks on notification
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationBuilder.setContentIntent(contentIntent);

        // Gets an instance of the NotificationManager service
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Builds the notification and issues it.
        notificationManager.notify(NotificationId.getID(), notificationBuilder.build());
    }
}
