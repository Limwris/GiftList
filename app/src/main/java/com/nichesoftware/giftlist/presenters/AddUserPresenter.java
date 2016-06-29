package com.nichesoftware.giftlist.presenters;

import android.support.annotation.NonNull;

import com.nichesoftware.giftlist.contracts.AddUserContract;
import com.nichesoftware.giftlist.dataproviders.DataProvider;
import com.nichesoftware.giftlist.model.User;

import java.util.List;

/**
 * Created by n_che on 22/06/2016.
 */
public class AddUserPresenter extends AbstractPresenter implements AddUserContract.UserActionListener {
    private static final String TAG = AddGiftPresenter.class.getSimpleName();

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
        // TODO: pas de chaîne en dur...
        view.showLoader("Lancement de l'invitation...");
        dataProvider.inviteUserToRoom(roomId, username, new DataProvider.Callback() {
            @Override
            public void onSuccess() {
                view.hideLoader();
                view.onUserAddedSuccess();
            }

            @Override
            public void onError() {
                view.hideLoader();
                view.onUserAddedFailed();
            }
        });
    }

    @Override
    public void loadContacts(int roomId) {
        // TODO: pas de chaîne en dur...
        view.showLoader("Recherche des contacts possédant l'application");
        dataProvider.retreiveAvailableContacts(roomId,
                new DataProvider.CallbackValue<List<User>>() {
                    @Override
                    public void onSuccess(List<User> value) {
                        view.hideLoader();
                        view.showContacts(value);
                    }

                    @Override
                    public void onError() {
                        view.hideLoader();
                        view.showContacts(null);
                    }
                });
    }
}
