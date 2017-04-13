package com.nichesoftware.giftlist.views.addroom;

import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.nichesoftware.giftlist.Injection;
import com.nichesoftware.giftlist.R;
import com.nichesoftware.giftlist.contracts.AddRoomContract;
import com.nichesoftware.giftlist.database.DatabaseManager;
import com.nichesoftware.giftlist.model.User;
import com.nichesoftware.giftlist.presenters.AddRoomPresenter;
import com.nichesoftware.giftlist.repository.cache.RoomCache;
import com.nichesoftware.giftlist.repository.cache.UserCache;
import com.nichesoftware.giftlist.repository.datasource.AuthDataSource;
import com.nichesoftware.giftlist.repository.datasource.RoomCloudDataSource;
import com.nichesoftware.giftlist.repository.provider.AuthDataSourceProvider;
import com.nichesoftware.giftlist.session.SessionManager;
import com.nichesoftware.giftlist.utils.StringUtils;
import com.nichesoftware.giftlist.views.AuthenticationActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Add room screen
 */
public class AddRoomActivity extends AuthenticationActivity<AddRoomContract.Presenter>
        implements AddRoomContract.View {
    // Fields   ------------------------------------------------------------------------------------
    /**
     * Graphical components
     */
    @BindView(R.id.add_room_name_edit_text)
    EditText mRoomNameEditText;
    @BindView(R.id.add_room_occasion_edit_text)
    EditText mOccasionEditText;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout mCoordinatorLayout;

    @OnClick(R.id.add_room_create_button)
    void onAddRoomButtonClick() {
        if (!validate()) {
            onCreateRoomFailed();
            return;
        }
        final String roomName = mRoomNameEditText.getText().toString();
        final String occasion = mOccasionEditText.getText().toString();
        presenter.addRoom(roomName, occasion);
    }

    @Override
    protected void initView() {
        super.initView();
        // Set up the toolbar
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            ActionBar ab = getSupportActionBar();
            if (ab != null) {
                ab.setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle(getString(R.string.add_room_title));
                ab.setHomeAsUpIndicator(R.drawable.ic_back_up_navigation);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.standard_menu, menu);
        MenuItem item = menu.findItem(R.id.disconnection_menu_item);
        if (presenter.isConnected()) {
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
                presenter.doDisconnect();
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
    protected int getContentView() {
        return R.layout.add_room_activity;
    }

    @Override
    protected AddRoomContract.Presenter newPresenter() {
        final User user = SessionManager.getInstance().getConnectedUser();
        RoomCache cache = new RoomCache(DatabaseManager.getInstance(),
                user != null ? user.getName() : "");
        RoomCloudDataSource cloudDataSource = new RoomCloudDataSource(Injection.getService());
        UserCache userCache = new UserCache(DatabaseManager.getInstance());
        AuthDataSource authDataSource = new AuthDataSourceProvider(userCache, Injection.getService());
        return new AddRoomPresenter(this, cache, cloudDataSource, authDataSource);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///     Private methods
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private boolean validate() {
        boolean valid = true;

        final String roomName = mRoomNameEditText.getText().toString();
        if (StringUtils.isEmpty(roomName)) {
            mRoomNameEditText.setError(getString(R.string.add_room_empty_field_error_text));
            valid = false;
        } else {
            mRoomNameEditText.setError(null);
        }

        final String occasion = mOccasionEditText.getText().toString();
        if (StringUtils.isEmpty(occasion)) {
            mOccasionEditText.setError(getString(R.string.add_room_empty_field_error_text));
            valid = false;
        } else {
            mOccasionEditText.setError(null);
        }

        return valid;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///     View contract
    ////////////////////////////////////////////////////////////////////////////////////////////////

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
        mProgressBar.animate();
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoader() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showError(@NonNull String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    protected void performLogin() {
        // Todo
    }
}
