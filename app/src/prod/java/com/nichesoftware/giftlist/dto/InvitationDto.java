package com.nichesoftware.giftlist.dto;

/**
 * Created by n_che on 24/06/2016.
 */
public class InvitationDto {
    /**
     * Identifiant de l'utilisateur
     */
    private String username;
    /**
     * Identifiant unique de la salle à laquelle est rattaché le cadeau
     */
    private int roomId;
    /**
     * Getter sur l'identifiant de l'utilisateur
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setter sur l'identifiant de l'utilisateur
     * @param username - identfiant de l'utilisateur
     */
    public void setUsername(String username) {
        this.username = username;
    }
    /**
     * Getter sur l'identifiant unique de la salle associée
     * @return roomId
     */
    public int getRoomId() {
        return roomId;
    }

    /**
     * Setter sur l'identifiant unique de la salle associée
     * @param roomId
     */
    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }
}
