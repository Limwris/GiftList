package com.nichesoftware.giftlist.views.adduser;

import com.nichesoftware.giftlist.model.User;
import com.nichesoftware.giftlist.views.adapter.ViewHolderVO;

/**
 * View model used in {@link com.nichesoftware.giftlist.views.adduser.ContactAdapter.ContactViewHolder}
 */
public class AddUserVO extends ViewHolderVO {
    /**
     * Utilisateur
     */
    private User user;

    /**
     * Flag indiquant si cet utilisateur a été sélectionné
     */
    private boolean isChecked;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AddUserVO addUserVO = (AddUserVO) o;

        if (isChecked != addUserVO.isChecked) return false;
        return user != null ? (user.getName() != null ? user.getName().equals(addUserVO.user.getName()) : addUserVO.user.getName() == null) : addUserVO.user == null;
    }

    @Override
    public int hashCode() {
        int result = user != null ? (user.getName() != null ? user.getName().hashCode() : 0) : 0;
        result = 31 * result + (isChecked ? 1 : 0);
        return result;
    }

    /**
     * Getter sur l'utilisateur
     * @return
     */
    public User getUser() {
        return user;
    }

    /**
     * Setter sur l'utilisateur
     * @param user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Getter sur le flag indiquant si cet utilisateur a été sélectionné
     * @return
     */
    public boolean isChecked() {
        return isChecked;
    }

    /**
     * Setter sur le flag indiquant si cet utilisateur a été sélectionné
     * @param isChecked
     */
    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }
}
