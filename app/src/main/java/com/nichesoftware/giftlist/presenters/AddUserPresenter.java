package com.nichesoftware.giftlist.presenters;

import android.support.annotation.NonNull;

import com.nichesoftware.giftlist.contracts.AddUserContract;
import com.nichesoftware.giftlist.dataproviders.DataProvider;
import com.nichesoftware.giftlist.model.User;

import java.util.List;

/**
 * Add user presenter
 */
public class AddUserPresenter extends AbstractPresenter<AddUserContract.View>
        implements AddUserContract.Presenter {
    // Constants   ---------------------------------------------------------------------------------
    private static final String TAG = AddGiftPresenter.class.getSimpleName();

    /**
     * Constructor
     *
     * @param view         View to attach
     * @param dataProvider The data provider
     */
    public AddUserPresenter(@NonNull AddUserContract.View view, @NonNull DataProvider dataProvider) {
        super(view, dataProvider);
    }


    @Override
    public void inviteUserToCurrentRoom(int roomId, String username) {
        mAttachedView.showLoader();
        mDataProvider.inviteUserToRoom(roomId, username, new DataProvider.Callback() {
            @Override
            public void onSuccess() {
                mAttachedView.hideLoader();
                mAttachedView.onUserAddedSuccess();
            }

            @Override
            public void onError() {
                mAttachedView.hideLoader();
                mAttachedView.onUserAddedFailed();
            }
        });
    }

    @Override
    public void loadContacts(int roomId) {
        mAttachedView.showLoader();
        mDataProvider.retreiveAvailableContacts(roomId,
                new DataProvider.CallbackValue<List<User>>() {
                    @Override
                    public void onSuccess(List<User> value) {
                        mAttachedView.hideLoader();
                        mAttachedView.showContacts(value);
                    }

                    @Override
                    public void onError() {
                        mAttachedView.hideLoader();
                        mAttachedView.showContacts(null);
                    }
                });
    }
}
