package com.nichesoftware.giftlist.contracts;

import android.content.Context;

import com.nichesoftware.giftlist.presenters.IPresenter;

import java.util.Map;

/**
 * GCM presenter
 */
public interface GcmContract {
    interface Presenter {
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
