package com.nichesoftware.giftlist.model;

import java.util.Date;

/**
 * Created by n_che on 27/06/2016.
 */
public class Invitation {
    /**
     * Utilisateur étant invité
     */
    private User invitedUser;
    /**
     * Salle dans laquelle l'utilisateur est invitée
     */
    private Room room;
    /**
     * Date d'expiration du token d'invitation
     */
    private Date expireDate;

    /**
     * Getter sur l'utilisateur étant invité
     * @return
     */
    public User getInvitedUser() {
        return invitedUser;
    }

    /**
     * Setter sur l'utilisateur étant invité
     * @param invitedUser
     */
    public void setInvitedUser(User invitedUser) {
        this.invitedUser = invitedUser;
    }

    /**
     * Getter sur la salle dans laquelle l'utilisateur est invitée
     * @return
     */
    public Room getRoom() {
        return room;
    }

    /**
     * Setter sur la salle dans laquelle l'utilisateur est invitée
     * @param room
     */
    public void setRoom(Room room) {
        this.room = room;
    }

    /**
     * Getter sur la date d'expiration du token d'invitation
     * @return
     */
    public Date getExpireDate() {
        return expireDate;
    }

    /**
     * Setter sur la date d'expiration du token d'invitation
     * @param expireDate
     */
    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }
}
