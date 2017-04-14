package com.nichesoftware.giftlist.views.rooms;

import android.content.Context;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nichesoftware.giftlist.R;
import com.nichesoftware.giftlist.views.adapter.DiffUtilCallback;
import com.nichesoftware.giftlist.views.adapter.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Room adapter
 */
public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {
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
    public RoomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder");

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View roomsView = inflater.inflate(R.layout.room_list_item_view, parent, false);

        return new RoomViewHolder(roomsView, mItemListener);
    }

    @Override
    public void onBindViewHolder(RoomViewHolder viewHolder, int position) {
        Log.d(TAG, "onBindViewHolder");
        if (position < mRooms.size()) {
            RoomVO room = mRooms.get(position);
            viewHolder.bind(room);
        }
    }

    public void replaceData(List<RoomVO> rooms) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtilCallback<>(mRooms, rooms));
        mRooms.clear();
        mRooms.addAll(rooms);
        diffResult.dispatchUpdatesTo(this);
    }

    @Override
    public int getItemCount() {
        return mRooms.size();
    }

    /* package */ static class RoomViewHolder extends ViewHolder<RoomVO> {

        /**
         * Nom
         */
        @BindView(R.id.room_name)
        TextView name;

        /**
         * Description des cadeaux en cours
         */
        @BindView(R.id.room_gift_description)
        TextView giftDescription;

        /**
         * Listener sur le clic de la salle
         */
        private final RoomsActivity.RoomItemListener roomItemListener;

        /**
         * Constructeur
         * @param itemView vue d'un item de la liste
         * @param listener listener sur le clic d'un item de la liste
         */
        /* package */ RoomViewHolder(View itemView, RoomsActivity.RoomItemListener listener) {
            super(itemView);
            roomItemListener = listener;
        }

        public void bind(RoomVO vo) {
            name.setText(vo.getRoomName());
            giftDescription.setText(vo.getDescription());
            itemView.findViewById(R.id.mainHolder).setOnClickListener(v -> {
                if (roomItemListener != null) {
                    roomItemListener.onRoomClick(vo.getId());
                }
            });
        }
    }
}
