package com.nichesoftware.giftlist.views.adapter;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;

import java.util.List;

/**
 * Implementation of the {@link android.support.v7.util.DiffUtil.Callback} used to calculate the difference between two lists
 */
public class DiffUtilCallback<T extends ViewHolderVO> extends DiffUtil.Callback {
    // Old data
    private final List<T> mOldItems;
    // New data
    private final List<T> mNewItems;

    /**
     * Default constructor with the 2 lists
     *
     * @param oldItems List of old items
     * @param newItems List of new items
     */
    public DiffUtilCallback(@NonNull List<T> oldItems, @NonNull List<T> newItems) {
        mOldItems = oldItems;
        mNewItems = newItems;
    }

    @Override
    public int getOldListSize() {
        return mOldItems.size();
    }

    @Override
    public int getNewListSize() {
        return mNewItems.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldItems.get(oldItemPosition).equals(mNewItems.get(newItemPosition));
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldItems.get(oldItemPosition).hash() == mNewItems.get(newItemPosition).hash();
    }
}
