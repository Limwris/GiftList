package com.nichesoftware.giftlist.contracts;

/**
 * Created by Kattleya on 28/06/2016.
 */
public interface AbstractContract {
    interface View {
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
        void doDisconnect();
    }
}
