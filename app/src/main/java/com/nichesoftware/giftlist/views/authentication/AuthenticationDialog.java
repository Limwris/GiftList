package com.nichesoftware.giftlist.views.authentication;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatDialog;
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
     * Lock on the login button
     */
    private boolean lock;

    /**
     * Graphical components
     */
    @BindView(R.id.start_log_in_dialog_username_edit_text)
    TextInputEditText mUsernameEditText;
    @BindView(R.id.start_log_in_dialog_password_edit_text)
    TextInputEditText mPasswordEditText;
    @BindView(R.id.toolbar_progressBar)
    ProgressBar mProgressBar;

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
            mUsernameEditText.setError(getContext().getString(R.string.start_dialog_empty_field_error_text));
            valid = false;
        } else {
            mUsernameEditText.setError(null);
        }

        final String password = mPasswordEditText.getText().toString();
        if (StringUtils.isEmpty(password)) {
            mPasswordEditText.setError(getContext().getString(R.string.start_dialog_empty_field_error_text));
            valid = false;
        } else {
            mPasswordEditText.setError(null);
        }

        return valid;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                  Implement methods                                         //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void showLoader() {
        mProgressBar.setVisibility(View.VISIBLE);
        mProgressBar.animate();
    }

    public void hideLoader() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    /**********************************************************************************************/
    /***                                        Lock                                            ***/
    /**********************************************************************************************/

    /**
     * Adds a lock on the log button
     */
    private void addLock() {
        lock = true;
        findViewById(R.id.authentication_dialog_ok_button).setEnabled(false);
    }

    /**
     * Removes the lock on the login button
     */
    private void removeLock() {
        lock = false;
        findViewById(R.id.authentication_dialog_ok_button).setEnabled(true);
    }
    /**********************************************************************************************/
    /***                                       Errors                                           ***/
    /**********************************************************************************************/

    /**
     * Indicates that the login field is empty
     */
    public void setLoginEmpty() {
        Log.d(TAG, "[AuthenticationDialog] setLoginEmpty");
        removeLock();
        hideLoader();
        mUsernameEditText.setError(getContext().getString(R.string.login_username_empty));
    }

    /**
     * Indicates that the password field is empty
     */
    public void setPasswordEmpty() {
        Log.d(TAG, "[AuthenticationDialog] setPasswordEmpty");
        removeLock();
        hideLoader();
        mPasswordEditText.setError(getContext().getString(R.string.login_password_empty));
    }

    /**
     * Indicates that the login has failed
     */
    public void setAuthentInError() {
        Log.d(TAG, "[AuthenticationDialog] setAuthentInError");
        removeLock();
        hideLoader();
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
