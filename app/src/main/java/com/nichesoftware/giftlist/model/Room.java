package com.nichesoftware.giftlist.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Room model
 */
public class Room implements Parcelable {
    /**
     * Identifiant unique de la salle
     */
    private String id;
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
     * Contructor
     *
     * @param id
     */
    public Room(final String id) {
        this.id = id;
    }

    /**
     * Contructor
     *
     * @param roomName
     * @param occasion
     */
    public Room(final String roomName, final String occasion) {
        this.roomName = roomName;
        this.occasion = occasion;
    }

    /**
     * Contructeur par défaut
     *
     * @param id
     * @param roomName
     * @param occasion
     */
    public Room(final String id, final String roomName, final String occasion) {
        this.id = id;
        this.roomName = roomName;
        this.occasion = occasion;
    }

    /**
     * Contructeur avec liste de cadeau
     *
     * @param id
     * @param roomName
     * @param occasion
     * @param gifts
     */
    public Room(final String id, final String roomName, final String occasion, List<Gift> gifts) {
        this.id = id;
        this.roomName = roomName;
        this.occasion = occasion;
        this.giftList = gifts;
    }

    /**
     * Getter sur l'identifiant unique de la salle
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * Setter sur l'identifiant unique de la salle
     * @param id    Identifiant de la salle
     */
    public void setId(String id) {
        this.id = id;
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
    /**
     * Ajout d'un cadeau à la liste
     * @param gift
     */
    public boolean updateGift(Gift gift) {
        for (Gift temp : giftList) {
            if (temp.getId().equals(gift.getId())) {
                // Todo: Update...
                return true;
            }
        }
        return false;
    }

    /**
     * Recherche un cadeau dans la liste des cadeaux de la salle
     * @param giftId - identifiant du cadeau recherchée
     * @return room  - cadeau correspondant à l'identifiant passé en paramètre, nul sinon
     */
    public Gift getGiftById(final String giftId) {
        if (giftList == null) {
            giftList = new ArrayList<>();
        }
        for (Gift gift : giftList) {
            if (gift.getId().equals(giftId)) {
                return gift;
            }
        }
        return null;
    }

    /**
     * Méthode qui retourne le nombre de cadeaux
     * @return
     */
    public int getGiftListSize() {
        if (giftList == null) {
            return 0;
        } else {
            return giftList.size();
        }
    }

    /**
     * Méthode qui retourne le nombre de cadeaux déjà achetés
     * @return
     */
    public int getBoughtGiftListSize() {
        if (giftList == null) {
            giftList = new ArrayList<>();
            return 0;
        } else {
            int boughtCounter = 0;
            for (Gift gift : giftList) {
                if (gift != null && gift.isBought()) {
                    boughtCounter++;
                }
            }
            return boughtCounter;
        }
    }

    @Override
    public String toString() {
        return "Room{" +
                "id='" + id + '\'' +
                ", roomName='" + roomName + '\'' +
                ", occasion='" + occasion + '\'' +
                '}';
    }

    /**********************************************************************************************/
    /********************************          PARCELABLE          ********************************/
    /**********************************************************************************************/

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(id);
        parcel.writeString(roomName);
        parcel.writeString(occasion);
    }

    public Room(Parcel in) {
        this.id = in.readString();
        this.roomName = in.readString();
        this.occasion = in.readString();
    }

    public static final Parcelable.Creator<Room> CREATOR = new Parcelable.Creator<Room>() {

        @Override
        public Room createFromParcel(Parcel in) {
            return new Room(in);
        }

        @Override
        public Room[] newArray(int size) {
            return new Room[size];
        }
    };
}
