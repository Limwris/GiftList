package com.nichesoftware.giftlist.contracts;

/**
 * Created by Kattleya on 07/07/2016.
 */
public interface AuthenticationContract {
    interface View extends AbstractContract.View {
        interface OnAuthenticationCallback {
            void onSuccess();
            void onError();
        }
        void dismiss();
    }
    interface UserActionListener extends AbstractContract.UserActionListener {
        interface OnAuthenticationCallback {
            void onSuccess();
            void onError();
        }
        void authenticate(final String username, final String password, final OnAuthenticationCallback callback);
    }
}
