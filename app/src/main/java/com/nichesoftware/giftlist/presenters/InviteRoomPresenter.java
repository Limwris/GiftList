package com.nichesoftware.giftlist.presenters;

import android.support.annotation.NonNull;

import com.nichesoftware.giftlist.contracts.InviteRoomContract;
import com.nichesoftware.giftlist.model.Room;
import com.nichesoftware.giftlist.repository.cache.Cache;
import com.nichesoftware.giftlist.repository.datasource.AuthDataSource;
import com.nichesoftware.giftlist.repository.datasource.ConnectedDataSource;

/**
 * Invite room presenter
 */
public class InviteRoomPresenter extends BasePresenter<InviteRoomContract.View, Room>
        implements InviteRoomContract.Presenter {
    // Constants   ---------------------------------------------------------------------------------
    private static final String TAG = InviteRoomPresenter.class.getSimpleName();

    /**
     * Constructor
     *
     * @param view                  View to attach
     * @param cache                 Cache
     * @param connectedDataSource   The cloud data provider
     * @param authDataSource        Authentication data source
     */
    public InviteRoomPresenter(@NonNull InviteRoomContract.View view, @NonNull Cache<Room> cache,
                          @NonNull ConnectedDataSource<Room> connectedDataSource,
                          @NonNull AuthDataSource authDataSource) {
        super(view, cache, connectedDataSource, authDataSource);
    }

    @Override
    public void acceptInvitationToRoom(String roomId) {
//        acceptInvitationToRoom(roomId, new Callback() {
//            @Override
//            public void onSuccess() {
//                mAttachedView.onAcceptInvitationSuccess();
//            }
//
//            @Override
//            public void onError() {
//                mAttachedView.onAcceptInvitationFailed();
//            }
//        });
    }
}
