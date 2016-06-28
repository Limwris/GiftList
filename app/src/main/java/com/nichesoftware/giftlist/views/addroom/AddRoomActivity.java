package com.nichesoftware.giftlist.views.addroom;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.nichesoftware.giftlist.Injection;
import com.nichesoftware.giftlist.R;
import com.nichesoftware.giftlist.contracts.AddRoomContract;
import com.nichesoftware.giftlist.presenters.AddRoomPresenter;
import com.nichesoftware.giftlist.utils.StringUtils;

/**
 * Created by n_che on 27/04/2016.
 */
public class AddRoomActivity extends AppCompatActivity implements AddRoomContract.View {
    /**
     * Graphical components
     */
    private EditText roomNameEditText;
    private EditText occasionEditText;
    private AppCompatButton button;
    private ProgressDialog progressDialog;

    /*
     * Listener sur les actions de l'utilisateur
     */
    private AddRoomContract.UserActionListener actionsListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_room_activity);

        actionsListener = new AddRoomPresenter(this, Injection.getDataProvider(this));

        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            ActionBar ab = getSupportActionBar();
            if (ab != null) {
                ab.setDisplayHomeAsUpEnabled(true);
                ab.setHomeAsUpIndicator(R.drawable.ic_back_up_navigation);
            }
        }

        roomNameEditText = (EditText) findViewById(R.id.add_room_name_edit_text);
        occasionEditText = (EditText) findViewById(R.id.add_room_occasion_edit_text);
        button = (AppCompatButton) findViewById(R.id.add_room_create_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validate()) {
                    onCreateRoomFailed();
                    return;
                }
                final String roomName = roomNameEditText.getText().toString();
                final String occasion = occasionEditText.getText().toString();
                actionsListener.addRoom(roomName, occasion);
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        super.onDestroy();
    }

    private boolean validate() {
        boolean valid = true;

        final String roomName = roomNameEditText.getText().toString();
        if (StringUtils.isEmpty(roomName)) {
            roomNameEditText.setError(getString(R.string.add_room_empty_field_error_text));
            valid = false;
        } else {
            roomNameEditText.setError(null);
        }

        final String occasion = occasionEditText.getText().toString();
        if (StringUtils.isEmpty(occasion)) {
            occasionEditText.setError(getString(R.string.add_room_empty_field_error_text));
            valid = false;
        } else {
            occasionEditText.setError(null);
        }

        return valid;
    }

    /**********************************************************************************************/
    /************************************     View contract     ***********************************/
    /**********************************************************************************************/

    @Override
    public void onCreateRoomSuccess() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onCreateRoomFailed() {
        Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinatorLayout), "Failed", Snackbar.LENGTH_SHORT);
        snackbar.show();
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
}
