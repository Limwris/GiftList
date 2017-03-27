package com.nichesoftware.giftlist.views.giftlist;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nichesoftware.giftlist.R;

import java.util.List;

/**
 * Gift list detail adapter
 */
public class GiftListDetailAdapter extends RecyclerView.Adapter<GiftListDetailAdapter.ViewHolder> {
    // Constants   ---------------------------------------------------------------------------------
    private static final String TAG = GiftListDetailAdapter.class.getSimpleName();

    // Fields   ------------------------------------------------------------------------------------
    /**
     * Données (liste de cadeaux)
     */
    private List<GiftListDetailVO> VOs;

    /**
     * Constructeur
     * @param VOs
     */
    public GiftListDetailAdapter(List<GiftListDetailVO> VOs) {
        Log.d(TAG, "GiftListDetailAdapter");
        this.VOs = VOs;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View giftDetailView = inflater.inflate(R.layout.gift_list_detail_item_view, parent, false);

        return new ViewHolder(giftDetailView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        if (position < VOs.size()) {
            GiftListDetailVO vo = VOs.get(position);
            viewHolder.bindData(vo);
        }
    }

    @Override
    public int getItemCount() {
        return VOs.size();
    }

    /* package */ class ViewHolder extends RecyclerView.ViewHolder {

        /**
         * Nom du cadeau
         */
        private TextView name;

        /**
         * Participation du cadeau
         */
        private TextView amount;


        /**
         * Constructeur
         * @param itemView
         */
        /* package */ ViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.gift_list_detail_username);
            amount = (TextView) itemView.findViewById(R.id.gift_list_detail_participation);
        }

        public void bindData(GiftListDetailVO vo) {
            name.setText(vo.getUsername());
            amount.setText(vo.getParticipation(), TextView.BufferType.SPANNABLE);
        }
    }
}
