package com.nichesoftware.giftlist.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

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
     * Montants alloués par utilisateur au cadeau
     */
    private Map<String, Double> amountByUser = new HashMap<>();

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
     * Indique si la cadeau a été acheté
     * @return isBought
     */
    public boolean isBought() {
        double amount = 0;
        for (Map.Entry<String, Double> entry : amountByUser.entrySet()) {
            amount += entry.getValue();
        }
        return amount >= price;
    }

    /**
     * Getter sur les montants alloués par utilisateur au cadeau
     * @return
     */
    public Map<String, Double> getAmountByUser() {
        return amountByUser;
    }

    /**
     * Setter sur les montants alloués par utilisateur au cadeau
     * @param amountByUser
     */
    public void setAmountByUser(Map<String, Double> amountByUser) {
        this.amountByUser = amountByUser;
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
        parcel.writeInt(amountByUser.size());
        for (Map.Entry<String, Double> entry : amountByUser.entrySet()) {
            parcel.writeString(entry.getKey());
            parcel.writeDouble(entry.getValue());
        }
    }

    public Gift(Parcel in) {
        this.id = in.readInt();
        this.price = in.readDouble();
        this.name = in.readString();
        this.url = in.readString();
        int count = in.readInt();
        for (int i = 0; i < count; i++) {
            amountByUser.put(in.readString(), in.readDouble());
        }
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
