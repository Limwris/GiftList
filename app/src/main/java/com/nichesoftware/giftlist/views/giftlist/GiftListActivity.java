package com.nichesoftware.giftlist.views.giftlist;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nichesoftware.giftlist.BuildConfig;
import com.nichesoftware.giftlist.Injection;
import com.nichesoftware.giftlist.R;
import com.nichesoftware.giftlist.contracts.GiftListContract;
import com.nichesoftware.giftlist.model.Gift;
import com.nichesoftware.giftlist.presenters.GiftListPresenter;
import com.nichesoftware.giftlist.views.ErrorView;
import com.nichesoftware.giftlist.views.addgift.AddGiftActivity;
import com.nichesoftware.giftlist.views.adduser.AddUserActivity;
import com.nichesoftware.giftlist.views.giftdetail.GiftDetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by n_che on 25/04/2016.
 */
public class GiftListActivity extends AppCompatActivity implements GiftListContract.View {
    private static final String TAG = GiftListActivity.class.getSimpleName();
    public static final String EXTRA_ROOM_ID = "ROOM_ID";
    public static final int RESULT_RELOAD = 100;
    public static final int ADD_GIFT_REQUEST = 10;  // The request code
    public static final int ADD_USER_REQUEST = 11;

    /**
     * Adapter lié à la RecyclerView
     */
    private GiftListAdapter giftListAdapter;
    /**
     * Listener sur les actions de l'utilisateur
     */
    private GiftListContract.UserActionListener actionsListener;
    /**
     * Listener sur le clic d'un cadeau
     */
    private GiftItemListener giftItemListener;
    /**
     * Identifiant de la salle
     */
    private int roomId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.gift_list_activity);

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

        actionsListener = new GiftListPresenter(this, Injection.getDataProvider(this));

        findViewById(R.id.fab_add_gift).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "onClick FAB");
                }
                Intent intent = new Intent(GiftListActivity.this, AddGiftActivity.class);
                intent.putExtra(AddGiftActivity.PARCELABLE_ROOM_ID_KEY, roomId);
                startActivityForResult(intent, ADD_GIFT_REQUEST);
            }
        });

        giftItemListener = new GiftItemListener() {
            @Override
            public void onGiftClick(Gift clickedGift) {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "Clic détecté sur le cadeau " + clickedGift.getName());
                }
                actionsListener.openGiftDetail(clickedGift);
            }
        };

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        giftListAdapter = new GiftListAdapter(new ArrayList<Gift>(0), giftItemListener);
        recyclerView.setAdapter(giftListAdapter);
        int numColumns = getResources().getInteger(R.integer.num_gifts_columns);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, numColumns));

        // Pull-to-refresh
        SwipeRefreshLayout swipeRefreshLayout =
                (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(this, R.color.colorPrimary),
                ContextCompat.getColor(this, R.color.colorAccent),
                ContextCompat.getColor(this, R.color.colorPrimaryDark));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                actionsListener.loadGifts(roomId, true);
            }
        });

        // Get the requested note id
        int personId = getIntent().getIntExtra(EXTRA_ROOM_ID, 0); // Todo: valeur par défaut ?
        initView(personId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
        }
    }

    /**
     * Initialisation de la vue
     * @param roomId
     */
    private void initView(@Nullable final int roomId) {
        this.roomId = roomId;
        // Charge les cadeaux à l'ouverture de l'activité
        actionsListener.loadGifts(roomId, false);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.giftlist_menu, menu);
        MenuItem item = menu.findItem(R.id.gift_list_invite_user);
