package com.nichesoftware.giftlist.model;

import android.support.annotation.NonNull;

/**
 * Created by n_che on 25/04/2016.
 */
public class Gift {
    /**
     * Identifiant unique du cadeau
     */
    private String id;
    /**
     * Montant du cadeau
     */
    private double amount;
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
//    public Gift() {
//        this.id = UUID.randomUUID().toString();
//    }

    /**
     * Setter sur l'identifiant unique du cadeau
     * @return id
     */
    public void setId(@NonNull final String id) {
        this.id = id;
    }

    /**
     * Getter sur l'identifiant unique du cadeau
     * @return id
     */
    @NonNull
    public String getId() {
        return id;
    }

    /**
     * Getter sur le montant du cadeau
     * @return amount
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Setter sur le montant du cadeau
     * @param amount
     */
    public void setAmount(double amount) {
        this.amount = amount;
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
}
