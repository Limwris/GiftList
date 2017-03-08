package com.nichesoftware.giftlist.contracts;

import com.nichesoftware.giftlist.model.Gift;

/**
 * Gift detail contract
 */
public interface GiftDetailContract {
    interface View extends AuthenticationContract.View {
        /**
         * Création de la salle avec succès
         */
        void onUpdateGiftSuccess();
        /**
         * Echec lors de la création de la salle
         */
        void onUpdateGiftFailed();
    }
    interface Presenter extends AuthenticationContract.Presenter {
        void updateGift(final Gift gift, int roomId, double allocatedAmount,
                        final String description, final String filePath);

        String getCurrentUsername();
    }
}
