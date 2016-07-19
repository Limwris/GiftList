package com.nichesoftware.giftlist.contracts;

/**
 * Created by n_che on 08/06/2016.
 */
public interface LaunchScreenContract {
    interface View extends AbstractContract.View {
        /**
         * Affiche la vue des salles
         */
        void showRoomsActivity();
    }
    interface UserActionListener extends AbstractContract.UserActionListener {
        /**
         * Lance l'application
         */
        void startApplication();
        /**
         * Enregistrement
         * @param username
         * @param password
         */
        void register(final String username, final String password);
    }
}
