package com.nichesoftware.giftlist.presenters;

import android.support.annotation.NonNull;

import com.nichesoftware.giftlist.contracts.AddRoomContract;
import com.nichesoftware.giftlist.dataproviders.DataProvider;

/**
 * Add room presenter
 */
public class AddRoomPresenter extends AbstractPresenter<AddRoomContract.View>
        implements AddRoomContract.Presenter {
    // Constants   ---------------------------------------------------------------------------------
    private static final String TAG = AddRoomPresenter.class.getSimpleName();

    /**
     * Constructeur
     * @param view
     * @param dataProvider
     */
    public AddRoomPresenter(@NonNull AddRoomContract.View view, @NonNull DataProvider dataProvider) {
        super(view, dataProvider);
    }

    @Override
    public void addRoom(String roomName, String occasion) {

        mDataProvider.createRoom(roomName, occasion, new DataProvider.Callback() {
            @Override
            public void onSuccess() {
                mAttachedView.onCreateRoomSuccess();
            }

            @Override
            public void onError() {
                mAttachedView.onCreateRoomFailed();
            }
        });
    }
}
