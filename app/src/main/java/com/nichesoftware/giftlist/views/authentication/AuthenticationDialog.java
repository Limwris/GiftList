package com.nichesoftware.giftlist.views.authentication;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.nichesoftware.giftlist.Injection;
import com.nichesoftware.giftlist.R;
import com.nichesoftware.giftlist.contracts.AuthenticationContract;
import com.nichesoftware.giftlist.presenters.AuthenticationPresenter;
import com.nichesoftware.giftlist.utils.StringUtils;

/**
 * Created by Kattleya on 07/07/2016.
 */
public class AuthenticationDialog extends AppCompatDialog implements AuthenticationContract.View {
    private final static String TAG = AuthenticationDialog.class.getSimpleName();

    /**
     * Listener sur les actions de l'utilisateur
     */
    private AuthenticationContract.UserActionListener actionsListener;

    /**
     * Callback sur le processus d'authentification
     */
    private OnAuthenticationCallback authenticationCallback;

    public AuthenticationDialog(Context context, final OnAuthenticationCallback authenticationCallback) {
        super(context, R.style.AppTheme_Dark_Dialog);
        this.authenticationCallback = authenticationCallback;

        setContentView(R.layout.start_log_in_dialog);
        actionsListener = new AuthenticationPresenter(this,
                Injection.getDataProvider(getContext()));

        final TextInputEditText usernameEditText = (TextInputEditText) findViewById(R.id.start_log_in_dialog_username_edit_text);
        final TextInputEditText passwordEditText = (TextInputEditText) findViewById(R.id.start_log_in_dialog_password_edit_text);

        findViewById(R.id.authentication_dialog_ok_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate(usernameEditText, passwordEditText)) {
                    actionsListener.authenticate(
                            usernameEditText.getText().toString(),
                            passwordEditText.getText().toString(),
                            new AuthenticationContract.UserActionListener.OnAuthenticationCallback() {
                                @Override
                                public void onSuccess() {
                                    authenticationCallback.onSuccess();
                                }

                                @Override
                                public void onError() {
                                    // Todo : Do somehting ?
                                    authenticationCallback.onError();
                                }
                            });
                }
            }
        });
        findViewById(R.id.authentication_dialog_cancel_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private boolean validate(EditText usernameEditText, EditText passwordEditText) {
        boolean valid = true;

        final String username = usernameEditText.getText().toString();
        if (StringUtils.isEmpty(username)) {
            usernameEditText.setError(getContext().getString(R.string.start_dialog_empty_field_error_text));
            valid = false;
        } else {
            usernameEditText.setError(null);
        }

        final String password = passwordEditText.getText().toString();
        if (StringUtils.isEmpty(password)) {
            passwordEditText.setError(getContext().getString(R.string.start_dialog_empty_field_error_text));
            valid = false;
        } else {
            passwordEditText.setError(null);
        }

        return valid;
    }

    @Override
    public void showLoader() {
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.animate();
    }

    @Override
    public void hideLoader() {
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
    }

    /**
     * Getter sur la callback sur le processus d'authentification
     * @return
     */
    public OnAuthenticationCallback getAuthenticationCallback() {
        return authenticationCallback;
    }

    /**
     * Setter sur la callback sur le processus d'authentification
     * @param authenticationCallback
     */
    public void setAuthenticationCallback(OnAuthenticationCallback authenticationCallback) {
        this.authenticationCallback = authenticationCallback;
    }
}
