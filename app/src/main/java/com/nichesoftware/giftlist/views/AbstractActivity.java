package com.nichesoftware.giftlist.views;

import android.support.annotation.CallSuper;
import android.text.TextUtils;
import android.util.Log;

import com.nichesoftware.giftlist.contracts.AuthenticationContract;
import com.nichesoftware.giftlist.views.authentication.AuthenticationDialog;
import com.nichesoftware.giftlist.views.authentication.IAuthenticationListener;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Authentication activity
 */
public abstract class AbstractActivity<P extends AuthenticationContract.Presenter>
        extends BaseActivity<P>
        implements AuthenticationContract.View, IAuthenticationListener {
    // Constants   ---------------------------------------------------------------------------------
    private static final String TAG = AbstractActivity.class.getSimpleName();

    // Fields   ------------------------------------------------------------------------------------
    /**
     * Unbinder ButterKnife to handle the Activity lifecycle
     */
    private Unbinder mButterKnifeUnbinder;

    /**
     * Authentication dialog (could be instantiated anywhere in the app)
     */
    protected AuthenticationDialog authenticationDialog;

    @Override
    @CallSuper
    protected void initView() {
        super.initView();
        // Bind ButterKnife
        mButterKnifeUnbinder = ButterKnife.bind(this);
    }

    @Override
    @CallSuper
    protected void onDestroy() {
        super.onDestroy();
        // Unbind ButterKnife
        mButterKnifeUnbinder.unbind();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///     Authentication
    ////////////////////////////////////////////////////////////////////////////////////////////////

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

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///     Private methods
    ////////////////////////////////////////////////////////////////////////////////////////////////

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
