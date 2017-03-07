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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Launch screen
 */
public class LaunchScreenActivity extends AppCompatActivity implements LaunchScreenContract.View {
    // Constants   ---------------------------------------------------------------------------------
    private static final String TAG = LaunchScreenActivity.class.getSimpleName();

    // Fields   ------------------------------------------------------------------------------------
    /**
     * Listener sur les actions de l'utilisateur
     */
    private LaunchScreenContract.UserActionListener actionsListener;

    /**
     * Unbinder Butter Knife
     */
    private Unbinder mButterKnifeUnbinder;

    /**
     * Graphical components
     */
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.didacticiel_pager)
    ViewPager mViewPager;
    @BindView(R.id.toolbar_progressBar)
    ProgressBar mProgressBar;

    @OnClick(R.id.didacticiel_not_authenticated_start)
    void onDisconnectedLaunchClick() {
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

    @OnClick(R.id.didacticiel_sign_up)
    void onSignUpClick() {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.sign_up_dialog, null);
        final TextInputEditText usernameEditText = (TextInputEditText) dialogView.findViewById(R.id.start_sign_up_dialog_username_edit_text);
        final TextInputEditText passwordEditText = (TextInputEditText) dialogView.findViewById(R.id.start_sign_up_dialog_password_edit_text);

        new AlertDialog.Builder(LaunchScreenActivity.this,
                R.style.AppTheme_Dark_Dialog)
                .setView(dialogView)
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

    @OnClick(R.id.didacticiel_log_in)
    void onLogInClick() {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launch_activity);
        mButterKnifeUnbinder = ButterKnife.bind(this);

        actionsListener = new LaunchScreenPresenter(this, Injection.getDataProvider(this));

        // Todo: régler problème de la Toolbar n'affichant plus la vue standard de l'action bar
        // Set up the toolbar.
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            ActionBar ab = getSupportActionBar();
            if (ab != null) {
                ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
                ab.setDisplayShowTitleEnabled(true);
            }
        }

        mViewPager.setAdapter(new DidacticielPagerAdapter(this));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mButterKnifeUnbinder.unbind();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                  Implement methods                                         //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void showRoomsActivity() {
            Log.d(TAG, "showRoomsActivity");

        Intent intent = new Intent(this, RoomsActivity.class);
        startActivity(intent);
        if (actionsListener.isConnected()) {
            finish();
        }
    }

    @Override
    public void showLoader() {
        mProgressBar.animate();
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoader() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public Context getContext() {
        return this;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                    Private methods                                         //
    ////////////////////////////////////////////////////////////////////////////////////////////////

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
}
