package com.nichesoftware.giftlist.dto;

/**
 * Created by n_che on 08/06/2016.
 */
public class RoomDto {
    /**
     * Identifiant de la salle
     */
    private int roomId;
    /**
     * Nom de la salle
     */
    private String roomName;
    /**
     * Prénom de la personne concernée par la salle
     */
    private String firstName;
    /**
     * Nom de la personne concernée par la salle
     */
    private String lastName;
    /**
     * Occasion du cadeau
     */
    private String occasion;

    /**
     * Getter sur l'identifiant de la salle
     * @return roomId
     */
    public int getRoomId() {
        return roomId;
    }

    /**
     * Setter sur l'identifiant de la salle
     * @param roomId
     */
    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    /**
     * Getter sur le nom de la salle
     * @return roomName
     */
    public String getRoomName() {
        return roomName;
    }

    /**
     * Setter sur le nom de la salle
     * @param roomName
     */
    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    /**
     * Getter sur l'occasion du cadeau
     * @return occasion
     */
    public String getOccasion() {
        return occasion;
    }

    /**
     * Setter sur l'occasion du cadeau
     * @param occasion
     */
    public void setOccasion(String occasion) {
        this.occasion = occasion;
    }
}
