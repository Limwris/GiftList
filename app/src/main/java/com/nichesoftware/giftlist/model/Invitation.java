package com.nichesoftware.giftlist.model;

import java.util.Date;

/**
 * Invitation to a room
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
     * Utilisateur invitant
     */
    private User invitingUser;
    /**
     * Token d'invitation
     */
    private String token;

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
     * Getter sur l'utilisateur invitant
     * @return
     */
    public User getInvitingUser() {
        return invitingUser;
    }

    /**
     * Setter sur l'utilisateur invitant
     * @param invitingUser
     */
    public void setInvitingUser(User invitingUser) {
        this.invitingUser = invitingUser;
    }

    /**
     * Getter sur le token d'invitation
     * @return
     */
    public String getToken() {
        return token;
    }

    /**
     * Setter sur le token d'invitation
     * @param token
     */
    public void setToken(String token) {
        this.token = token;
    }
}
