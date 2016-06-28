package com.nichesoftware.giftlist.contracts;

/**
 * Created by n_che on 27/06/2016.
 */
public interface GcmContract {
    interface ActionListener extends AbstractContract.UserActionListener {
        /**
         * Register the device and retreive the GCM token
         * @param gcmToken
         */
        void registerGcm(final String gcmToken);
    }
}
