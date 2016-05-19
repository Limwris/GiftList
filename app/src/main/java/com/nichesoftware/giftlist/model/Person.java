package com.nichesoftware.giftlist.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by n_che on 25/04/2016.
 */
public class Person {
    /**
     * Identifiant unique de la personne
     */
    private String id;
    /**
     * Prénom
     */
    private String firstName;
    /**
     * Nom
     */
    private String lastName;

    /**
     * Liste des cadeaux associés à cette personne
     */
    private List<Gift> giftList = new ArrayList<>();

    /**
     * Contructeur par défaut
     * @param firstName
     * @param lastName
     */
//    public Person(@Nullable final String firstName, @Nullable final String lastName) {
//        this.id = UUID.randomUUID().toString();
//        this.firstName = firstName;
//        this.lastName = lastName;
//    }

    /**
     * Contructeur avec liste de cadeau
     * @param firstName
     * @param lastName
     * @param gifts
     */
//    public Person(@Nullable final String firstName, @Nullable final String lastName, @NonNull List<Gift> gifts) {
//        this.id = UUID.randomUUID().toString();
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.giftList = gifts;
//    }

    /**
     * Contructeur par défaut
     * @param id
     * @param firstName
     * @param lastName
     */
    public Person(@NonNull final String id, @Nullable final String firstName, @Nullable final String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    /**
     * Contructeur avec liste de cadeau
     * @param id
     * @param firstName
     * @param lastName
     * @param gifts
     */
    public Person(@NonNull final String id, @Nullable final String firstName, @Nullable final String lastName, @NonNull List<Gift> gifts) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.giftList = gifts;
    }

    /**
     * Getter sur le prénom
     * @return firstName
     */
    @Nullable
    public String getFirstName() {
        return firstName;
    }

    /**
     * Setter sur le prénom
     * @param firstName
     */
    @Nullable
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Getter sur le nom
     * @return lastName
     */
    @Nullable
    public String getLastName() {
        return lastName;
    }

    /**
     * Setter sur le nom
     * @param lastName
     */
    @Nullable
    public void setLastName(String lastName) {
        this.lastName = lastName;
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
    @NonNull
    public String getId() {
        return id;
    }

    /**
     * Setter sur l'identifiant unique de la personne
     * @return id
     */
    public void setId(@NonNull final String id) {
        this.id = id;
    }
}
