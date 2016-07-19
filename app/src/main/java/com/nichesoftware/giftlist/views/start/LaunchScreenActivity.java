package com.nichesoftware.giftlist.views.start;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.nichesoftware.giftlist.BuildConfig;
import com.nichesoftware.giftlist.Injection;
import com.nichesoftware.giftlist.R;
import com.nichesoftware.giftlist.contracts.AuthenticationContract;
import com.nichesoftware.giftlist.contracts.LaunchScreenContract;
import com.nichesoftware.giftlist.presenters.LaunchScreenPresenter;
import com.nichesoftware.giftlist.utils.StringUtils;
import com.nichesoftware.giftlist.views.authentication.AuthenticationDialog;
import com.nichesoftware.giftlist.views.rooms.RoomsActivity;

/**
 * Created by n_che on 08/06/2016.
 */
public class LaunchScreenActivity extends AppCompatActivity implements LaunchScreenContract.View {
    private static final String TAG = LaunchScreenActivity.class.getSimpleName();

    /**
     * Listener sur les actions de l'utilisateur
     */
    private LaunchScreenContract.UserActionListener actionsListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launch_activity);

        actionsListener = new LaunchScreenPresenter(this, Injection.getDataProvider(this));

        // Todo: régler problème de la Toolbar n'affichant plus la vue standard de l'action bar
        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            ActionBar ab = getSupportActionBar();
            if (ab != null) {
                ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
                ab.setDisplayShowTitleEnabled(true);
            }
        }

        ViewPager viewPager = (ViewPager) findViewById(R.id.didacticiel_pager);
        viewPager.setAdapter(new DidacticielPagerAdapter(this));

        findViewById(R.id.didacticiel_sign_up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = getLayoutInflater();
                View dialoglayout = inflater.inflate(R.layout.sign_up_dialog, null);
                final TextInputEditText usernameEditText = (TextInputEditText) dialoglayout.findViewById(R.id.start_sign_up_dialog_username_edit_text);
                final TextInputEditText passwordEditText = (TextInputEditText) dialoglayout.findViewById(R.id.start_sign_up_dialog_password_edit_text);

                new AlertDialog.Builder(LaunchScreenActivity.this,
                        R.style.AppTheme_Dark_Dialog)
                        .setView(dialoglayout)
                        .setPositiveButton(R.string.start_dialog_sign_up_positive_button,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int whichButton) {
                                        if (validate(usernameEditText, passwordEditText)) {
                                            if (dialogInterface != null) {
                                                dialogInterface.dismiss();
                                            }

                                            actionsListener.register(
                                                    usernameEditText.getText().toString(),
                                                    passwordEditText.getText().toString());
                                        }
                                    }
                                })
                        .setNegativeButton(R.string.cancel_button_text,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int whichButton) {
                                        if (dialogInterface != null) {
                                            dialogInterface.dismiss();
                                        }
                                    }
                                }).show();
            }
        });

        findViewById(R.id.didacticiel_log_in).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuthenticationDialog authenticationDialog = new AuthenticationDialog(LaunchScreenActivity.this,
                        new AuthenticationContract.OnAuthenticationCallback() {
                            @Override
                            public void onSuccess() {
                                actionsListener.startApplication();
                            }

                            @Override
                            public void onError() {

                            }
                        });
                authenticationDialog.show();
            }
        });

        findViewById(R.id.didacticiel_not_authenticated_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(LaunchScreenActivity.this,
                        R.style.AppTheme_Dark_Dialog)
                        .setMessage(R.string.not_authenticated_start_message)
                        .setPositiveButton(R.string.not_authenticated_start_positive_button,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int  whichButton) {
                                        actionsListener.startApplication();
                                        if (dialogInterface != null) {
                                            dialogInterface.dismiss();
                                        }
                                    }
                                })
                        .setNegativeButton(R.string.cancel_button_text,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int  whichButton) {
                                        if (dialogInterface != null) {
                                            dialogInterface.dismiss();
                                        }
                                    }
                                }).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**********************************************************************************************/
    /************************************     View contract     ***********************************/
    /**********************************************************************************************/

    @Override
    public void showRoomsActivity() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "showRoomsActivity");
        }
        Intent intent = new Intent(this, RoomsActivity.class);
        startActivity(intent);
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

    private boolean validate(EditText usernameEditText, EditText passwordEditText) {
        boolean valid = true;

        final String username = usernameEditText.getText().toString();
        if (StringUtils.isEmpty(username)) {
            usernameEditText.setError(getString(R.string.start_dialog_empty_field_error_text));
            valid = false;
        } else {
            usernameEditText.setError(null);
        }

        final String password = passwordEditText.getText().toString();
        if (StringUtils.isEmpty(password)) {
            passwordEditText.setError(getString(R.string.start_dialog_empty_field_error_text));
            valid = false;
        } else {
            passwordEditText.setError(null);
        }

        return valid;
    }

    @Override
    public Context getContext() {
        return this;
    }
}
