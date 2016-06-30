package com.nichesoftware.giftlist.contracts;

import com.nichesoftware.giftlist.model.Room;

/**
 * Created by n_che on 27/06/2016.
 */
public interface InviteRoomContract {
    interface View extends AbstractContract.View {
        /**
         * L'acception de l'invitation a réussi
         */
        void onAcceptInvitationSuccess();
        /**
         * Echec lors de l'acception de l'invitation
         */
        void onAcceptInvitationFailed();
        /**
         * Succès du retour d'information sur la salle
         */
        void onRoomInformationSuccess(Room room);
        /**
         * Echec lors de la tentative de récupération des informations de la salle
         */
        void onRoomInformationFailed();
    }
    interface UserActionListener extends AbstractContract.UserActionListener {
        void acceptInvitationToRoom(final int roomId, final String invitationToken);
        void getRoomInformation(final int roomId);
    }
}
