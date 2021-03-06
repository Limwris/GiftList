package com.nichesoftware.giftlist.contracts;

import com.nichesoftware.giftlist.model.Gift;

/**
 * Created by n_che on 09/06/2016.
 */
public interface GiftDetailContract {
    interface View extends AbstractContract.View {
        /**
         * Création de la salle avec succès
         */
        void onUpdateGiftSuccess();
        /**
         * Echec lors de la création de la salle
         */
        void onUpdateGiftFailed();
    }
    interface UserActionListener extends AbstractContract.UserActionListener {
        void updateGift(final Gift gift, int roomId, double allocatedAmount,
                        final String description, final String filePath);
    }
}
