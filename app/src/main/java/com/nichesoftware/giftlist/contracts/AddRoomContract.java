package com.nichesoftware.giftlist.contracts;

/**
 * "Add room" contract
 */
public interface AddRoomContract {
    interface View extends AuthenticationContract.View {
        /**
         * Création de la salle avec succès
         */
        void onCreateRoomSuccess();
        /**
         * Echec lors de la création de la salle
         */
        void onCreateRoomFailed();
    }
    interface Presenter extends AuthenticationContract.Presenter {
        void addRoom(final String roomName, final String occasion);
    }
}
