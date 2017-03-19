package com.nichesoftware.giftlist.views.inviteroom;

import android.support.annotation.NonNull;
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
import com.nichesoftware.giftlist.views.AbstractActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Invite room screen
 */
public class InviteRoomActivity extends AbstractActivity<InviteRoomContract.Presenter>
        implements InviteRoomContract.View {
    // Constants   ---------------------------------------------------------------------------------
    private static final String TAG = InviteRoomActivity.class.getSimpleName();
    public static final String EXTRA_ROOM = "ROOM";

    // Fields   ------------------------------------------------------------------------------------
    /**
     * Model
     */
    private Room room;

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
        presenter.acceptInvitationToRoom(room.getId());
    }

    @Override
    protected void initView() {
        super.initView();

        /**
         * Récupération de la salle
         */
        room = getIntent().getParcelableExtra(EXTRA_ROOM);

        // Set up the toolbar.
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }

        mMessageTextView.setText(getString(R.string.invite_room_name_room_text, room.getRoomName()));
    }

    @Override
    protected int getContentView() {
        return R.layout.invite_room_activity;
    }

    @Override
    protected InviteRoomContract.Presenter newPresenter() {
        return new InviteRoomPresenter(this, Injection.getDataProvider());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///     View contract
    ////////////////////////////////////////////////////////////////////////////////////////////////

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
    public void showError(@NonNull String message) {

    }

    @Override
    protected void performLogin() {

    }
}
