package com.nichesoftware.giftlist.contracts;

/**
 * Launch screen contract
 */
public interface LaunchScreenContract {
    interface View extends AuthenticationContract.View {
        /**
         * Affiche la vue des salles
         */
        void showRoomsActivity();
    }
    interface Presenter extends AuthenticationContract.Presenter {
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