//        if (actionsListener.isInvitationAvailable()) {
//            item.setEnabled(true);
//            item.getIcon().setAlpha(255);
//        } else {
//            // disabled
//            item.setEnabled(false);
//            item.getIcon().setAlpha(130);
//        }
        return true;
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
                showLoader();
                Intent intent = new Intent(this, AddUserActivity.class);
                intent.putExtra(AddUserActivity.EXTRA_ROOM_ID, roomId);
                startActivityForResult(intent, ADD_USER_REQUEST);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void doShowLeaveRoomDialog() {
        new AlertDialog.Builder(this,
                R.style.AppTheme_Dark_Dialog)
                .setMessage(R.string.leave_room_dialog_message)
                .setPositiveButton(R.string.ok_button_text,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int  whichButton) {
                                actionsListener.leaveCurrentRoom(roomId);
                                if (dialogInterface != null) {
                                    dialogInterface.dismiss();
                                }
                                setResult(RESULT_RELOAD);
                                finish();
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

    private void setRefreshIndicator(final boolean doShow) {
        final SwipeRefreshLayout swipeRefreshLayout =
                (SwipeRefreshLayout) findViewById(R.id.refresh_layout);

        // Make sure setRefreshing() is called after the layout is done with everything else.
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(doShow);
            }
        });
    }

    @Override
    public void showGifts(List<Gift> gifts) {
        ErrorView errorView = (ErrorView) findViewById(R.id.error_view);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        if (gifts.isEmpty()) {
            errorView.setMessage(getResources().getString(R.string.gift_error_view_message));
            errorView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            errorView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            giftListAdapter.replaceData(gifts);
        }
    }

    @Override
    public void showGiftDetail(final Gift gift) {
        Intent intent = new Intent(this, GiftDetailActivity.class);
        // Passing data as a parecelable object
        intent.putExtra(GiftDetailActivity.PARCELABLE_GIFT_KEY, gift);
        startActivity(intent);
    }

    @Override
    public void forceReload() {
        actionsListener.loadGifts(roomId, true);
    }

    /**********************************************************************************************/
    /********************************     Adapter & ViewHolder     ********************************/
    /**********************************************************************************************/

    private static class GiftListAdapter extends RecyclerView.Adapter<GiftListAdapter.ViewHolder> {

        /**
         * Données (liste de cadeaux)
         */
        private List<Gift> gifts;

        /**
         * Listener sur le clic d'un cadeau
         */
        private GiftItemListener giftItemListener;

        /**
         * Context
         */
        private Context context;

        /**
         * Constructeur
         * @param gifts
         */
        public GiftListAdapter(List<Gift> gifts, GiftItemListener giftItemListener) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "GiftListAdapter");
            }
            setList(gifts);
            this.giftItemListener = giftItemListener;
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "GiftListAdapter - itemListener: " + giftItemListener);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            this.context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View noteView = inflater.inflate(R.layout.gift_list_item_view, parent, false);

            return new ViewHolder(noteView, giftItemListener);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            Gift gift = gifts.get(position);

            viewHolder.name.setText(gift.getName());
            viewHolder.amount.setText(String.format(context.getResources().getString(R.string.gift_description), gift.getPrice()));
        }

        public void replaceData(List<Gift> gifts) {
            setList(gifts);
            notifyDataSetChanged();
        }

        private void setList(List<Gift> gifts) {
            this.gifts = gifts;
        }

        @Override
        public int getItemCount() {
            return gifts.size();
        }

        public Gift getItem(int position) {
            return gifts.get(position);
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            /**
             * Nom du cadeau
             */
            public TextView name;

            /**
             * Montant du cadeau
             */
            public TextView amount;

            /**
             * Listener sur le clic de la personne
             */
            private GiftItemListener giftItemListener;


            /**
             * Constructeur
             * @param itemView
             */
            public ViewHolder(View itemView, GiftItemListener listener) {
                super(itemView);
                giftItemListener = listener;

                name = (TextView) itemView.findViewById(R.id.gift_name);
                amount = (TextView) itemView.findViewById(R.id.gift_amount);
                itemView.findViewById(R.id.mainHolder).setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "Clic détecté dans la liste à la position: " + position);
                }
                Gift gift = getItem(position);
                if (giftItemListener != null) {
                    giftItemListener.onGiftClick(gift);
                }
            }
        }
    }

    /**
     * Interface du listener du clic sur un cadeau
     */
    public interface GiftItemListener {
        void onGiftClick(Gift clickedGift);
    }
}
