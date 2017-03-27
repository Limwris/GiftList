package com.nichesoftware.giftlist.contracts;

import com.nichesoftware.giftlist.model.Gift;
import com.nichesoftware.giftlist.views.giftlist.GiftVO;

import java.util.List;

/**
 * "Gifts list" contract
 */
public interface GiftListContract {
    interface View extends AuthenticationContract.View {
        /**
         * Affiche les cadeaux passées en paramètre
         * @param gifts
         */
        void showGifts(List<GiftVO> gifts);
        /**
         * Affiche le détail du cadeau passée en paramètre (id)
         * @param gift
         */
        void showGiftDetail(final Gift gift);
        /**
         * La sortie de la salle a réussi
         */
        void onLeaveRoomSuccess();
        /**
         * La sortie de la salle a échoué
         */
        void onLeaveRoomError();
    }
    interface Presenter extends AuthenticationContract.Presenter {
        /**
         * Action permettant de charger la liste des cadeaux
         * @param roomId        Identifiant de la salle
         * @param forceUpdate   Flag indiquant si la mise à jour doit être forcée
         */
        void loadGifts(final String roomId, boolean forceUpdate);
        /**
         * Action permettant d'ouvrir le détail d'un cadeau
         * @param giftId
         */
        void openGiftDetail(String giftId);
        /**
         * Action permettant à l'utilisateur de quitter la salle
         * @param roomId
         */
        void leaveCurrentRoom(final String roomId);
        /**
         * Indique si l'invitation est disponible
         * @return flag indiquant si l'invitation est disponible
         */
        boolean isInvitationAvailable();
    }
}
