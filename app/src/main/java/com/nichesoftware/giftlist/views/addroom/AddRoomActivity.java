package com.nichesoftware.giftlist.views.addroom;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

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
                getSupportActionBar().setTitle(getString(R.string.add_room_title));
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.standard_menu, menu);
        MenuItem item = menu.findItem(R.id.disconnection_menu_item);
        if (actionsListener.isConnected()) {
            item.setEnabled(true);
        } else {
            // disabled
            item.setEnabled(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.disconnection_menu_item:
                actionsListener.doDisconnect();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onDestroy() {
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
        // Todo
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Failed", Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    @Override
    public void showLoader() {
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.toolbar_progressBar);
        progressBar.animate();
        findViewById(R.id.toolbar_progressBar).setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoader() {
        findViewById(R.id.toolbar_progressBar).setVisibility(View.INVISIBLE);
    }

    @Override
    public Context getContext() {
        return this;
    }
}
