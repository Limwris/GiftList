package com.nichesoftware.giftlist.views.rooms;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.nichesoftware.giftlist.Injection;
import com.nichesoftware.giftlist.R;
import com.nichesoftware.giftlist.contracts.RoomsContract;
import com.nichesoftware.giftlist.database.DatabaseManager;
import com.nichesoftware.giftlist.model.User;
import com.nichesoftware.giftlist.presenters.RoomsPresenter;
import com.nichesoftware.giftlist.repository.cache.RoomCache;
import com.nichesoftware.giftlist.repository.cache.UserCache;
import com.nichesoftware.giftlist.repository.datasource.AuthDataSource;
import com.nichesoftware.giftlist.repository.datasource.RoomCloudDataSource;
import com.nichesoftware.giftlist.repository.provider.AuthDataSourceProvider;
import com.nichesoftware.giftlist.session.SessionManager;
import com.nichesoftware.giftlist.views.AuthenticationActivity;
import com.nichesoftware.giftlist.views.ErrorView;
import com.nichesoftware.giftlist.views.addroom.AddRoomActivity;
import com.nichesoftware.giftlist.views.giftlist.GiftListActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Room screen
 */
public class RoomsActivity extends AuthenticationActivity<RoomsContract.Presenter>
        implements RoomsContract.View {
    // Constants   ---------------------------------------------------------------------------------
    private static final String TAG = RoomsActivity.class.getSimpleName();
    public static final int ADD_ROOM_REQUEST = 20;  // The request code
    public static final int ROOM_DETAIL_REQUEST = 21;

    // Fields   ------------------------------------------------------------------------------------
    /**
     * Adapter lié à la RecyclerView
     */
    private RoomAdapter mRoomsAdapter;

    /**
     * Graphical components
     */
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.error_view)
    ErrorView mErrorView;

    @OnClick(R.id.fab_add_room)
    void onAddRoomClick() {
        Log.d(TAG, "onClick FAB");
        Intent intent = new Intent(this, AddRoomActivity.class);
        startActivityForResult(intent, RoomsActivity.ADD_ROOM_REQUEST);
    }


    @Override
    protected void initView() {
        super.initView();

        // Set up the toolbar
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            ActionBar ab = getSupportActionBar();
            if (ab != null) {
                ab.setDisplayHomeAsUpEnabled(false);
            }
        }

        RoomItemListener itemListener = new RoomItemListener() {
            @Override
            public void onRoomClick(String clickedRoomId) {
                Log.d(TAG, "Clic détecté sur la salle " + clickedRoomId);
                presenter.openRoomDetail(clickedRoomId);
            }
        };

        mRoomsAdapter = new RoomAdapter(itemListener);
        mRecyclerView.setAdapter(mRoomsAdapter);
        int numColumns = getResources().getInteger(R.integer.num_persons_columns);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, numColumns));

        // Pull-to-refresh
        mSwipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(this, R.color.colorPrimary),
                ContextCompat.getColor(this, R.color.colorAccent),
                ContextCompat.getColor(this, R.color.colorPrimaryDark));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.loadRooms(true);
            }
        });

        // Charge les salles à l'ouverture de l'activité
        presenter.loadRooms(false);
    }

    @Override
    protected int getContentView() {
        return R.layout.rooms_activity;
    }

    @Override
    protected RoomsContract.Presenter newPresenter() {
        final User user = SessionManager.getInstance().getConnectedUser();
        RoomCache cache = new RoomCache(DatabaseManager.getInstance(),
                user != null ? user.getUsername() : "");
        RoomCloudDataSource cloudDataSource = new RoomCloudDataSource(SessionManager.getInstance().getToken(),
                Injection.getService());
        UserCache userCache = new UserCache(DatabaseManager.getInstance());
        AuthDataSource authDataSource = new AuthDataSourceProvider(userCache, Injection.getService());
        return new RoomsPresenter(this, cache, cloudDataSource, authDataSource);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == ADD_ROOM_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                presenter.loadRooms(false);
            }
        } else if (requestCode == ROOM_DETAIL_REQUEST) {
            if (resultCode == GiftListActivity.RESULT_RELOAD) {
                presenter.loadRooms(false);
            }
        }
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

    /**********************************************************************************************/
    /************************************     View contract     ***********************************/
    /**********************************************************************************************/

    @Override
    public void showLoader() {
        setRefreshIndicator(true);
    }

    @Override
    public void hideLoader() {
        setRefreshIndicator(false);
    }

    @Override
    public void showError(@NonNull String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    private void setRefreshIndicator(final boolean doShow) {
        // Make sure setRefreshing() is called after the layout is done with everything else.
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(doShow);
            }
        });
    }

    @Override
    public void showRooms(List<RoomVO> rooms) {
        Log.d(TAG, "showRooms");

        if (rooms.isEmpty()) {
            mErrorView.setMessage(getResources().getString(R.string.room_error_view_message));
            mErrorView.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mErrorView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mRoomsAdapter.replaceData(rooms);
        }
    }

    @Override
    public void showRoomDetail(@NonNull String roomId) {
        Log.d(TAG, "showRoomDetail");

        Intent intent = new Intent(this, GiftListActivity.class);
        intent.putExtra(GiftListActivity.EXTRA_ROOM_ID, roomId);
        startActivityForResult(intent, ROOM_DETAIL_REQUEST);
    }

    @Override
    protected void performLogin() {

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                       Inner class                                          //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Interface du listener du clic sur une salle
     */
    public interface RoomItemListener {
        void onRoomClick(String clickedRoomId);
    }
}
