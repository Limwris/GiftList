package com.nichesoftware.giftlist.views.inviteroom;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nichesoftware.giftlist.Injection;
import com.nichesoftware.giftlist.R;
import com.nichesoftware.giftlist.contracts.InviteRoomContract;
import com.nichesoftware.giftlist.database.DatabaseManager;
import com.nichesoftware.giftlist.model.Room;
import com.nichesoftware.giftlist.model.User;
import com.nichesoftware.giftlist.presenters.InviteRoomPresenter;
import com.nichesoftware.giftlist.repository.cache.RoomCache;
import com.nichesoftware.giftlist.repository.cache.UserCache;
import com.nichesoftware.giftlist.repository.datasource.AuthDataSource;
import com.nichesoftware.giftlist.repository.datasource.RoomCloudDataSource;
import com.nichesoftware.giftlist.repository.provider.AuthDataSourceProvider;
import com.nichesoftware.giftlist.session.SessionManager;
import com.nichesoftware.giftlist.views.AuthenticationActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Invite room screen
 */
public class InviteRoomActivity extends AuthenticationActivity<InviteRoomContract.Presenter>
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
    @BindView(R.id.invite_room_content)
    ViewGroup mContainer;
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
        final User user = SessionManager.getInstance().getConnectedUser();
        RoomCache cache = new RoomCache(DatabaseManager.getInstance(),
                user != null ? user.getUsername() : "");
        RoomCloudDataSource cloudDataSource = new RoomCloudDataSource(Injection.getService());
        UserCache userCache = new UserCache(DatabaseManager.getInstance());
        AuthDataSource authDataSource = new AuthDataSourceProvider(userCache, Injection.getService());
        return new InviteRoomPresenter(this, cache, cloudDataSource, authDataSource);
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
        Snackbar.make(mContainer, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    protected void performLogin() {

    }
}
