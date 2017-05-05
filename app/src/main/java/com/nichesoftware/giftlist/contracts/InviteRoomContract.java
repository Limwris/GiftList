package com.nichesoftware.giftlist.contracts;

import com.nichesoftware.giftlist.model.Invitation;

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

        /**
         * Le refus de l'invitation a réussi
         */
        void onDeclineInvitationSuccess();

        /**
         * Echec lors du refus de l'invitation
         */
        void onDeclineInvitationFailed();
    }

    interface Presenter extends AuthenticationContract.Presenter {
        /**
         * Accept invitation to the given {@link com.nichesoftware.giftlist.model.Room}
         *
         * @param roomId {@link com.nichesoftware.giftlist.model.Room} identifier
         */
        void acceptInvitationToRoom(final String roomId);

        /**
         * Decline invitation to the given {@link com.nichesoftware.giftlist.model.Room}
         *
         * @param invitation {@link Invitation} to decline
         */
        void declineInvitationToRoom(final Invitation invitation);
    }
}
