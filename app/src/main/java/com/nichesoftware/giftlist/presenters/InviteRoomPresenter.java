package com.nichesoftware.giftlist.presenters;

import android.support.annotation.NonNull;

import com.nichesoftware.giftlist.contracts.InviteRoomContract;
import com.nichesoftware.giftlist.dataproviders.DataProvider;

/**
 * Created by n_che on 27/06/2016.
 */
public class InviteRoomPresenter implements InviteRoomContract.UserActionListener {
    private static final String TAG = InviteRoomPresenter.class.getSimpleName();

    /**
     * Data provider
     */
    private DataProvider dataProvider;

    /**
     * View
     */
    private InviteRoomContract.View view;

    /**
     * Constructeur
     * @param view
     * @param dataProvider
     */
    public InviteRoomPresenter(@NonNull InviteRoomContract.View view, @NonNull DataProvider dataProvider) {
        this.dataProvider = dataProvider;
        this.view = view;
    }

    @Override
    public void acceptInvitationToRoom(int roomId) {
        dataProvider.acceptInvitationToRoom(roomId, new DataProvider.Callback() {
            @Override
            public void onSuccess() {
                view.onAcceptInvitationSuccess();
            }

            @Override
            public void onError() {
                view.onAcceptInvitationFailed();
            }
        });
    }
}
