package com.nichesoftware.giftlist.contracts;

/**
 * Created by n_che on 27/06/2016.
 */
public interface InviteRoomContract {
    interface View {
        /**
         * L'acception de l'invitation a réussi
         */
        void onAcceptInvitationSuccess();
        /**
         * Echec lors de l'acception de l'invitation
         */
        void onAcceptInvitationFailed();
        /**
         * Affiche le loader
         */
        void showLoader();
        /**
         * Supprime le loader
         */
        void hideLoader();
    }
    interface UserActionListener {
        void acceptInvitationToRoom(final int roomId);
    }
}
