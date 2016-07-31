package com.nichesoftware.giftlist.views.giftlist;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by n_che on 25/04/2016.
 */
public class GiftListActivity extends AppCompatActivity implements GiftListContract.View {
    private static final String TAG = GiftListActivity.class.getSimpleName();
    public static final String EXTRA_ROOM_ID = "ROOM_ID";
    public static final int RESULT_RELOAD = 100;    // The result codes
    public static final int ADD_GIFT_REQUEST = 10;  // The request codes
    public static final int ADD_USER_REQUEST = 11;
    public static final int GIFT_DETAIL_REQUEST = 12;

    /**
     * Adapter lié à la RecyclerView
     */
    private GiftListAdapter giftListAdapter;
    private DialogInterface leaveDialog = null;
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
                getSupportActionBar().setTitle(getString(R.string.gift_list_title));
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
        int roomId = getIntent().getIntExtra(EXTRA_ROOM_ID, -1);
        initView(roomId);
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
        MenuItem invitationMenuItem = menu.findItem(R.id.gift_list_invite_user);
        if (actionsListener.isInvitationAvailable()) {
            invitationMenuItem.setEnabled(true);
            invitationMenuItem.getIcon().setAlpha(255);
        } else {
            // disabled
            invitationMenuItem.setEnabled(false);
            invitationMenuItem.getIcon().setAlpha(130);
        }

