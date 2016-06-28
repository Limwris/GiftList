package com.nichesoftware.giftlist.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by n_che on 17/06/2016.
 */
public class User {
    public final static String DISCONNECTED_USER = "DISCONNECTED_USER";
    /**
     * Identifiant de l'utilisateur
     */
    private String username;
    /**
     * Token de l'utilisateur
     */
    private String token;
    /**
     * Liste des salles
     */
    private List<Room> rooms;
    /**
     * Numéro de téléphone
     */
    private String phoneNumber;
    /**
     * Flag indiquant si le token GCM a été envoyé correctement au serveur
     */
    private boolean isTokenSent;

    /**
     * Getter sur l'identifiant de l'utilisateur
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setter sur l'identifiant de l'utilisateur
     * @param username - identfiant de l'utilisateur
     */
    public void setUsername(String username) {
        this.username = username;
    }
    /**
     * Getter sur la liste des salles
     * @return rooms
     */
    public List<Room> getRooms() {
        return rooms;
    }

    /**
     * Setter sur la liste des salles
     * @param rooms - liste des salles de l'utilisateur
     */
    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    /**
     * Getter sur le token de l'utilisateur
     * @return token
     */
    public String getToken() {
        return token;
    }

    /**
     * Setter sur le token de l'utilisateur
     * @param token
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Getter sur le numéro de téléphone
     * @return phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Setter sur le numéro de téléphone
     * @param phoneNumber
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Getter sur le flag indiquant si le token GCM a été envoyé correctement au serveur
     * @return
     */
    public boolean isTokenSent() {
        return isTokenSent;
    }

    /**
     * Setter sur le flag indiquant si le token GCM a été envoyé correctement au serveur
     * @param isTokenSent
     */
    public void setIsTokenSent(boolean isTokenSent) {
        this.isTokenSent = isTokenSent;
    }

    /**
     * Ajoute une salle à la liste
     * @param room - Salle à ajouter à la liste de l'utilisateur
     */
    public void addRoom(Room room) {
        if (rooms == null) {
            rooms = new ArrayList<>();
        }
        rooms.add(room);
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", token='" + token + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", isTokenSent=" + isTokenSent +
                '}';
    }
}
