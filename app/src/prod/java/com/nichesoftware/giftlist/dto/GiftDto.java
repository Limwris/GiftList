package com.nichesoftware.giftlist.dto;

/**
 * Created by n_che on 09/06/2016.
 */
public class GiftDto {
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
     * Description du cadeau
     */
    private String description;
    /**
     * Montant alloué par l'utilisateur au cadeau
     */
    private double amount;
    /**
     * Identifiant unique de la salle à laquelle est rattaché le cadeau
     */
    private int roomId;


    /**
     * Getter sur l'identifiant unique du cadeau
     * @return id
     */
    public int getId() {
        return id;
    }


    /**
     * Setter sur l'identifiant unique du cadeau
     * @param id
     */
    public void setId(int id) {
        this.id = id;
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
     * Getter sur la description du cadeau
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter sur la description du cadeau
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter sur le montant alloué par l'utilisateur au cadeau
     * @return amount
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Setter sur le montant alloué par l'utilisateur au cadeau
     * @param amount
     */
    public void setAmount(double amount) {
        this.amount = amount;
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
