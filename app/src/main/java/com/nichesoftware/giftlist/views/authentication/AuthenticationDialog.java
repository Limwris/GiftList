package com.nichesoftware.giftlist.views.authentication;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nichesoftware.giftlist.R;
import com.nichesoftware.giftlist.utils.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Authentication dialog
 */
public class AuthenticationDialog extends AppCompatDialog {
    // Constants   ---------------------------------------------------------------------------------
    private final static String TAG = AuthenticationDialog.class.getSimpleName();

    // Fields   ------------------------------------------------------------------------------------
    /**
     * Callback sur le processus d'authentification
     */
    private IAuthenticationListener mAthenticationlistener;

    /**
     * Unbinder Butter Knife
     */
    private Unbinder mButterKnifeUnbinder;

    /**
     * Graphical components
     */
    @BindView(R.id.start_log_in_dialog_username_input_layout)
    TextInputLayout mUsernameInputLayout;
    @BindView(R.id.start_log_in_dialog_username_edit_text)
    TextInputEditText mUsernameEditText;
    @BindView(R.id.start_log_in_dialog_password_input_layout)
    TextInputLayout mPasswordInputLayout;
    @BindView(R.id.start_log_in_dialog_password_edit_text)
    TextInputEditText mPasswordEditText;
    @BindView(R.id.toolbar_progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.authentication_dialog_ok_button)
    AppCompatButton mOkButton;

    @OnClick(R.id.authentication_dialog_ok_button)
    void onOkButtonClick() {
        if (validate()) {
            mAthenticationlistener.onAuthentication(
                    mUsernameEditText.getText().toString(),
                    mPasswordEditText.getText().toString());
        }
    }

    @OnClick(R.id.authentication_dialog_cancel_button)
    void onCancelButtonClick() {
        dismiss();
    }

    public AuthenticationDialog(Context context, final IAuthenticationListener authenticationCallback) {
        super(context, R.style.AppTheme_Dark_Dialog);
        this.mAthenticationlistener = authenticationCallback;

        setContentView(R.layout.log_in_dialog);
        mButterKnifeUnbinder = ButterKnife.bind(this);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mButterKnifeUnbinder.unbind();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                    Private methods                                         //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private boolean validate() {
        boolean valid = true;

        final String username = mUsernameEditText.getText().toString();
        if (StringUtils.isEmpty(username)) {
            mUsernameInputLayout.setErrorEnabled(true);
            mUsernameInputLayout.setError(getContext().getString(R.string.start_dialog_empty_field_error_text));
            valid = false;
        } else {
            mUsernameInputLayout.setErrorEnabled(false);
            mUsernameInputLayout.setError(null);
        }

        final String password = mPasswordEditText.getText().toString();
        if (StringUtils.isEmpty(password)) {
            mPasswordInputLayout.setErrorEnabled(true);
            mPasswordInputLayout.setError(getContext().getString(R.string.start_dialog_empty_field_error_text));
            valid = false;
        } else {
            mPasswordInputLayout.setErrorEnabled(false);
            mPasswordInputLayout.setError(null);
        }

        return valid;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                  Implement methods                                         //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void showLoader() {
        // Adds a lock on the log button
        mOkButton.setEnabled(false);
        mProgressBar.setVisibility(View.VISIBLE);
        mProgressBar.animate();
    }

    public void hideLoader() {
        // Removes the lock on the login button
        mOkButton.setEnabled(true);
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                     Error methods                                          //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Indicates that the login field is empty
     */
    public void setLoginEmpty() {
        Log.d(TAG, "setLoginEmpty");
        mUsernameEditText.setError(getContext().getString(R.string.login_username_empty));
    }

    /**
     * Indicates that the password field is empty
     */
    public void setPasswordEmpty() {
        Log.d(TAG, "setPasswordEmpty");
        mPasswordEditText.setError(getContext().getString(R.string.login_password_empty));
    }

    /**
     * Indicates that the login has failed
     */
    public void setAuthentInError() {
        Log.d(TAG, "setAuthentInError");
        Toast.makeText(getContext(), getContext().getString(R.string.login_error), Toast.LENGTH_LONG).show();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                  Getters & setters                                         //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Getter sur la callback sur le processus d'authentification
     * @return authenticationlistener
     */
    public IAuthenticationListener getAuthenticationlistener() {
        return mAthenticationlistener;
    }

    /**
     * Setter sur la callback sur le processus d'authentification
     * @param authenticationlistener
     */
    public void setAuthenticationlistener(IAuthenticationListener authenticationlistener) {
        this.mAthenticationlistener = authenticationlistener;
    }
}
