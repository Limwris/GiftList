package com.nichesoftware.giftlist.views.giftlist;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nichesoftware.giftlist.R;
import com.nichesoftware.giftlist.views.adapter.ViewHolder;

import java.util.List;

import butterknife.BindView;

/**
 * Gift list detail adapter
 */
/* package */ class GiftListDetailAdapter extends RecyclerView.Adapter<GiftListDetailAdapter.GiftDetailViewHolder> {
    // Constants   ---------------------------------------------------------------------------------
    private static final String TAG = GiftListDetailAdapter.class.getSimpleName();

    /// Fields   ------------------------------------------------------------------------------------
    /**
     * Données (liste de cadeaux)
     */
    private List<GiftListDetailVO> VOs;

    /**
     * Constructeur
     * @param VOs   Data
     */
    /* package */ GiftListDetailAdapter(List<GiftListDetailVO> VOs) {
        Log.d(TAG, "GiftListDetailAdapter");
        this.VOs = VOs;
    }

    @Override
    public GiftDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View giftDetailView = inflater.inflate(R.layout.gift_list_detail_item_view, parent, false);

        return new GiftDetailViewHolder(giftDetailView);
    }

    @Override
    public void onBindViewHolder(GiftDetailViewHolder viewHolder, int position) {
        if (position < VOs.size()) {
            GiftListDetailVO vo = VOs.get(position);
            viewHolder.bind(vo);
        }
    }

    @Override
    public int getItemCount() {
        return VOs.size();
    }

    /* package */ static class GiftDetailViewHolder extends ViewHolder<GiftListDetailVO> {

        /**
         * Nom du cadeau
         */
        @BindView(R.id.gift_list_detail_username)
        TextView name;

        /**
         * Participation du cadeau
         */
        @BindView(R.id.gift_list_detail_participation)
        TextView amount;

        /**
         * Default constructor
         *
         * @param itemView Root view of the {@link ViewHolder}
         */
        /* package */ GiftDetailViewHolder(View itemView) {
            super(itemView);
        }

        public void bind(GiftListDetailVO vo) {
            name.setText(vo.getUsername());
            amount.setText(vo.getParticipation(), TextView.BufferType.SPANNABLE);
        }
    }
}
