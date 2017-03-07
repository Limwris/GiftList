package com.nichesoftware.giftlist.views.authentication;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatDialog;
import android.view.View;
import android.widget.ProgressBar;

import com.nichesoftware.giftlist.Injection;
import com.nichesoftware.giftlist.R;
import com.nichesoftware.giftlist.contracts.AuthenticationContract;
import com.nichesoftware.giftlist.presenters.AuthenticationPresenter;
import com.nichesoftware.giftlist.utils.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Authentication dialog
 */
public class AuthenticationDialog extends AppCompatDialog implements AuthenticationContract.View {
    // Constants   ---------------------------------------------------------------------------------
    private final static String TAG = AuthenticationDialog.class.getSimpleName();

    // Fields   ------------------------------------------------------------------------------------
    /**
     * Listener sur les actions de l'utilisateur
     */
    private AuthenticationContract.UserActionListener actionsListener;

    /**
     * Callback sur le processus d'authentification
     */
    private AuthenticationContract.OnAuthenticationCallback authenticationCallback;

    /**
     * Unbinder Butter Knife
     */
    private Unbinder mButterKnifeUnbinder;

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
            actionsListener.authenticate(
                    mUsernameEditText.getText().toString(),
                    mPasswordEditText.getText().toString(),
                    authenticationCallback);
        }
    }

    @OnClick(R.id.authentication_dialog_cancel_button)
    void onCancelButtonClick() {
        dismiss();
    }

    public AuthenticationDialog(Context context, final AuthenticationContract.OnAuthenticationCallback authenticationCallback) {
        super(context, R.style.AppTheme_Dark_Dialog);
        this.authenticationCallback = authenticationCallback;

        setContentView(R.layout.log_in_dialog);
        mButterKnifeUnbinder = ButterKnife.bind(this);

        actionsListener = new AuthenticationPresenter(this,
                Injection.getDataProvider(getContext()));
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

    @Override
    public void showLoader() {
        mProgressBar.setVisibility(View.VISIBLE);
        mProgressBar.animate();
    }

    @Override
    public void hideLoader() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                  Getters & setters                                         //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Getter sur la callback sur le processus d'authentification
     * @return
     */
    public AuthenticationContract.OnAuthenticationCallback getAuthenticationCallback() {
        return authenticationCallback;
    }

    /**
     * Setter sur la callback sur le processus d'authentification
     * @param authenticationCallback
     */
    public void setAuthenticationCallback(AuthenticationContract.OnAuthenticationCallback authenticationCallback) {
        this.authenticationCallback = authenticationCallback;
    }
}
