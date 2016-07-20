package com.nichesoftware.giftlist.views.inviteroom;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nichesoftware.giftlist.Injection;
import com.nichesoftware.giftlist.R;
import com.nichesoftware.giftlist.contracts.InviteRoomContract;
import com.nichesoftware.giftlist.model.Room;
import com.nichesoftware.giftlist.presenters.InviteRoomPresenter;

/**
 * Created by n_che on 27/06/2016.
 */
public class InviteRoomActivity extends AppCompatActivity implements InviteRoomContract.View {
    private static final String TAG = InviteRoomActivity.class.getSimpleName();
    public static final String EXTRA_ROOM = "ROOM";

    /**
     * Model
     */
    private Room room;

    /**
     * Graphical components
     */
    private ProgressDialog progressDialog;

    /**
     * Listener sur les actions de l'utilisateur
     */
    private InviteRoomContract.UserActionListener actionsListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.invite_room_activity);

        /**
         * Récupération de la salle
         */
        room = getIntent().getParcelableExtra(EXTRA_ROOM);

        actionsListener = new InviteRoomPresenter(this, Injection.getDataProvider(this));

        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        TextView message = (TextView) findViewById(R.id.invite_room_name_text_view);
        message.setText(getString(R.string.invite_room_name_room_text, room.getRoomName()));

        Button button = (Button) findViewById(R.id.invite_room_button_accept);
        button.setEnabled(true);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionsListener.acceptInvitationToRoom(room.getId());
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

    /**********************************************************************************************/
    /************************************     View contract     ***********************************/
    /**********************************************************************************************/

    @Override
    public void onAcceptInvitationSuccess() {
        finish();
    }

    @Override
    public void onAcceptInvitationFailed() {
        // Todo
    }

    @Override
    public void showLoader() {
        progressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        // Todo
        progressDialog.setMessage(getResources().getString(R.string.invite_room_loader_message));
        progressDialog.show();
    }

    @Override
    public void hideLoader() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public Context getContext() {
        return this;
    }
}
