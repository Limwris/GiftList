package com.nichesoftware.giftlist.views.giftlist;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nichesoftware.giftlist.R;
import com.nichesoftware.giftlist.views.adapter.ViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Gift list adapter
 */
public class GiftListAdapter extends RecyclerView.Adapter<GiftListAdapter.GiftViewHolder> {
    // Constants   ---------------------------------------------------------------------------------
    private static final String TAG = GiftListAdapter.class.getSimpleName();

    // Fields   ------------------------------------------------------------------------------------
    /**
     * Données (liste de cadeaux)
     */
    private List<GiftVO> gifts = new ArrayList<>();

    /**
     * Listener sur le clic d'un cadeau
     */
    private final GiftListActivity.GiftItemListener giftItemListener;

    /**
     * Constructeur
     *
     * @param giftItemListener      Click listener
     */
    public GiftListAdapter(GiftListActivity.GiftItemListener giftItemListener) {
        this.giftItemListener = giftItemListener;
    }

    @Override
    public GiftViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View giftView = inflater.inflate(R.layout.gift_list_item_view, parent, false);

        return new GiftViewHolder(giftView, new OnGiftViewHolderListener() {
            @Override
            public void onSeeMoreClick(int position) {
                for (GiftVO gift : gifts) {
                    // If the last expanded postion were not the current one
                    if (gift.isExpanded() && gifts.indexOf(gift) != position) {
                        gift.setExpanded(false);
                    }
                }
                // Expand the given position
                gifts.get(position).setExpanded(!gifts.get(position).isExpanded());
                notifyDataSetChanged();
            }

            @Override
            public void onGiftClick(int position) {
                if (position < gifts.size()) {
                    GiftVO giftVO = gifts.get(position);
                    if (giftItemListener != null) {
                        giftItemListener.onGiftClick(giftVO.getId());
                    }
                }
            }
        });
    }

    @Override
    public void onBindViewHolder(final GiftViewHolder viewHolder, int position) {
        if (position < gifts.size()) {
            GiftVO giftVO = gifts.get(position);
            viewHolder.bind(giftVO);
        }
    }

    public void replaceData(@NonNull List<GiftVO> gifts) {
        this.gifts = gifts;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return gifts.size();
    }

    /* package */ interface OnGiftViewHolderListener {
        void onSeeMoreClick(final int position);
        void onGiftClick(final int position);
    }

    /* package */ static class GiftViewHolder extends ViewHolder<GiftVO> {
        /**
         * Nom du cadeau
         */
        @BindView(R.id.gift_name)
        TextView name;

        /**
         * Participation du cadeau
         */
        @BindView(R.id.gift_amount)
        TextView amount;

        /**
         * Restant au cadeau
         */
        @BindView(R.id.gift_remainder)
        TextView remainder;

        /**
         * Prix du cadeau
         */
        @BindView(R.id.gift_price)
        TextView price;

        /**
         * Image associée au cadeau
         */
        @BindView(R.id.gift_image)
        ImageView image;

        /**
         * Display participating user
         */
        @BindView(R.id.gift_list_details)
        RecyclerView detailRecyclerView;

        @OnClick(R.id.gift_list_see_more_button)
        void onSeeMoreButtonClick() {
            if (mSeeMoreClickListener != null) {
                mSeeMoreClickListener.onSeeMoreClick(getAdapterPosition());
            }
        }

        @OnClick(R.id.gift_list_detail_button)
        void onDetailButtonClick() {
            if (mSeeMoreClickListener != null) {
                mSeeMoreClickListener.onGiftClick(getAdapterPosition());
            }
        }

        /**
         * Listener
         */
        private final OnGiftViewHolderListener mSeeMoreClickListener;

        /**
         * Constructeur
         * @param itemView
         */
        /* package */ GiftViewHolder(final View itemView, OnGiftViewHolderListener seeMoreClickListener) {
            super(itemView);
            mSeeMoreClickListener = seeMoreClickListener;

            detailRecyclerView.setHasFixedSize(true);
            detailRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.VERTICAL, false));
        }

        @Override
        public void bind(GiftVO gift) {
            if (!gift.isExpanded()) {
                detailRecyclerView.setVisibility(View.GONE);
            } else {
                detailRecyclerView.setVisibility(View.VISIBLE);
            }

            name.setText(gift.getName());
            price.setText(gift.getPrice(), TextView.BufferType.SPANNABLE);
            amount.setText(gift.getAmount(), TextView.BufferType.SPANNABLE);
            remainder.setText(gift.getRemainder(), TextView.BufferType.SPANNABLE);
            detailRecyclerView.setAdapter(new GiftListDetailAdapter(gift.getDetailVO()));

            Picasso.with(image.getContext())
                    .load(gift.getImageUrl())
                    .fit().centerCrop().placeholder(R.drawable.placeholder)
                    .into(image);
        }
    }
}
