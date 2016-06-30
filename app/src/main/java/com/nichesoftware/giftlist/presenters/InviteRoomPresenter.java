package com.nichesoftware.giftlist.presenters;

import android.support.annotation.NonNull;

import com.nichesoftware.giftlist.contracts.InviteRoomContract;
import com.nichesoftware.giftlist.dataproviders.DataProvider;
import com.nichesoftware.giftlist.model.Room;

/**
 * Created by n_che on 27/06/2016.
 */
public class InviteRoomPresenter extends AbstractPresenter implements InviteRoomContract.UserActionListener {
    private static final String TAG = InviteRoomPresenter.class.getSimpleName();

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
    public void acceptInvitationToRoom(int roomId, final String invitationToken) {
        dataProvider.acceptInvitationToRoom(roomId, invitationToken, new DataProvider.Callback() {
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

    @Override
    public void getRoomInformation(int roomId) {
        dataProvider.getRoomInformation(roomId, new DataProvider.CallbackValue<Room>() {
            @Override
            public void onSuccess(Room value) {
                view.onRoomInformationSuccess(value);
            }

            @Override
            public void onError() {
                view.onRoomInformationFailed();
            }
        });
    }
}
