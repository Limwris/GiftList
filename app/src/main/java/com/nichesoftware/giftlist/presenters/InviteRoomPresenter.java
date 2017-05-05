package com.nichesoftware.giftlist.presenters;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.nichesoftware.giftlist.BaseApplication;
import com.nichesoftware.giftlist.contracts.InviteRoomContract;
import com.nichesoftware.giftlist.model.Invitation;
import com.nichesoftware.giftlist.model.Room;
import com.nichesoftware.giftlist.repository.cache.Cache;
import com.nichesoftware.giftlist.repository.datasource.AuthDataSource;
import com.nichesoftware.giftlist.repository.datasource.CloudDataSource;
import com.nichesoftware.giftlist.repository.datasource.CloudDataSourceDecorator;
import com.nichesoftware.giftlist.repository.datasource.DataSource;
import com.nichesoftware.giftlist.repository.provider.InvitationDataSourceProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Invite room presenter
 */
public class InviteRoomPresenter extends AuthenticationPresenter<InviteRoomContract.View>
        implements InviteRoomContract.Presenter {
    /// Constants   ---------------------------------------------------------------------------------
    private static final String TAG = InviteRoomPresenter.class.getSimpleName();

    /// Fields
    /**
     * Data provider
     */
    private final InvitationDataSourceProvider mDataProvider;

    /**
     * Subscription RX
     */
    private Disposable mAcceptInvitationSubscription, mDeclineInvitationSubscription;

    /**
     * Constructor
     *
     * @param view                  View to attach
     * @param connectedDataSource   The cloud data provider
     * @param authDataSource        Authentication data source
     */
    public InviteRoomPresenter(@NonNull InviteRoomContract.View view, @NonNull Cache<Room> cache,
                          @NonNull CloudDataSource<Invitation> connectedDataSource,
                          @NonNull AuthDataSource authDataSource) {
        super(view, authDataSource);
        mDataProvider = new InvitationDataSourceProvider(cache, connectedDataSource);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAcceptInvitationSubscription != null && !mAcceptInvitationSubscription.isDisposed()) {
            mAcceptInvitationSubscription.dispose();
        }
        if (mDeclineInvitationSubscription != null && !mDeclineInvitationSubscription.isDisposed()) {
            mDeclineInvitationSubscription.dispose();
        }
    }

    @Override
    public void acceptInvitationToRoom(String roomId) {
        Invitation invitation = new Invitation();
        invitation.setRoom(new Room(roomId));
        if (mAcceptInvitationSubscription != null && !mAcceptInvitationSubscription.isDisposed()) {
            mAcceptInvitationSubscription.dispose();
        }
        mAcceptInvitationSubscription = mDataProvider.update(invitation)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> mAttachedView.showLoader())
                .doFinally(() -> mAttachedView.hideLoader())
                .subscribe(acceptedInvitation -> {
                    Log.d(TAG, "acceptInvitationToRoom");
                    mAttachedView.onAcceptInvitationSuccess();
                },
                throwable -> {
                    Log.e(TAG, "acceptInvitationToRoom", throwable);
                    mAttachedView.onAcceptInvitationFailed();
                });
    }

    @Override
    public void declineInvitationToRoom(Invitation invitation) {
        if (mDeclineInvitationSubscription != null && !mDeclineInvitationSubscription.isDisposed()) {
            mDeclineInvitationSubscription.dispose();
        }
        mDeclineInvitationSubscription = mDataProvider.delete(invitation)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> mAttachedView.showLoader())
                .doFinally(() -> mAttachedView.hideLoader())
                .subscribe(invitations -> {
                            Log.d(TAG, "declineInvitationToRoom: onNext");
                            mAttachedView.onDeclineInvitationSuccess();
                        },
                        throwable -> {
                            Log.e(TAG, "declineInvitationToRoom: onError", throwable);
                            mAttachedView.onDeclineInvitationFailed();
                        });
    }
}
