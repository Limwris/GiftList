package com.nichesoftware.giftlist.views.giftlist;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.nichesoftware.giftlist.Injection;
import com.nichesoftware.giftlist.BuildConfig;
import com.nichesoftware.giftlist.R;
import com.nichesoftware.giftlist.contracts.GiftListContract;
import com.nichesoftware.giftlist.model.Gift;
import com.nichesoftware.giftlist.presenters.GiftListPresenter;
import com.nichesoftware.giftlist.views.ErrorView;
import com.nichesoftware.giftlist.views.addgift.AddGiftActivity;
import com.nichesoftware.giftlist.views.giftdetail.GiftDetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by n_che on 25/04/2016.
 */
public class GiftListView extends FrameLayout implements GiftListContract.View {
    private static final String TAG = GiftListView.class.getSimpleName();


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

    public GiftListView(Context context) {
        super(context);
    }

    public GiftListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "GiftListView");
        }
        init(context);
    }

    public GiftListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GiftListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void compile(@Nullable final int roomId) {
        this.roomId = roomId;
        // Charge les cadeaux à l'ouverture de l'activité
        actionsListener.loadGifts(roomId, false);
    }
    /**
     * Initialisation de la vue
     * @param context
     */
    protected void init(Context context) {

        actionsListener = new GiftListPresenter(this, Injection.getDataProvider(getContext()));

        giftItemListener = new GiftItemListener() {
            @Override
            public void onGiftClick(Gift clickedGift) {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "Clic détecté sur le cadeau " + clickedGift.getName());
                }
                actionsListener.openGiftDetail(clickedGift);
            }
        };

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View root = inflater.inflate(R.layout.list_view, this, true);
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);
        giftListAdapter = new GiftListAdapter(new ArrayList<Gift>(0), giftItemListener);
        recyclerView.setAdapter(giftListAdapter);
        int numColumns = getContext().getResources().getInteger(R.integer.num_gifts_columns);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), numColumns));

        // Pull-to-refresh
        SwipeRefreshLayout swipeRefreshLayout =
                (SwipeRefreshLayout) root.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getContext(), R.color.colorPrimary),
                ContextCompat.getColor(getContext(), R.color.colorAccent),
                ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                actionsListener.loadGifts(roomId, true);
            }
        });
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        // Action sur le FAB -> La vue doit être attachée à la fenêtre pour être certain
        // que la vue ait accès à l'activité parente et que le FAB soit inflate correctement
        final Activity activity = getActivity();
        if (activity != null) {
            activity.findViewById(R.id.fab_add_gift).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (BuildConfig.DEBUG) {
                        Log.d(TAG, "onClick FAB");
                    }
                    Intent intent = new Intent(activity, AddGiftActivity.class);
                    intent.putExtra(AddGiftActivity.PARCELABLE_ROOM_ID_KEY, roomId);
                    activity.startActivityForResult(intent, GiftListActivity.ADD_GIFT_REQUEST);
                }
            });
        }
    }

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
//        ErrorView errorView = (ErrorView) findViewById(R.id.gifts_error_view);
//        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.gifts_list);
        ErrorView errorView = (ErrorView) findViewById(R.id.error_view);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        if (gifts.isEmpty()) {
            errorView.setMessage(getResources().getString(R.string.gift_error_view_message));
            errorView.setVisibility(VISIBLE);
            recyclerView.setVisibility(GONE);
        } else {
            errorView.setVisibility(GONE);
            recyclerView.setVisibility(VISIBLE);
            giftListAdapter.replaceData(gifts);
        }
    }

    @Override
    public void showGiftDetail(final Gift gift) {
        final Activity activity = getActivity();
        if (activity != null) {
            Intent intent = new Intent(activity, GiftDetailActivity.class);
            // Passing data as a parecelable object
            intent.putExtra(GiftDetailActivity.PARCELABLE_GIFT_KEY, gift);
            activity.startActivity(intent);
        }
    }

    @Override
    public void forceReload() {
        actionsListener.loadGifts(roomId, true);
    }


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

        public class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

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

    private Activity getActivity() {
        Context context = getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity)context;
            }
            context = ((ContextWrapper)context).getBaseContext();
        }
        return null;
    }
}
