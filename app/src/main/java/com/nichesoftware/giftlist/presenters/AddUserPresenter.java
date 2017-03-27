package com.nichesoftware.giftlist.presenters;

import android.support.annotation.NonNull;
import android.util.Log;

import com.nichesoftware.giftlist.contracts.AddUserContract;
import com.nichesoftware.giftlist.model.Invitation;
import com.nichesoftware.giftlist.model.Room;
import com.nichesoftware.giftlist.model.User;
import com.nichesoftware.giftlist.repository.datasource.AuthDataSource;
import com.nichesoftware.giftlist.service.Service;
import com.nichesoftware.giftlist.session.SessionManager;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Add user presenter
 */
public class AddUserPresenter extends AuthenticationPresenter<AddUserContract.View>
        implements AddUserContract.Presenter {
    // Constants   ---------------------------------------------------------------------------------
    private static final String TAG = AddGiftPresenter.class.getSimpleName();

    // Fields   ------------------------------------------------------------------------------------
    /**
     * Service
     */
    private Service mService;

    /**
     * Constructor
     *
     * @param view                View to attach
     * @param authDataSource      Authentication data source
     */
    public AddUserPresenter(@NonNull AddUserContract.View view,
                            @NonNull AuthDataSource authDataSource,
                            @NonNull Service service) {
        super(view, authDataSource);
        mService = service;
    }

    @Override
    public void inviteUserToCurrentRoom(String roomId, String username) {
        Room room = new Room(roomId);
        User invitedUser = new User(username);
        Invitation invitation = new Invitation();
        invitation.setInvitedUser(invitedUser);
        invitation.setRoom(room);
        final String token = SessionManager.getInstance().getToken();
        if (token != null) {
            mService.inviteUserToRoom(token , invitation)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(disposable -> mAttachedView.showLoader())
                    .doFinally(() -> mAttachedView.hideLoader())
                    .subscribe(addGift -> mAttachedView.onUserAddedSuccess(),
                            throwable -> {
                                mAttachedView.showError(throwable.getMessage());
                                mAttachedView.onUserAddedFailed();
                            },
                            () -> Log.d(TAG, "inviteUserToCurrentRoom: onComplete"));
        }
    }

    @Override
    public void loadContacts(String roomId) {
//        mAttachedView.showLoader();
//        retreiveAvailableContacts(roomId,
//                    @Override
//                    public void onSuccess(List<User> value) {
//                        mAttachedView.hideLoader();
//                        mAttachedView.showContacts(value);
//                    }
//
//                    @Override
//                    public void onError() {
//                        mAttachedView.hideLoader();
//                        mAttachedView.showContacts(null);
//                    }
//                });
    }
}
