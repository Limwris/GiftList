package com.nichesoftware.giftlist.contracts;

import com.nichesoftware.giftlist.model.User;

import java.util.List;

/**
 * Created by n_che on 22/06/2016.
 */
public interface AddUserContract {
    interface View {
        /**
         * Ajout d'un utilisateur avec succès
         */
        void onUserAddedSuccess();
        /**
         * Echec lors de l'ajout d'un utilisateur
         */
        void onUserAddedFailed();
        /**
         * Affiche le loader
         */
        void showLoader(final String message);
        /**
         * Supprime le loader
         */
        void hideLoader();
        /**
         * Affiche les contacts passées en paramètre
         * @param users
         */
        void showContacts(List<User> users);
    }
    interface UserActionListener {
        /**
         * Action permettant à l'utilisateur d'inviter un autre utilisateur dans la salle
         * @param roomId
         * @param username
         */
        void inviteUserToCurrentRoom(final int roomId, final String username);
        void loadContacts();
    }
}
