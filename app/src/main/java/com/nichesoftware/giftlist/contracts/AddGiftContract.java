package com.nichesoftware.giftlist.contracts;

/**
 * Contract of the "Add gift" functionality
 */
public interface AddGiftContract {
    interface View extends AuthenticationContract.View {
        /**
         * Création du cadeau avec succès
         */
        void onCreateGiftSuccess();
        /**
         * Echec lors de la création du cadeau
         */
        void onCreateGiftFailed();
    }
    interface Presenter extends AuthenticationContract.Presenter {
        void addGift(String roomid, final String name, double price, double amount,
                     final String description, final String filePath);
    }
}
