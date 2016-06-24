package com.nichesoftware.giftlist.views.start;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.nichesoftware.giftlist.BuildConfig;
import com.nichesoftware.giftlist.Injection;
import com.nichesoftware.giftlist.R;
import com.nichesoftware.giftlist.contracts.StartContract;
import com.nichesoftware.giftlist.presenters.StartPresenter;
import com.nichesoftware.giftlist.utils.StringUtils;
import com.nichesoftware.giftlist.views.rooms.RoomsActivity;

/**
 * Created by n_che on 08/06/2016.
 */
public class StartActivity extends AppCompatActivity implements StartContract.View {
    private static final String TAG = StartActivity.class.getSimpleName();

    /**
     * Graphical components
     */
    private ProgressDialog progressDialog;

    /**
     * Listener sur les actions de l'utilisateur
     */
    private StartContract.UserActionListener actionsListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);

        actionsListener = new StartPresenter(this, Injection.getDataProvider(this));

        ViewPager viewPager = (ViewPager) findViewById(R.id.didacticiel_pager);
        viewPager.setAdapter(new DidacticielPagerAdapter(this));

        findViewById(R.id.didacticiel_sign_up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = getLayoutInflater();
                View dialoglayout = inflater.inflate(R.layout.start_sign_up_dialog, null);
                final TextInputEditText usernameEditText = (TextInputEditText) dialoglayout.findViewById(R.id.start_sign_up_dialog_username_edit_text);
                final TextInputEditText passwordEditText = (TextInputEditText) dialoglayout.findViewById(R.id.start_sign_up_dialog_password_edit_text);

                new AlertDialog.Builder(StartActivity.this,
                        R.style.AppTheme_Dark_Dialog)
                        .setView(dialoglayout)
                        .setPositiveButton(R.string.start_dialog_positive_button,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int whichButton) {
                                        if (validate(usernameEditText, passwordEditText)) {
                                            actionsListener.startApplication(
                                                    usernameEditText.getText().toString(),
                                                    passwordEditText.getText().toString());
                                        }
                                        if (dialogInterface != null) {
                                            dialogInterface.dismiss();
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
                LayoutInflater inflater = getLayoutInflater();
                View dialoglayout = inflater.inflate(R.layout.start_log_in_dialog, null);
                final TextInputEditText usernameEditText = (TextInputEditText) dialoglayout.findViewById(R.id.start_log_in_dialog_username_edit_text);
                final TextInputEditText passwordEditText = (TextInputEditText) dialoglayout.findViewById(R.id.start_log_in_dialog_password_edit_text);

                new AlertDialog.Builder(StartActivity.this,
                        R.style.AppTheme_Dark_Dialog)
                        .setView(dialoglayout)
                        .setPositiveButton(R.string.start_dialog_positive_button,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int whichButton) {
                                        if (validate(usernameEditText, passwordEditText)) {
                                            actionsListener.startApplication(
                                                    usernameEditText.getText().toString(),
                                                    passwordEditText.getText().toString());
                                        }
                                        if (dialogInterface != null) {
                                            dialogInterface.dismiss();
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

        findViewById(R.id.didacticiel_not_authenticated_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(StartActivity.this,
                        R.style.AppTheme_Dark_Dialog)
                        .setMessage(R.string.not_authenticated_start_message)
                        .setPositiveButton(R.string.not_authenticated_start_positive_button,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int  whichButton) {
                                        actionsListener.startApplication(null, null);
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
    public void showRoomsActivity() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "showRoomsActivity");
        }
        Intent intent = new Intent(this, RoomsActivity.class);
        startActivity(intent);
    }

    @Override
    public void showLoader(final String message) {
        progressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    @Override
    public void hideLoader() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
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
}
