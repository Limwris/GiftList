package com.nichesoftware.giftlist.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Created by n_che on 25/04/2016.
 */
public class Gift implements Parcelable {
    /**
     * Identifiant unique du cadeau
     */
    private int id;
    /**
     * Montant du cadeau
     */
    private double price;
    /**
     * Nom du cadeau
     */
    private String name;
    /**
     * Url pointant vers un site proposant le cadeau
     */
    private String url;
    /**
     * Flag indiquant si la cadeau a été acheté
     */
    private boolean isBought;

    /**
     * Constructeur
     */
    public Gift() {
        // Nothing
    }

    /**
     * Setter sur l'identifiant unique du cadeau
     * @return id
     */
    public void setId(@NonNull final int id) {
        this.id = id;
    }

    /**
     * Getter sur l'identifiant unique du cadeau
     * @return id
     */
    @NonNull
    public int getId() {
        return id;
    }

    /**
     * Getter sur le montant du cadeau
     * @return price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Setter sur le montant du cadeau
     * @param price
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Getter sur le nom du cadeau
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter sur le nom du cadeau
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter sur l'url pointant vers un site proposant le cadeau
     * @return url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Setter sur l'url pointant vers un site proposant le cadeau
     * @param url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Getter sur le flag indiquant si la cadeau a été acheté
     * @return isBought
     */
    public boolean isBought() {
        return isBought;
    }

    /**
     * Setter sur le flag indiquant si la cadeau a été acheté
     * @param isBought
     */
    public void setIsBought(boolean isBought) {
        this.isBought = isBought;
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
        parcel.writeInt(id);
        parcel.writeDouble(price);
        parcel.writeString(name);
        parcel.writeString(url);
        parcel.writeByte((byte) (isBought ? 1 : 0));
    }

    public Gift(Parcel in) {
        this.id = in.readInt();
        this.price = in.readDouble();
        this.name = in.readString();
        this.url = in.readString();
        this.isBought = in.readByte() != 0;     //myBoolean == true if byte != 0
    }

    public static final Parcelable.Creator<Gift> CREATOR = new Parcelable.Creator<Gift>() {

        @Override
        public Gift createFromParcel(Parcel in) {
            return new Gift(in);
        }

        @Override
        public Gift[] newArray(int size) {
            return new Gift[size];
        }
    };
}
