package com.nichesoftware.giftlist.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Base ViewHolder
 */
public abstract class ViewHolder<T> extends RecyclerView.ViewHolder {

    /**
     * Default constructor
     *
     * @param itemView Root view of the {@link ViewHolder}
     */
    public ViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public abstract void bind(T data);
}
