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
    private String name;
    /**
     * Occasion du cadeau
     */
    private String occasion;
    /**
     * Liste des cadeaux associés à cette personne
     */
    private List<Gift> gifts;

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
     * @param name
     * @param occasion
     */
    public Room(final String name, final String occasion) {
        this.name = name;
        this.occasion = occasion;
    }

    /**
     * Contructeur par défaut
     *
     * @param id
     * @param name
     * @param occasion
     */
    public Room(final String id, final String name, final String occasion) {
        this.id = id;
        this.name = name;
        this.occasion = occasion;
    }

    /**
     * Contructeur avec liste de cadeau
     *
     * @param id
     * @param name
     * @param occasion
     * @param gifts
     */
    public Room(final String id, final String name, final String occasion, List<Gift> gifts) {
        this.id = id;
        this.name = name;
        this.occasion = occasion;
        this.gifts = gifts;
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
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter sur le nom de la salle
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter sur la liste des cadeaux associés à cette personne
     * @return List<Gift> gifts
     */
    public List<Gift> getGifts() {
        return gifts;
    }

    /**
     * Setter sur la liste des cadeaux associés à cette personne
     * @param gifts
     */
    public void setGifts(List<Gift> gifts) {
        this.gifts = gifts;
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
        if (gifts == null) {
            gifts = new ArrayList<>();
        }
        gifts.add(gift);
    }

    public void addAllGifts(List<Gift> gifts) {
        if (this.gifts == null) {
            this.gifts = new ArrayList<>();
        }
        this.gifts.addAll(gifts);
    }
    /**
     * Ajout d'un cadeau à la liste
     * @param gift
     */
    public boolean updateGift(Gift gift) {
        for (Gift temp : gifts) {
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
        if (gifts == null) {
            gifts = new ArrayList<>();
        }
        for (Gift gift : gifts) {
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
        if (gifts == null) {
            return 0;
        } else {
            return gifts.size();
        }
    }

    /**
     * Méthode qui retourne le nombre de cadeaux déjà achetés
     * @return
     */
    public int getBoughtGiftListSize() {
        if (gifts == null) {
            gifts = new ArrayList<>();
            return 0;
        } else {
            int boughtCounter = 0;
            for (Gift gift : gifts) {
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
                ", name='" + name + '\'' +
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
        parcel.writeString(name);
        parcel.writeString(occasion);
        parcel.writeTypedList(gifts);
    }

    public Room(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.occasion = in.readString();
        in.readTypedList(gifts, Gift.CREATOR);
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
