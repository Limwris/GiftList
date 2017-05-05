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
import com.nichesoftware.giftlist.contracts.AddUserContract;
import com.nichesoftware.giftlist.model.Contacts;
import com.nichesoftware.giftlist.model.Invitation;
import com.nichesoftware.giftlist.model.Room;
import com.nichesoftware.giftlist.model.User;
import com.nichesoftware.giftlist.repository.datasource.AuthDataSource;
import com.nichesoftware.giftlist.service.Service;
import com.nichesoftware.giftlist.session.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Add user presenter
 */
public class AddUserPresenter extends AuthenticationPresenter<AddUserContract.View>
        implements AddUserContract.Presenter {
    /// Constants   ---------------------------------------------------------------------------------
    private static final String TAG = AddGiftPresenter.class.getSimpleName();

    /// Fields   ------------------------------------------------------------------------------------
    /**
     * Service
     */
    private Service mService;

    /**
     * RX subscriptions
     */
    private Disposable mAddUserSubscription, mLoadContactsSubscription;

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
        if (mLoadContactsSubscription != null && !mLoadContactsSubscription.isDisposed()) {
            mLoadContactsSubscription.dispose();
        }
    }

    @Override
    public void inviteUserToCurrentRoom(String roomId, String username) {
        User invitedUser = new User(username);
        Invitation invitation = new Invitation();
        invitation.setInvitedUser(invitedUser);

        if (mAddUserSubscription != null && !mAddUserSubscription.isDisposed()) {
            mAddUserSubscription.dispose();
        }
        mAddUserSubscription = mService.inviteUserToRoom(roomId, invitation)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(disposable -> mAttachedView.showLoader())
                    .doFinally(() -> mAttachedView.hideLoader())
                    .subscribe(invitationDone -> mAttachedView.onUserAddedSuccess(),
                            throwable -> {
                                mAttachedView.showError(throwable.getMessage());
                                mAttachedView.onUserAddedFailed();
                            },
                            () -> Log.d(TAG, "inviteUserToCurrentRoom: onComplete"));
    }

    @Override
    public void inviteUsersToCurrentRoom(String roomId, List<String> usernames) {

        List<Invitation> invitations = new ArrayList<>();
        for (String username: usernames) {
            User invitedUser = new User(username);
            Invitation invitation = new Invitation();
            invitation.setInvitedUser(invitedUser);
            invitations.add(invitation);
        }

        if (mAddUserSubscription != null && !mAddUserSubscription.isDisposed()) {
            mAddUserSubscription.dispose();
        }
        mAddUserSubscription = mService.inviteUsersToRoom(roomId, invitations)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> mAttachedView.showLoader())
                .doFinally(() -> mAttachedView.hideLoader())
                .subscribe(invitationDone -> mAttachedView.onUserAddedSuccess(),
                        throwable -> {
                            Log.e(TAG, "inviteUsersToCurrentRoom: onError", throwable);
                            mAttachedView.showError(throwable.getMessage());
                        });
    }

    @Override
    public void loadContacts(String roomId) {
        Log.d(TAG, String.format(Locale.getDefault(), "loadContacts: from room %s", roomId));
        Contacts contacts = new Contacts();
        contacts.setPhoneNumbers(fetchContacts());

        if (mLoadContactsSubscription != null && !mLoadContactsSubscription.isDisposed()) {
            mLoadContactsSubscription.dispose();
        }
        mLoadContactsSubscription = mService.retreiveAvailableContacts(roomId, contacts)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> mAttachedView.showLoader())
                .doFinally(() -> mAttachedView.hideLoader())
                .subscribe(users -> {
                    Log.d(TAG, "loadContacts: onNext");
                    mAttachedView.showContacts(users);
                        },
                        throwable -> {
                            Log.e(TAG, "loadContacts: onError", throwable);
                            mAttachedView.showContacts(null);
                        });
    }

    // region Private methods
    /**
     * Retrieve available contacts from phone numbers list
     *
     * @return  List of phone numbers in contact list of the device
     */
    private List<String> fetchContacts() {
        Log.d(TAG, "fetchContacts");
        final Context context = BaseApplication.getAppContext();

        List<String> phoneNumbers = new ArrayList<>();

        ContentResolver contentResolver = context.getContentResolver();

        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                null,
                ContactsContract.Contacts.HAS_PHONE_NUMBER + " != 0",
                null,
                null);

        // Loop for every contact in the phone
        if (cursor != null && cursor.getCount() > 0) {

            while (cursor.moveToNext()) {

                String contact_id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                Cursor phoneCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        new String[] { contact_id },
                        null);

                if (phoneCursor!= null) {
                    while (phoneCursor.moveToNext()) {
                        int phoneType = phoneCursor.getInt(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                        if (phoneType == ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE) {
                            PhoneNumberUtil pnu = PhoneNumberUtil.getInstance();
                            Phonenumber.PhoneNumber pn = null;

                            try {
                                pn = pnu.parse(phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)),
                                        Locale.getDefault().getCountry());
                            } catch (NumberParseException ignored) { }

                            if (pn != null) {
                                String phoneNumber = pnu.format(pn, PhoneNumberUtil.PhoneNumberFormat.E164);

                                if (!phoneNumbers.contains(phoneNumber)) {
                                    phoneNumbers.add(phoneNumber);
                                }
                            }
                        }
                    }

                    phoneCursor.close();
                }
            }

            cursor.close();
        }

        return phoneNumbers;
    }
    // endregion
}
