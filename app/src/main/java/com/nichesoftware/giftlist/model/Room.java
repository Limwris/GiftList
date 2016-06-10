package com.nichesoftware.giftlist.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kattleya on 22/05/2016.
 */
public class Room {
    /**
     * Identifiant unique de la salle
     */
    private int id;
    /**
     * Nom de la salle
     */
    private String roomName;
    /**
     * Occasion du cadeau
     */
    private String occasion;
    /**
     * Liste des cadeaux associés à cette personne
     */
    private List<Gift> giftList;


    /**
     * Contructeur par défaut
     * @param id
     * @param roomName
     * @param occasion
     */
    public Room(final int id, final String roomName, final String occasion) {
        this.id = id;
        this.roomName = roomName;
        this.occasion = occasion;
    }

    /**
     * Contructeur avec liste de cadeau
     * @param id
     * @param roomName
     * @param occasion
     * @param gifts
     */
    public Room(final int id, final String roomName, final String occasion, List<Gift> gifts) {
        this.id = id;
        this.roomName = roomName;
        this.occasion = occasion;
        this.giftList = gifts;
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
     * Getter sur la liste des cadeaux associés à cette personne
     * @return List<Gift> giftList
     */
    public List<Gift> getGiftList() {
        return giftList;
    }

    /**
     * Setter sur la liste des cadeaux associés à cette personne
     * @param giftList
     */
    public void setGiftList(List<Gift> giftList) {
        this.giftList = giftList;
    }

    /**
     * Getter sur l'identifiant unique de la personne
     * @return id
     */
    public int getId() {
        return id;
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

    /**
     * Ajout d'un cadeau à la liste
     * @param gift
     */
    public void addGift(Gift gift) {
        if (giftList == null) {
            giftList = new ArrayList<>();
        }
        giftList.add(gift);
    }

    public void addAllGifts(List<Gift> gifts) {
        if (giftList == null) {
            giftList = new ArrayList<>();
        }
        giftList.addAll(gifts);
    }
}