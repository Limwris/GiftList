package com.nichesoftware.giftlist.views.giftlist;

import android.text.SpannableString;

import com.nichesoftware.giftlist.views.adapter.ViewHolderVO;

/**
 * View Object used in the {@link GiftListDetailAdapter}
 */
public class GiftListDetailVO extends ViewHolderVO {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GiftListDetailVO that = (GiftListDetailVO) o;

        if (username != null ? !username.equals(that.username) : that.username != null)
            return false;
        return participation != null ? participation.equals(that.participation) : that.participation == null;
    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (participation != null ? participation.hashCode() : 0);
        return result;
    }
}
