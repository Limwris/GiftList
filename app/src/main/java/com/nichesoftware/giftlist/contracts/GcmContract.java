package com.nichesoftware.giftlist.contracts;

/**
 * Created by n_che on 27/06/2016.
 */
public interface GcmContract {
    interface ActionListener {

        interface RegistrationCompleteCallback {
            void onRegistrationCompleted(final String token);
        }

        /**
         * Register the device and retreive the GCM token
         * @param callback
         */
        void registerGcm(RegistrationCompleteCallback callback);
    }
}
