package com.nichesoftware.giftlist.views.inviteroom;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
    public static final String EXTRA_ROOM_ID = "ROOM_ID";
    public static final String EXTRA_TOKEN = "TOKEN";

    /**
     * Model
     */
    private int roomId;
    private String token;
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
         * Récupération de l'identifiant de la salle
         */
        roomId = getIntent().getIntExtra(EXTRA_ROOM_ID, -1);
        token = getIntent().getStringExtra(EXTRA_TOKEN);

        actionsListener = new InviteRoomPresenter(this, Injection.getDataProvider(this));

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

        actionsListener.getRoomInformation(roomId);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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
    public void onRoomInformationSuccess(Room room) {
        TextView message = (TextView) findViewById(R.id.invite_room_name_text_view);
        message.setText(getString(R.string.invite_room_name_room_text, room.getRoomName()));

        Button button = (Button) findViewById(R.id.invite_room_button_accept);
        button.setEnabled(true);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionsListener.acceptInvitationToRoom(roomId, token);
            }
        });
    }

    @Override
    public void onRoomInformationFailed() {
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
}
