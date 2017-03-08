package com.nichesoftware.giftlist.presenters;

import android.support.annotation.NonNull;

import com.nichesoftware.giftlist.contracts.InviteRoomContract;
import com.nichesoftware.giftlist.dataproviders.DataProvider;

/**
 * "Invite room" presenter
 */
public class InviteRoomPresenter extends AuthenticationPresenter<InviteRoomContract.View> implements InviteRoomContract.Presenter {
    private static final String TAG = InviteRoomPresenter.class.getSimpleName();

    /**
     * Constructor
     *
     * @param view         View to attach
     * @param dataProvider The data provider
     */
    public InviteRoomPresenter(@NonNull InviteRoomContract.View view, @NonNull DataProvider dataProvider) {
        super(view, dataProvider);
    }

    @Override
    public void acceptInvitationToRoom(int roomId) {
        mDataProvider.acceptInvitationToRoom(roomId, new DataProvider.Callback() {
            @Override
            public void onSuccess() {
                mAttachedView.onAcceptInvitationSuccess();
            }

            @Override
            public void onError() {
                mAttachedView.onAcceptInvitationFailed();
            }
        });
    }
}
