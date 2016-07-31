package com.nichesoftware.giftlist.contracts;

/**
 * Created by n_che on 09/06/2016.
 */
public interface AddGiftContract {
    interface View extends AbstractContract.View {
        /**
         * Création du cadeau avec succès
         */
        void onCreateGiftSuccess();
        /**
         * Echec lors de la création du cadeau
         */
        void onCreateGiftFailed();
    }
    interface UserActionListener extends AbstractContract.UserActionListener {
        void addGift(int roomid, final String name, double price, double amount,
                     final String description, final String filePath);
    }
}
