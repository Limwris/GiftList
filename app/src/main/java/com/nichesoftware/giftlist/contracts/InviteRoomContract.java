package com.nichesoftware.giftlist.contracts;

/**
 * "Invite room" contract
 */
public interface InviteRoomContract {
    interface View extends AuthenticationContract.View {
        /**
         * L'acception de l'invitation a réussi
         */
        void onAcceptInvitationSuccess();
        /**
         * Echec lors de l'acception de l'invitation
         */
        void onAcceptInvitationFailed();
    }
    interface Presenter extends AuthenticationContract.Presenter {
        void acceptInvitationToRoom(final String roomId);
    }
}
