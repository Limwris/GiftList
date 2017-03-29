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
import com.nichesoftware.giftlist.contracts.GcmContract;
import com.nichesoftware.giftlist.dto.NotificationDto;
import com.nichesoftware.giftlist.gcm.NotificationId;
import com.nichesoftware.giftlist.model.Invitation;
import com.nichesoftware.giftlist.model.User;
import com.nichesoftware.giftlist.service.Service;
import com.nichesoftware.giftlist.session.SessionManager;
import com.nichesoftware.giftlist.views.inviteroom.InviteRoomActivity;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * GCM presenter
 */
public class GcmPresenter implements GcmContract.Presenter {
    // Constants   ---------------------------------------------------------------------------------
    private static final String TAG = GcmPresenter.class.getSimpleName();

    // Fields   ------------------------------------------------------------------------------------
    /**
     * Service
     */
    private Service mService;

    /**
     * RX subscription
     */
    private Disposable mRegisterGcmSubscription;

    /**
     * Constructor
     *
     * @param service
     */
    public GcmPresenter(Service service) {
        this.mService = service;
    }

    /**
     * Registering with GCM and obtaining the gcm registration id
     */
    @Override
    public void registerGcm(final String gcmToken) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "registerGcm");
        }
        if( mRegisterGcmSubscription != null && !mRegisterGcmSubscription.isDisposed()) {
            mRegisterGcmSubscription.dispose();
        }
        mRegisterGcmSubscription = mService.registerDevice(gcmToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        .subscribe(aBoolean -> {
            // TODO: 28/03/2017
        });
    }

    @Override
    public void doProcessInvitationNotification(final Context context,
                                                final String notification, final String data) {
        Gson gson = new Gson();
        Invitation invitation = gson.fromJson(data, Invitation.class);
        NotificationDto notificationDto = gson.fromJson(notification, NotificationDto.class);

        Intent intent = new Intent(context, InviteRoomActivity.class);
        intent.putExtra(InviteRoomActivity.EXTRA_ROOM, invitation.getRoom());

        // Si l'utilisateur courant est bien celui qui est invité
        User user = SessionManager.getInstance().getConnectedUser();
        if (user != null && user.getUsername().equals(invitation.getInvitedUser().getUsername())) {
            // Build Notification
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle(notificationDto.getTitle())
                            .setContentText(notificationDto.getBody())
                            .setAutoCancel(true);

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
}
