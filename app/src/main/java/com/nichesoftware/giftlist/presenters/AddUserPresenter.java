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
import io.reactivex.disposables.Disposable;

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
     * RX subscription
     */
    private Disposable mAddUserSubscription;

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
    public void onDestroy() {
        super.onDestroy();
        if (mAddUserSubscription != null && !mAddUserSubscription.isDisposed()) {
            mAddUserSubscription.dispose();
        }
    }

    @Override
    public void inviteUserToCurrentRoom(String roomId, String username) {
        User invitedUser = new User(username);
//        Room room = new Room(roomId);
//        Invitation invitation = new Invitation();
//        invitation.setInvitedUser(invitedUser);
//        invitation.setRoom(room);

        if (mAddUserSubscription != null && !mAddUserSubscription.isDisposed()) {
            mAddUserSubscription.dispose();
        }
//        mAddUserSubscription = mService.inviteUserToRoom(invitation)
        mAddUserSubscription = mService.inviteUserToRoom(roomId, invitedUser)
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
