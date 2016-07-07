package com.nichesoftware.giftlist.views.rooms;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
public class RoomsActivity extends AppCompatActivity implements RoomsContract.View {
    private static final String TAG = RoomsActivity.class.getSimpleName();
    public static final int ADD_ROOM_REQUEST = 20;  // The request code
    public static final int ROOM_DETAIL_REQUEST = 21;

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
    /**
     * Graphical components
     */
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onCreate");
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.rooms_activity);

        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            ActionBar ab = getSupportActionBar();
            // Providing Up Navigation (source: http://developer.android.com/intl/ru/training/implementing-navigation/ancestral.html)
            if (ab != null) {
                ab.setDisplayHomeAsUpEnabled(true);
                ab.setHomeAsUpIndicator(R.drawable.ic_menu);
            }
        }

        // Set up the navigation drawer.
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        findViewById(R.id.fab_add_room).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BuildConfig.DEBUG) {Log.d(TAG, "onClick FAB");
                }
                Intent intent = new Intent(RoomsActivity.this, AddRoomActivity.class);
                startActivityForResult(intent, RoomsActivity.ADD_ROOM_REQUEST);
            }
        });

        actionsListener = new RoomsPresenter(this, Injection.getDataProvider(this));

        itemListener = new RoomItemListener() {
            @Override
            public void onRoomClick(Room clickedRoom) {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "Clic détecté sur la salle " + clickedRoom.getRoomName());
                }
                actionsListener.openRoomDetail(clickedRoom);
            }
        };

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        roomsAdapter = new RoomsAdapter(new ArrayList<Room>(0), itemListener);
        recyclerView.setAdapter(roomsAdapter);
        int numColumns = getResources().getInteger(R.integer.num_persons_columns);

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
                actionsListener.loadRooms(true);
            }
        });

        // Charge les salles à l'ouverture de l'activité
        actionsListener.loadRooms(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == ADD_ROOM_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                forceReload();
            }
        } else if (requestCode == ROOM_DETAIL_REQUEST) {
            if (resultCode == GiftListActivity.RESULT_RELOAD) {
                forceReload();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Open the navigation drawer when the home icon is selected from the toolbar.
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Initialize the drawer
     * @param navigationView
     */
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            default:
                                break;
                        }
                        // Close the navigation drawer when an item is selected.
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
    public void showRooms(List<Room> rooms) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "showRooms");
        }

        ErrorView errorView = (ErrorView) findViewById(R.id.error_view);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        if (rooms.isEmpty()) {
            errorView.setMessage(getResources().getString(R.string.room_error_view_message));
            errorView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            errorView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            roomsAdapter.replaceData(rooms);
        }
    }

    @Override
    public void showRoomDetail(@NonNull int roomId) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "showRoomDetail");
        }
        Intent intent = new Intent(this, GiftListActivity.class);
        intent.putExtra(GiftListActivity.EXTRA_ROOM_ID, roomId);
        startActivityForResult(intent, ROOM_DETAIL_REQUEST);
    }

    @Override
    public void forceReload() {
        actionsListener.loadRooms(false);
    }


    /**********************************************************************************************/
    /********************************     Adapter & ViewHolder     ********************************/
    /**********************************************************************************************/

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
}
