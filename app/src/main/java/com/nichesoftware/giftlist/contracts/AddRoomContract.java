package com.nichesoftware.giftlist.contracts;

/**
 * Created by n_che on 27/04/2016.
 */
public interface AddRoomContract {
    interface View extends AbstractContract.View {
        /**
         * Création de la salle avec succès
         */
        void onCreateRoomSuccess();
        /**
         * Echec lors de la création de la salle
         */
        void onCreateRoomFailed();
    }
    interface UserActionListener extends AbstractContract.UserActionListener {
        void addRoom(final String roomName, final String occasion);
    }
}
