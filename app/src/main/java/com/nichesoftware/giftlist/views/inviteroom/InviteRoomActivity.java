package com.nichesoftware.giftlist.views.inviteroom;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nichesoftware.giftlist.Injection;
import com.nichesoftware.giftlist.R;
import com.nichesoftware.giftlist.contracts.InviteRoomContract;
import com.nichesoftware.giftlist.model.Room;
import com.nichesoftware.giftlist.presenters.InviteRoomPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Invite room screen
 */
public class InviteRoomActivity extends AppCompatActivity implements InviteRoomContract.View {
    // Constants   ---------------------------------------------------------------------------------
    private static final String TAG = InviteRoomActivity.class.getSimpleName();
    public static final String EXTRA_ROOM = "ROOM";

    // Fields   ------------------------------------------------------------------------------------
    /**
     * Model
     */
    private Room room;

    /**
     * Unbinder Butter Knife
     */
    private Unbinder mButterKnifeUnbinder;

    /**
     * Listener sur les actions de l'utilisateur
     */
    private InviteRoomContract.UserActionListener actionsListener;

    /**
     * Graphical components
     */
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.invite_room_name_text_view)
    TextView mMessageTextView;
    @BindView(R.id.toolbar_progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.toolbar_progressBar_container)
    ViewGroup mProgressBarContainer;

    @OnClick(R.id.invite_room_button_accept)
    void onInviteButtonClick() {
        actionsListener.acceptInvitationToRoom(room.getId());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.invite_room_activity);
        mButterKnifeUnbinder = ButterKnife.bind(this);

        /**
         * Récupération de la salle
         */
        room = getIntent().getParcelableExtra(EXTRA_ROOM);

        actionsListener = new InviteRoomPresenter(this, Injection.getDataProvider(this));

        // Set up the toolbar.
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }

        mMessageTextView.setText(getString(R.string.invite_room_name_room_text, room.getRoomName()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mButterKnifeUnbinder.unbind();
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
        mProgressBar.animate();
        mProgressBarContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoader() {
        mProgressBarContainer.setVisibility(View.INVISIBLE);
    }

    @Override
    public Context getContext() {
        return this;
    }
}
