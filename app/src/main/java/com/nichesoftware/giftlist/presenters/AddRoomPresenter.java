package com.nichesoftware.giftlist.presenters;

import android.support.annotation.NonNull;

import com.nichesoftware.giftlist.contracts.AddRoomContract;
import com.nichesoftware.giftlist.dataproviders.DataProvider;

/**
 * Created by n_che on 09/06/2016.
 */
public class AddRoomPresenter extends AbstractPresenter implements AddRoomContract.UserActionListener {
    private static final String TAG = AddRoomPresenter.class.getSimpleName();

    /**
     * View
     */
    private AddRoomContract.View view;

    /**
     * Constructeur
     * @param view
     * @param dataProvider
     */
    public AddRoomPresenter(@NonNull AddRoomContract.View view, @NonNull DataProvider dataProvider) {
        this.dataProvider = dataProvider;
        this.view = view;
    }

    @Override
    public void addRoom(String roomName, String occasion) {

        dataProvider.createRoom(roomName, occasion, new DataProvider.Callback() {
            @Override
            public void onSuccess() {
                view.onCreateRoomSuccess();
            }

            @Override
            public void onError() {
                view.onCreateRoomFailed();
            }
        });
    }
}
