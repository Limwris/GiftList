package com.nichesoftware.giftlist.presenters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.nichesoftware.giftlist.BuildConfig;
import com.nichesoftware.giftlist.contracts.GcmContract;
import com.nichesoftware.giftlist.dataproviders.DataProvider;
import com.nichesoftware.giftlist.dto.AcceptInvitationDto;
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
    public void doProcessInvitationNotification(final Context context, String data) {
        Gson gson = new Gson();
        // Todo: Revoir gestion
        AcceptInvitationDto dto = gson.fromJson(data, AcceptInvitationDto.class);
        Intent intent = new Intent(context, InviteRoomActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(InviteRoomActivity.EXTRA_ROOM_ID, dto.getRoomId());
        intent.putExtra(InviteRoomActivity.EXTRA_TOKEN, dto.getToken());
        context.startActivity(intent);
    }
}
