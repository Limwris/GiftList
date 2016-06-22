package com.nichesoftware.giftlist.presenters;

import android.support.annotation.NonNull;

import com.nichesoftware.giftlist.contracts.AddUserContract;
import com.nichesoftware.giftlist.dataproviders.DataProvider;

/**
 * Created by n_che on 22/06/2016.
 */
public class AddUserPresenter implements AddUserContract.UserActionListener {
    private static final String TAG = AddGiftPresenter.class.getSimpleName();

    /**
     * Data provider
     */
    private DataProvider dataProvider;

    /**
     * View
     */
    private AddUserContract.View view;

    /**
     * Constructeur
     * @param view
     * @param dataProvider
     */
    public AddUserPresenter(@NonNull AddUserContract.View view, @NonNull DataProvider dataProvider) {
        this.dataProvider = dataProvider;
        this.view = view;
    }


    @Override
    public void inviteUserToCurrentRoom(int roomId, String username) {
        // http://blog.xebia.fr/2013/01/28/google-cloud-messaging-presentation/
        // Todo
    }

    @Override
    public void loadContacts() {
        // Todo
        view.showContacts(null);
    }
}
