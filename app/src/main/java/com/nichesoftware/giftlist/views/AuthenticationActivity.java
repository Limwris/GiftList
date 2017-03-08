package com.nichesoftware.giftlist.views;

import android.text.TextUtils;
import android.util.Log;

import com.nichesoftware.giftlist.contracts.AuthenticationContract;
import com.nichesoftware.giftlist.views.authentication.AuthenticationDialog;
import com.nichesoftware.giftlist.views.authentication.IAuthenticationListener;

/**
 * Authentication activity
 */
public abstract class AuthenticationActivity<P extends AuthenticationContract.Presenter> extends BaseActivity<P>
implements AuthenticationContract.View, IAuthenticationListener {
    private static final String TAG = AuthenticationActivity.class.getSimpleName();

    /**
     * Authentication dialog (could be instanciated anywhere in the app
     */
    protected AuthenticationDialog authenticationDialog;

    /**********************************************************************************************/
    /***                                Authentication                                          ***/
    /**********************************************************************************************/

    @Override
    public void onAuthentication(final String username, final String password) {
        Log.d(TAG, "onAuthentication");
        if(validate(username, password)) {
            getPresenter().onAuthentication(username, password);
        }
    }

    @Override
    public void onAuthenticationError() {
        Log.d(TAG, "onAuthenticationError");
        if (authenticationDialog != null) {
            //authenticationDialog.setAuthentInError();
        }
    }

    @Override
    public void onAuthenticationSucceeded() {
        Log.d(TAG, "onAuthenticationSucceeded");
        if (authenticationDialog != null) {
            authenticationDialog.dismiss();
        }
        performLogin();
    }

    /**
     * Method called after authentication succeeded
     */
    protected abstract void performLogin();

    /**
     * Validate the login form
     * @param username
     * @param password
     * @return whether the login form is valid or not
     */
    private boolean validate(final String username, final String password) {
        Log.d(TAG, "validate");
        boolean valid = true;

        if (TextUtils.isEmpty(username)) {
            authenticationDialog.setLoginEmpty();
            valid = false;
        }

        if (TextUtils.isEmpty(password)) {
            authenticationDialog.setPasswordEmpty();
            valid = false;
        }

        return valid;
    }
}
