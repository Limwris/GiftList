package com.nichesoftware.giftlist.contracts;

/**
 * Created by n_che on 27/04/2016.
 */
public interface AddRoomContract {
    interface View {
        /**
         * Création de la salle avec succès
         */
        void onCreateRoomSuccess();
        /**
         * Echec lors de la création de la salle
         */
        void onCreateRoomFailed();
        /**
         * Affiche le loader
         */
        void showLoader(final String message);
        /**
         * Supprime le loader
         */
        void hideLoader();
    }
    interface UserActionListener {
        void addRoom(final String roomName, final String occasion);
    }
}
