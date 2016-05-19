package com.nichesoftware.giftlist.contracts;

import com.nichesoftware.giftlist.model.Gift;

import java.util.List;

/**
 * Created by n_che on 25/04/2016.
 */
public interface GiftListContract {
    interface View {
        /**
         * Affiche les cadeaux passées en paramètre
         * @param gifts
         */
        void showGifts(List<Gift> gifts);
        /**
         * Affiche le détail du cadeau passée en paramètre (id)
         * @param giftId
         */
        void showGiftDetail(final String giftId);
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
        /**
         * Action permettant de charger la liste des cadeaux
         * @param forceUpdate   Flag indiquant si la mise à jour doit être forcée
         */
        void loadGifts(final String personId, boolean forceUpdate);
        /**
         * Action permettant d'ouvrir le détail d'un cadeau
         * @param gift
         */
        void openGiftDetail(Gift gift);
    }
}
