package com.nichesoftware.giftlist.views.start;

import com.nichesoftware.giftlist.R;

/**
 * Created by n_che on 08/06/2016.
 */
public enum PagerEnum {

    GETTING_STARTED(R.string.getting_started_didacticiel_title, R.layout.getting_started_didacticiel_view),
    ADD_ROOM(R.string.add_room_didacticiel_title, R.layout.add_room_didacticiel_view),
    ADD_GIFT(R.string.add_gift_didacticiel_title, R.layout.add_gift_didacticiel_view);

    private int mTitleResId;
    private int mLayoutResId;

    PagerEnum(int titleResId, int layoutResId) {
        mTitleResId = titleResId;
        mLayoutResId = layoutResId;
    }

    public int getTitleResId() {
        return mTitleResId;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }
}
