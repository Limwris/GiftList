package com.nichesoftware.giftlist.views;

import android.text.TextUtils;
import android.util.Log;

import com.nichesoftware.giftlist.contracts.AuthenticationContract;
import com.nichesoftware.giftlist.views.authentication.AuthenticationDialog;
import com.nichesoftware.giftlist.views.authentication.IAuthenticationListener;

/**
 * Authentication activity
 */
public abstract class AuthenticationActivity<P extends AuthenticationContract.Presenter>
        extends BaseActivity<P>
        implements AuthenticationContract.View, IAuthenticationListener {
    // Constants   ---------------------------------------------------------------------------------
    private static final String TAG = AuthenticationActivity.class.getSimpleName();

    // Fields   ------------------------------------------------------------------------------------
    /**
     * Authentication dialog (could be instantiated anywhere in the app)
     */
    protected AuthenticationDialog mAuthenticationDialog;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///     Authentication
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onAuthentication(final String username, final String password) {
        Log.d(TAG, "onAuthentication");
        if(validate(username, password)) {
            presenter.onAuthentication(username, password);
        }
    }

    @Override
    public void displayAuthenticationLoader(boolean show) {
        if (mAuthenticationDialog != null) {
            if (show) {
                mAuthenticationDialog.showLoader();
            } else {
                mAuthenticationDialog.hideLoader();
            }
        }
    }

    @Override
    public void onAuthenticationError() {
        Log.d(TAG, "onAuthenticationError");
        if (mAuthenticationDialog != null) {
            mAuthenticationDialog.setAuthentInError();
        }
    }

    @Override
    public void onAuthenticationSucceeded() {
        Log.d(TAG, "onAuthenticationSucceeded");
        if (mAuthenticationDialog != null) {
            mAuthenticationDialog.dismiss();
        }
        performLogin();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///     Private methods
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Method called after authentication succeeded
     */
    protected abstract void performLogin();

    /**
     * Validate the login form
     * @param username      Username filled in by the user
     * @param password      Password filled in by the user
     * @return whether the login form is valid or not
     */
    private boolean validate(final String username, final String password) {
        Log.d(TAG, "validate");
        boolean valid = true;

        if (TextUtils.isEmpty(username)) {
            mAuthenticationDialog.setLoginEmpty();
            valid = false;
        }

        if (TextUtils.isEmpty(password)) {
            mAuthenticationDialog.setPasswordEmpty();
            valid = false;
        }

        return valid;
    }
}
