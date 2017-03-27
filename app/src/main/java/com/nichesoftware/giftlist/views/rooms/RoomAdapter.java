package com.nichesoftware.giftlist.views.rooms;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nichesoftware.giftlist.R;

import java.util.ArrayList;
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
    private List<RoomVO> mRooms = new ArrayList<>();

    /**
     * Listener sur le clic de la salle
     */
    private RoomsActivity.RoomItemListener mItemListener;

    /**
     * Constructor
     *
     * @param itemListener      On click listener
     */
    public RoomAdapter(RoomsActivity.RoomItemListener itemListener) {
        Log.d(TAG, "RoomsAdapter");
        this.mItemListener = itemListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder");

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View roomsView = inflater.inflate(R.layout.room_list_item_view, parent, false);

        return new ViewHolder(roomsView, mItemListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Log.d(TAG, "onBindViewHolder");
        if (position < mRooms.size()) {
            RoomVO room = mRooms.get(position);
            viewHolder.bindData(room);
        }
    }

    public void replaceData(List<RoomVO> rooms) {
        mRooms = rooms;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mRooms.size();
    }

    /* package */ class ViewHolder extends RecyclerView.ViewHolder {

        /**
         * Nom
         */
        private TextView name;

        /**
         * Description des cadeaux en cours
         */
        private TextView giftDescription;

        /**
         * Listener sur le clic de la salle
         */
        private RoomsActivity.RoomItemListener roomItemListener;

        /**
         * Constructeur
         * @param itemView vue d'un item de la liste
         * @param listener listener sur le clic d'un item de la liste
         */
        /* package */ ViewHolder(View itemView, RoomsActivity.RoomItemListener listener) {
            super(itemView);
            roomItemListener = listener;
            name = (TextView) itemView.findViewById(R.id.room_name);
            giftDescription = (TextView) itemView.findViewById(R.id.room_gift_description);
        }

        public void bindData(RoomVO vo) {
            name.setText(vo.getRoomName());
            giftDescription.setText(vo.getDescription());
            itemView.findViewById(R.id.mainHolder).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (roomItemListener != null) {
                        roomItemListener.onRoomClick(vo.getId());
                    }
                }
            });
        }
    }
}
