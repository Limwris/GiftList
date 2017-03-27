package com.nichesoftware.giftlist.views.giftlist;

import android.text.SpannableString;

/**
 * View Object used in the {@link GiftListDetailAdapter}
 */
public class GiftListDetailVO {
    private String username;
    private SpannableString participation;

    public SpannableString getParticipation() {
        return participation;
    }

    public void setParticipation(SpannableString participation) {
        this.participation = participation;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
