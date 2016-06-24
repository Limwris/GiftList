package com.nichesoftware.giftlist.views.adduser;

import com.nichesoftware.giftlist.model.User;

/**
 * Created by n_che on 24/06/2016.
 */
public class AddUserVO {
    /**
     * Utilisateur
     */
    private User user;
    /**
     * Flag indiquant si cet utilisateur a été sélectionné
     */
    private boolean isChecked;

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
