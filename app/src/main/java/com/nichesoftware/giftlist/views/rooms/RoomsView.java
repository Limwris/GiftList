package com.nichesoftware.giftlist.views.rooms;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
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

import com.nichesoftware.giftlist.BuildConfig;
import com.nichesoftware.giftlist.Injection;
import com.nichesoftware.giftlist.R;
import com.nichesoftware.giftlist.contracts.RoomsContract;
import com.nichesoftware.giftlist.model.Room;
import com.nichesoftware.giftlist.presenters.RoomsPresenter;
import com.nichesoftware.giftlist.views.ErrorView;
import com.nichesoftware.giftlist.views.addroom.AddRoomActivity;
import com.nichesoftware.giftlist.views.giftlist.GiftListActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by n_che on 25/04/2016.
 */
public class RoomsView extends FrameLayout implements RoomsContract.View {
    private static final String TAG = RoomsView.class.getSimpleName();

    /**
     * Adapter lié à la RecyclerView
     */
    private RoomsAdapter roomsAdapter;
    /**
     * Listener sur les actions de l'utilisateur
     */
    private RoomsContract.UserActionListener actionsListener;
    /**
     * Listener for clicks on person in the RecyclerView.
     */
    private RoomItemListener itemListener;

    public RoomsView(Context context) {
        super(context);
    }

    public RoomsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "RoomsView");
        }
        init(context);
    }

    public RoomsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RoomsView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * Initialisation de la vue
     * @param context
     */
    protected void init(Context context) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "init");
        }

        actionsListener = new RoomsPresenter(this, Injection.getDataProvider(getContext()));

        itemListener = new RoomItemListener() {
            @Override
            public void onRoomClick(Room clickedRoom) {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "Clic détecté sur la salle " + clickedRoom.getRoomName());
                }
                actionsListener.openRoomDetail(clickedRoom);
            }
        };

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View root = inflater.inflate(R.layout.list_view, this, true);
//        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.persons_list);
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);
        roomsAdapter = new RoomsAdapter(new ArrayList<Room>(0), itemListener);
        recyclerView.setAdapter(roomsAdapter);
        int numColumns = getContext().getResources().getInteger(R.integer.num_persons_columns);

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
                actionsListener.loadRooms(true);
            }
        });

        // Charge les salles à l'ouverture de l'activité
        actionsListener.loadRooms(false);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        // Action sur le FAB -> La vue doit être attachée à la fenêtre pour être certain
        // que la vue ait accès à l'activité parente et que le FAB soit inflate correctement
        final Activity activity = getActivity();
        if (activity != null) {
            activity.findViewById(R.id.fab_add_room).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (BuildConfig.DEBUG) {
                        Log.d(TAG, "onClick FAB");
                    }
                    Intent intent = new Intent(activity, AddRoomActivity.class);
                    activity.startActivityForResult(intent, RoomsActivity.ADD_ROOM_REQUEST);
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
    public void showRooms(List<Room> rooms) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "showRooms");
        }

        ErrorView errorView = (ErrorView) findViewById(R.id.error_view);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        if (rooms.isEmpty()) {
            errorView.setMessage(getResources().getString(R.string.room_error_view_message));
            errorView.setVisibility(VISIBLE);
            recyclerView.setVisibility(GONE);
        } else {
            errorView.setVisibility(GONE);
            recyclerView.setVisibility(VISIBLE);
            roomsAdapter.replaceData(rooms);
        }
    }

    @Override
    public void showRoomDetail(@NonNull int roomId) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "showRoomDetail");
        }

        Intent intent = new Intent(getContext(), GiftListActivity.class);
        intent.putExtra(GiftListActivity.EXTRA_ROOM_ID, roomId);
        getContext().startActivity(intent);
    }

    @Override
    public void forceReload() {
        actionsListener.loadRooms(false);
    }

    private static class RoomsAdapter extends RecyclerView.Adapter<RoomsAdapter.ViewHolder> {

        /**
         * Données (liste de salles)
         */
        private List<Room> rooms;

        /**
         * Listener sur le clic de la salle
         */
        private RoomItemListener itemListener;

        /**
         * Context
         */
        private Context context;

        /**
         * Constructeur
         * @param rooms
         * @param itemListener
         */
        public RoomsAdapter(List<Room> rooms, RoomItemListener itemListener) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "RoomsAdapter");
            }
            setList(rooms);
            this.itemListener = itemListener;
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "RoomsAdapter - itemListener: " + itemListener);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "RoomsAdapter - onCreateViewHolder");
            }

            this.context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View roomsView = inflater.inflate(R.layout.room_list_item_view, parent, false);

            return new ViewHolder(roomsView, itemListener);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "RoomsAdapter - onBindViewHolder");
            }

            Room room = rooms.get(position);

            viewHolder.name.setText(room.getRoomName());
            // The first parameter of getQuantityString is used to decide which format to use (single or plural)
            viewHolder.giftDescription.setText(context.getResources().getQuantityString(R.plurals.gift_description, 0, 0, room.getGiftListSize()));
        }

        public void replaceData(List<Room> rooms) {
            setList(rooms);
            notifyDataSetChanged();
        }

        private void setList(List<Room> rooms) {
            this.rooms = rooms;
        }

        @Override
        public int getItemCount() {
            return rooms.size();
        }

        public Room getItem(int position) {
            return rooms.get(position);
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements android.view.View.OnClickListener {

            /**
             * Nom
             */
            public TextView name;

            /**
             * Description des cadeaux en cours
             */
            public TextView giftDescription;

            /**
             * Listener sur le clic de la salle
             */
            private RoomItemListener roomItemListener;

            /**
             * Constructeur
             * @param itemView vue d'un item de la liste
             * @param listener listener sur le clic d'un item de la liste
             */
            public ViewHolder(View itemView, RoomItemListener listener) {
                super(itemView);
                roomItemListener = listener;
                name = (TextView) itemView.findViewById(R.id.person_name);
                giftDescription = (TextView) itemView.findViewById(R.id.person_gift_description);
                itemView.findViewById(R.id.mainHolder).setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "Clic détecté dans la liste à la position: " + position);
                }
                Room room = getItem(position);
                if (roomItemListener != null) {
                    roomItemListener.onRoomClick(room);
                }

            }
        }
    }

    /**
     * Interface du listener du clic sur une salle
     */
    public interface RoomItemListener {
        void onRoomClick(Room clickedRoom);
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
