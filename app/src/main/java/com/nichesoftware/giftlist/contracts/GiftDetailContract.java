package com.nichesoftware.giftlist.contracts;

import com.nichesoftware.giftlist.model.Gift;

/**
 * Gift detail contract
 */
public interface GiftDetailContract {
    interface View extends AuthenticationContract.View {
        /**
         * Mise à jour réussie du cadeau
         */
        void onUpdateGiftSuccess();
    }
    interface Presenter extends AuthenticationContract.Presenter {
        void updateGift(final Gift gift, String roomId, double allocatedAmount,
                        final String description, final String filePath);
    }
}
