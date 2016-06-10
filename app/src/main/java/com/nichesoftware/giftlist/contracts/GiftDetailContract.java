package com.nichesoftware.giftlist.contracts;

import com.nichesoftware.giftlist.model.Gift;

/**
 * Created by n_che on 09/06/2016.
 */
public interface GiftDetailContract {
    interface View {
        /**
         * Création de la salle avec succès
         */
        void onUpdateGiftSuccess();
        /**
         * Echec lors de la création de la salle
         */
        void onUpdateGiftFailed();
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
        void updateGift(final Gift gift, double allocatedAmount);
    }
}
