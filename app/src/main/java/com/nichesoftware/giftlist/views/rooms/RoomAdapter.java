package com.nichesoftware.giftlist.views.rooms;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nichesoftware.giftlist.R;
import com.nichesoftware.giftlist.model.Room;

import java.util.List;

/**
 * Room adapter
 */
public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.ViewHolder> {
    // Constants   ---------------------------------------------------------------------------------
    private static final String TAG = RoomAdapter.class.getSimpleName();

    // Fields   ------------------------------------------------------------------------------------
    /**
     * Données (liste de salles)
     */
    private List<Room> rooms;

    /**
     * Listener sur le clic de la salle
     */
    private RoomsActivity.RoomItemListener itemListener;

    /**
     * Context
     */
    private Context context;

    /**
     * Constructeur
     * @param rooms
     * @param itemListener
     */
    public RoomAdapter(List<Room> rooms, RoomsActivity.RoomItemListener itemListener) {
        Log.d(TAG, "RoomsAdapter");

        setList(rooms);
        this.itemListener = itemListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "RoomsAdapter - onCreateViewHolder");

        this.context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View roomsView = inflater.inflate(R.layout.room_list_item_view, parent, false);

        return new ViewHolder(roomsView, itemListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Log.d(TAG, "RoomsAdapter - onBindViewHolder");

        Room room = rooms.get(position);

        viewHolder.name.setText(room.getRoomName());
        // The first parameter of getQuantityString is used to decide which format to use (single or plural)
        viewHolder.giftDescription.setText(context.getResources().getQuantityString(R.plurals.gift_description, room.getBoughtGiftListSize(), room.getBoughtGiftListSize(), room.getGiftListSize()));
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
        TextView name;

        /**
         * Description des cadeaux en cours
         */
        TextView giftDescription;

        /**
         * Listener sur le clic de la salle
         */
        private RoomsActivity.RoomItemListener roomItemListener;

        /**
         * Constructeur
         * @param itemView vue d'un item de la liste
         * @param listener listener sur le clic d'un item de la liste
         */
        public ViewHolder(View itemView, RoomsActivity.RoomItemListener listener) {
            super(itemView);
            roomItemListener = listener;
            name = (TextView) itemView.findViewById(R.id.room_name);
            giftDescription = (TextView) itemView.findViewById(R.id.room_gift_description);
            itemView.findViewById(R.id.mainHolder).setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Log.d(TAG, "Clic détecté dans la liste à la position: " + position);

            Room room = getItem(position);
            if (roomItemListener != null) {
                roomItemListener.onRoomClick(room);
            }

        }
    }
}
