package com.nichesoftware.giftlist.contracts;

/**
 * Created by n_che on 09/06/2016.
 */
public interface AddGiftContract {
    interface View {
        /**
         * Création du cadeau avec succès
         */
        void onCreateGiftSuccess();
        /**
         * Echec lors de la création du cadeau
         */
        void onCreateGiftFailed();
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
        void addGift(int roomid, final String name, double price, double amount);
    }
}
