package com.nichesoftware.giftlist.contracts;

/**
 * Created by n_che on 27/04/2016.
 */
public interface AddPersonContract {
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
        void addPerson(final String firstName, final String lastName);
    }
}