        MenuItem disconnectionMenuItem = menu.findItem(R.id.disconnection_menu_item);
        if (actionsListener.isConnected()) {
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
                actionsListener.doDisconnect();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void doShowLeaveRoomDialog() {
        new AlertDialog.Builder(this,
                R.style.AppTheme_Dark_Dialog)
                .setMessage(R.string.leave_room_dialog_message)
                .setPositiveButton(R.string.ok_button_text,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int  whichButton) {
                                leaveDialog = dialogInterface;
                                actionsListener.leaveCurrentRoom(roomId);
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
        actionsListener.loadGifts(roomId, true);
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
        intent.putExtra(GiftDetailActivity.EXTRA_ROOM_ID, roomId);
        startActivityForResult(intent, GIFT_DETAIL_REQUEST);
    }

    @Override
    public void onLeaveRoomSuccess() {
        if (leaveDialog != null) {
            leaveDialog.dismiss();
        }
        leaveDialog = null;
        setResult(RESULT_RELOAD);
        finish();
    }

    @Override
    public void onLeaveRoomError() {
        if (leaveDialog != null) {
            leaveDialog.dismiss();
        }
        leaveDialog = null;
        // Todo
        Snackbar.make(findViewById(android.R.id.content), "Echec...", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public Context getContext() {
        return this;
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
         * Position de l'élément où sont affichées les informations supplémentaires
         */
        private int expandedPosition;

        /**
         * Constructeur
         * @param gifts
         */
        public GiftListAdapter(List<Gift> gifts, GiftItemListener giftItemListener) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "GiftListAdapter");
            }
            expandedPosition = -1;
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
            View giftView = inflater.inflate(R.layout.gift_list_item_view, parent, false);

            return new ViewHolder(giftView, giftItemListener);
        }

        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, int position) {
            Gift gift = gifts.get(position);
            final List<GiftListDetailVO> VOs = new ArrayList<>();
            double remainder = gift.getPrice();
            for (Map.Entry<String, Double> entry : gift.getAmountByUser().entrySet()) {
                GiftListDetailVO vo = new GiftListDetailVO();
                vo.setUsername(entry.getKey());
                double participation = entry.getValue();
                vo.setParticipation(participation);
                VOs.add(vo);

                remainder-=participation;
            }

            final boolean isExpanded = (viewHolder.getAdapterPosition()==expandedPosition);
            if (!isExpanded) {
                viewHolder.detailRecyclerView.setVisibility(View.GONE);
            }

            viewHolder.name.setText(gift.getName());
            viewHolder.price.setText(String.format(context.getResources().getString(R.string.gift_price_description), gift.getPrice()));
            viewHolder.amount.setText(String.format(context.getResources().getString(R.string.gift_amount_description), gift.getAmount()));
            viewHolder.remainder.setText(String.format(context.getResources().getString(R.string.gift_remainder_description), remainder));
            viewHolder.seeMoreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewHolder.detailRecyclerView.setAdapter(new GiftListDetailAdapter(VOs));
                    viewHolder.detailRecyclerView.setVisibility(View.VISIBLE);
                    expandedPosition = isExpanded ? -1:viewHolder.getAdapterPosition();
                    TransitionManager.beginDelayedTransition(viewHolder.detailRecyclerView);
                    notifyDataSetChanged();
                }
            });

            Picasso.with(context)
                    .load(Injection.getDataProvider(context).getGiftImageUrl(gift.getId()))
                    .fit().centerCrop().placeholder(R.drawable.placeholder)
                    .into(viewHolder.image);
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

        public class ViewHolder extends RecyclerView.ViewHolder {
            /**
             * Nom du cadeau
             */
            public TextView name;

            /**
             * Participation du cadeau
             */
            public TextView amount;

            /**
             * Restant au cadeau
             */
            public TextView remainder;

            /**
             * Prix du cadeau
             */
            public TextView price;

            /**
             * Image associée au cadeau
             */
            public ImageView image;

            public RecyclerView detailRecyclerView;
            public Button seeMoreButton;

            /**
             * Listener sur le clic de la personne
             */
            private GiftItemListener giftItemListener;


            /**
             * Constructeur
             * @param itemView
             */
            public ViewHolder(final View itemView, GiftItemListener listener) {
                super(itemView);
                giftItemListener = listener;

                name = (TextView) itemView.findViewById(R.id.gift_name);
                price = (TextView) itemView.findViewById(R.id.gift_price);
                amount = (TextView) itemView.findViewById(R.id.gift_amount);
                remainder = (TextView) itemView.findViewById(R.id.gift_remainder);
                image = (ImageView) itemView.findViewById(R.id.gift_image);
                seeMoreButton = (Button) itemView.findViewById(R.id.gift_list_see_more_button);

                detailRecyclerView = (RecyclerView) itemView.findViewById(R.id.gift_list_details);
                detailRecyclerView.setHasFixedSize(true);
                detailRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

                itemView.findViewById(R.id.gift_list_detail_button).setOnClickListener(new View.OnClickListener() {
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
                });

            }
        }
    }

    /**
     * Interface du listener du clic sur un cadeau
     */
    public interface GiftItemListener {
        void onGiftClick(Gift clickedGift);
    }

    private static class GiftListDetailAdapter extends RecyclerView.Adapter<GiftListDetailAdapter.ViewHolder> {

        /**
         * Données (liste de cadeaux)
         */
        private List<GiftListDetailVO> VOs;

        /**
         * Context
         */
        private Context context;

        /**
         * Constructeur
         * @param VOs
         */
        public GiftListDetailAdapter(List<GiftListDetailVO> VOs) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "GiftListDetailAdapter");
            }
            this.VOs = VOs;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            this.context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View giftDetailView = inflater.inflate(R.layout.gift_list_detail_item_view, parent, false);

            return new ViewHolder(giftDetailView);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            GiftListDetailVO vo = VOs.get(position);

            viewHolder.name.setText(vo.getUsername());
            viewHolder.amount.setText(String.format(context.getResources().getString(R.string.gift_list_detail_amount_description), vo.getParticipation()));
        }

        @Override
        public int getItemCount() {
            return VOs.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            /**
             * Nom du cadeau
             */
            public TextView name;

            /**
             * Participation du cadeau
             */
            public TextView amount;


            /**
             * Constructeur
             * @param itemView
             */
            public ViewHolder(View itemView) {
                super(itemView);

                name = (TextView) itemView.findViewById(R.id.gift_list_detail_username);
                amount = (TextView) itemView.findViewById(R.id.gift_list_detail_participation);
            }
        }
    }
}
