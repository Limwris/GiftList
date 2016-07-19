package com.nichesoftware.giftlist.contracts;

/**
 * Created by Kattleya on 07/07/2016.
 */
public interface AuthenticationContract {
    interface OnAuthenticationCallback {
        void onSuccess();
        void onError();
    }
    interface View extends AbstractContract.View {
        void dismiss();
    }
    interface UserActionListener extends AbstractContract.UserActionListener {
        void authenticate(final String username, final String password, final OnAuthenticationCallback callback);
    }
}
