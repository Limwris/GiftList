package com.nichesoftware.giftlist.dto;

/**
 * Created by n_che on 27/06/2016.
 */
public class AcceptInvitationDto {
    /**
     * Token permettant de valider si l'invitation est valide
     */
    private String token;
    /**
     * Identifiant unique de la salle à laquelle est rattaché le cadeau
     */
    private int roomId;

    /**
     * Getter sur le token permettant de valider si l'invitation est valide
     * @return
     */
    public String getToken() {
        return token;
    }

    /**
     * Setter sur le token permettant de valider si l'invitation est valide
     * @param token
     */
    public void setToken(String token) {
        this.token = token;
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
