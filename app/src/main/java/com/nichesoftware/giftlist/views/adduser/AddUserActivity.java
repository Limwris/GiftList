package com.nichesoftware.giftlist.views.adduser;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.nichesoftware.giftlist.Injection;
import com.nichesoftware.giftlist.R;
import com.nichesoftware.giftlist.contracts.AddUserContract;
import com.nichesoftware.giftlist.database.DatabaseManager;
import com.nichesoftware.giftlist.model.User;
import com.nichesoftware.giftlist.presenters.AddUserPresenter;
import com.nichesoftware.giftlist.repository.cache.UserCache;
import com.nichesoftware.giftlist.repository.datasource.AuthDataSource;
import com.nichesoftware.giftlist.repository.provider.AuthDataSourceProvider;
import com.nichesoftware.giftlist.views.AuthenticationActivity;
import com.nichesoftware.giftlist.views.ErrorView;
import com.nichesoftware.giftlist.views.giftlist.GiftListActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Add user activity
 */
public class AddUserActivity extends AuthenticationActivity<AddUserContract.Presenter>
        implements AddUserContract.View {
    // Constants   ---------------------------------------------------------------------------------
    private static final String TAG = AddUserActivity.class.getSimpleName();
    public static final String EXTRA_ROOM_ID = "ROOM_ID";

    // Fields   ------------------------------------------------------------------------------------
    /**
     * Model
     */
    private String roomId;

    /**
     * Adapter lié à la RecyclerView
     */
    private ContactAdapter mContactsAdapter;

    /**
     * Graphical components
     */
    @BindView(R.id.add_user_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.toolbar_progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.error_view)
    ErrorView mErrorView;

    @Override
    protected void initView() {
        super.initView();

        // Récupération de l'identifiant de la salle
        roomId = getIntent().getStringExtra(EXTRA_ROOM_ID);

        // Set up the toolbar.
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            ActionBar ab = getSupportActionBar();
            if (ab != null) {
                ab.setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle(getString(R.string.add_user_title));
                ab.setHomeAsUpIndicator(R.drawable.ic_back_up_navigation);
            }
        }

        mContactsAdapter = new ContactAdapter(new ContactAdapter.ItemChangeListener() {
            @Override
            public void onItemChanged() {
                invalidateOptionsMenu();
            }
        });
        mRecyclerView.setAdapter(mContactsAdapter);
        int numColumns = getResources().getInteger(R.integer.num_contacts_columns);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, numColumns));

        presenter.loadContacts(roomId);
    }

    @Override
    protected int getContentView() {
        return R.layout.add_user_activity;
    }

    @Override
    protected AddUserContract.Presenter newPresenter() {
        UserCache userCache = new UserCache(DatabaseManager.getInstance());
        AuthDataSource authDataSource = new AuthDataSourceProvider(userCache, Injection.getService());
        return new AddUserPresenter(this, authDataSource, Injection.getService());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_user_menu, menu);
        MenuItem disconnectionItem = menu.findItem(R.id.disconnection_menu_item);
        MenuItem addItem = menu.findItem(R.id.add_user_menu_item);
        if (presenter.isConnected()) {
            disconnectionItem.setEnabled(true);
        } else {
            // disabled
            disconnectionItem.setEnabled(false);
        }
        // Disable addItem by default (no item has been checked)
        addItem.setEnabled(false);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem addItem = menu.findItem(R.id.add_user_menu_item);
        if (isItemChecked()) {
            addItem.setEnabled(true);
        } else {
            // disabled
            addItem.setEnabled(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.disconnection_menu_item:
                presenter.doDisconnect();
                return true;
            case R.id.add_user_menu_item:
                doInviteUsers();
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

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///     Private methods
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private void doInviteUsers() {
        List<String> usernames = new ArrayList<>();
        for (AddUserVO vo : mContactsAdapter.getData()) {
            if (vo.isChecked()) {
                usernames.add(vo.getUser().getName());
            }
        }
        presenter.inviteUsersToCurrentRoom(roomId, usernames);
    }

    private boolean isItemChecked() {
        for (AddUserVO vo : mContactsAdapter.getData()) {
            if (vo.isChecked()) {
                return true;
            }
        }
        return false;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///     View contract
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onUserAddedSuccess() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onUserAddedFailed() {
        setResult(RESULT_CANCELED);
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
    public void showContacts(List<User> users) {
        Log.d(TAG, "showContacts");

        if (users == null || users.isEmpty()) {
            mErrorView.setMessage(getResources().getString(R.string.add_user_error_view_message));
            mErrorView.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mErrorView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            List<AddUserVO> vos = new ArrayList<>();
            for (User user : users) {
                AddUserVO vo = new AddUserVO();
                vo.setUser(user);
                vos.add(vo);
            }
            mContactsAdapter.replaceData(vos);
        }
    }

    @Override
    protected void performLogin() {

    }
}
