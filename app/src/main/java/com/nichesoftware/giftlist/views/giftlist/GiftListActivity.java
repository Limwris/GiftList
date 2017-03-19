package com.nichesoftware.giftlist.views.giftlist;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.nichesoftware.giftlist.Injection;
import com.nichesoftware.giftlist.R;
import com.nichesoftware.giftlist.contracts.GiftListContract;
import com.nichesoftware.giftlist.model.Gift;
import com.nichesoftware.giftlist.presenters.GiftListPresenter;
import com.nichesoftware.giftlist.views.AbstractActivity;
import com.nichesoftware.giftlist.views.ErrorView;
import com.nichesoftware.giftlist.views.addgift.AddGiftActivity;
import com.nichesoftware.giftlist.views.adduser.AddUserActivity;
import com.nichesoftware.giftlist.views.giftdetail.GiftDetailActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Gift list activity
 */
public class GiftListActivity extends AbstractActivity<GiftListContract.Presenter>
        implements GiftListContract.View {
    // Constants   ---------------------------------------------------------------------------------
    private static final String TAG = GiftListActivity.class.getSimpleName();
    public static final String EXTRA_ROOM_ID = "ROOM_ID";
    public static final int RESULT_RELOAD = 100;    // The result codes
    public static final int ADD_GIFT_REQUEST = 10;  // The request codes
    public static final int ADD_USER_REQUEST = 11;
    public static final int GIFT_DETAIL_REQUEST = 12;

    // Fields   ------------------------------------------------------------------------------------
    /**
     * Adapter lié à la RecyclerView
     */
    private GiftListAdapter mGiftListAdapter;

    /**
     * Dialog
     */
    private DialogInterface mLeaveDialog = null;

    /**
     * Listener sur le clic d'un cadeau
     */
    private GiftItemListener mGiftItemListener;

    /**
     * Identifiant de la salle
     */
    private int roomId;

    /**
     * Graphical components
     */
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.error_view)
    ErrorView mErrorView;

    @OnClick(R.id.fab_add_gift)
    void onAddGiftClick() {
        Log.d(TAG, "onClick FAB");

        Intent intent = new Intent(this, AddGiftActivity.class);
        intent.putExtra(AddGiftActivity.PARCELABLE_ROOM_ID_KEY, roomId);
        startActivityForResult(intent, ADD_GIFT_REQUEST);
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
                getSupportActionBar().setTitle(getString(R.string.gift_list_title));
                ab.setHomeAsUpIndicator(R.drawable.ic_back_up_navigation);
            }
        }

        mGiftItemListener = new GiftItemListener() {
            @Override
            public void onGiftClick(Gift clickedGift) {
                Log.d(TAG, "Clic détecté sur le cadeau " + clickedGift.getName());
                presenter.openGiftDetail(clickedGift);
            }
        };

        mGiftListAdapter = new GiftListAdapter(new ArrayList<Gift>(0), mGiftItemListener);
        mRecyclerView.setAdapter(mGiftListAdapter);
        int numColumns = getResources().getInteger(R.integer.num_gifts_columns);

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
                presenter.loadGifts(roomId, true);
            }
        });

        // Get the requested note id
        int roomId = getIntent().getIntExtra(EXTRA_ROOM_ID, -1);
        initView(roomId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLeaveDialog != null) {
            mLeaveDialog.dismiss();
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.gift_list_activity;
    }

    @Override
    protected GiftListContract.Presenter newPresenter() {
        return new GiftListPresenter(this, Injection.getDataProvider());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        roomId = getIntent().getIntExtra(EXTRA_ROOM_ID, -1);
        // Check which request we're responding to
        if (requestCode == ADD_GIFT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                forceReload();
            }
        } else if (requestCode == ADD_USER_REQUEST) {
            if (resultCode == RESULT_OK) {
                // Do something ?
            }
        } else if (requestCode == GIFT_DETAIL_REQUEST) {
            if (resultCode == RESULT_OK) {
                forceReload();
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.giftlist_menu, menu);
        MenuItem invitationMenuItem = menu.findItem(R.id.gift_list_invite_user);
        if (presenter.isInvitationAvailable()) {
            invitationMenuItem.setEnabled(true);
            invitationMenuItem.getIcon().setAlpha(255);
        } else {
            // disabled
            invitationMenuItem.setEnabled(false);
            invitationMenuItem.getIcon().setAlpha(130);
        }

        MenuItem disconnectionMenuItem = menu.findItem(R.id.disconnection_menu_item);
        if (presenter.isConnected()) {
            disconnectionMenuItem.setEnabled(true);
        } else {
            // disabled
            disconnectionMenuItem.setEnabled(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.giftlist_delete_room:
                // Comportement du bouton "Supprimer une salle"
                doShowLeaveRoomDialog();
                return true;
            case R.id.gift_list_invite_user:
                // Comportement du bouton "Inviter un utilisateur"
                Intent intent = new Intent(this, AddUserActivity.class);
                intent.putExtra(AddUserActivity.EXTRA_ROOM_ID, roomId);
                startActivityForResult(intent, ADD_USER_REQUEST);
                return true;
            case R.id.disconnection_menu_item:
                presenter.doDisconnect();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///     Private methods
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Initialisation de la vue
     * @param roomId
     */
    private void initView(@Nullable final int roomId) {
        this.roomId = roomId;
        // Charge les cadeaux à l'ouverture de l'activité
        presenter.loadGifts(roomId, false);
    }

    private void doShowLeaveRoomDialog() {
        new AlertDialog.Builder(this,
                R.style.AppTheme_Dark_Dialog)
                .setMessage(R.string.leave_room_dialog_message)
                .setPositiveButton(R.string.ok_button_text,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int  whichButton) {
                                mLeaveDialog = dialogInterface;
                                presenter.leaveCurrentRoom(roomId);
                            }
                        })
                .setNegativeButton(R.string.cancel_button_text,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int  whichButton) {
                                if (dialogInterface != null) {
                                    dialogInterface.dismiss();
                                }
                            }
                        }).show();
    }

    private void forceReload() {
        presenter.loadGifts(roomId, true);
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

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///     View contract
    ////////////////////////////////////////////////////////////////////////////////////////////////


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

    }

    @Override
    public void showGifts(List<Gift> gifts) {
        if (gifts.isEmpty()) {
            mErrorView.setMessage(getResources().getString(R.string.gift_error_view_message));
            mErrorView.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mErrorView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mGiftListAdapter.replaceData(gifts);
        }
    }

    @Override
    public void showGiftDetail(final Gift gift) {
        Intent intent = new Intent(this, GiftDetailActivity.class);
        // Passing data as a parecelable object
        intent.putExtra(GiftDetailActivity.PARCELABLE_GIFT_KEY, gift);
        intent.putExtra(GiftDetailActivity.EXTRA_ROOM_ID, roomId);
        startActivityForResult(intent, GIFT_DETAIL_REQUEST);
    }

    @Override
    public void onLeaveRoomSuccess() {
        if (mLeaveDialog != null) {
            mLeaveDialog.dismiss();
        }
        mLeaveDialog = null;
        setResult(RESULT_RELOAD);
        finish();
    }

    @Override
    public void onLeaveRoomError() {
        if (mLeaveDialog != null) {
            mLeaveDialog.dismiss();
        }
        mLeaveDialog = null;
        // Todo
        Snackbar.make(findViewById(android.R.id.content), "Echec...", Snackbar.LENGTH_LONG).show();
    }

    @Override
    protected void performLogin() {

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                       Inner class                                          //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Interface du listener du clic sur un cadeau
     */
    public interface GiftItemListener {
        void onGiftClick(Gift clickedGift);
    }
}
