package com.nichesoftware.giftlist.contracts;

import android.content.Context;

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
        /**
         *
         * @return
         */
        Context getContext();
    }
    interface UserActionListener {
        void doDisconnect();
        boolean isConnected();
        String getCurrentUsername();
    }
}
