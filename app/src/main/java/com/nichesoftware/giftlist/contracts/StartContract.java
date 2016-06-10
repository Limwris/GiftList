package com.nichesoftware.giftlist.contracts;

/**
 * Created by n_che on 08/06/2016.
 */
public interface StartContract {
    interface View {
        /**
         *
         */
        void showRoomsActivity();
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
        /**
         *
         */
        void startApplication();
    }
}
