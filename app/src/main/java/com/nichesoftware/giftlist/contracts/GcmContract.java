package com.nichesoftware.giftlist.contracts;

import android.content.Context;

import java.util.Map;

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
        /**
         * Process the GCM message when an invitation is sent
         * @param context
         * @param notification
         * @param data
         */
        void doProcessInvitationNotification(final Context context,
                                             final String notification, final String data);
    }
}
