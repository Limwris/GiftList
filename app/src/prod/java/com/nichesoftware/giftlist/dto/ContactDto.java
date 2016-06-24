package com.nichesoftware.giftlist.dto;

import java.util.List;

/**
 * Created by n_che on 24/06/2016.
 */
public class ContactDto {
    /**
     * Liste de numéros de téléphone
     */
    private List<String> phoneNumbers;
    /**
     * Identifiant unique de la salle à laquelle est rattaché le cadeau
     */
    private int roomId;
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

    /**
     * Getter sur la liste de numéros de téléphone
     * @return
     */
    public List<String> getPhoneNumbers() {
        return phoneNumbers;
    }

    /**
     * Setter sur la liste de numéros de téléphone
     * @param phoneNumbers
     */
    public void setPhoneNumbers(List<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }
}
