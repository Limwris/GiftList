package com.nichesoftware.giftlist.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * User model
 */
public class User implements Parcelable {
    /**
     * Identifiant de l'utilisateur
     */
    private String name;
    /**
     * Mot de passe de l'utilisateur
     */
    private String password;
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
     * Default constructor
     */
    public User() {
        // Nothing
    }

    /**
     * Constructor
     *
     * @param username      Identifiant de l'utilisateur
     */
    public User(final String username) {
        this.name = username;
    }

    /**
     * Constructor
     *
     * @param username      Identifiant de l'utilisateur
     * @param password      Mot de passe de l'utilisateur
     */
    public User(final String username, final String password) {
        this.name = username;
        this.password = password;
    }

    /**
     * Getter sur l'identifiant de l'utilisateur
     * @return username
     */
    public String getName() {
        return name;
    }

    /**
     * Setter sur l'identifiant de l'utilisateur
     * @param username - identfiant de l'utilisateur
     */
    public void setName(String username) {
        this.name = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
                "username='" + name + '\'' +
                ", token='" + token + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", isTokenSent=" + isTokenSent +
                '}';
    }

    // region Parcelable
    protected User(Parcel in) {
        name = in.readString();
        password = in.readString();
        token = in.readString();
        phoneNumber = in.readString();
        isTokenSent = in.readInt() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(password);
        dest.writeString(token);
        dest.writeString(phoneNumber);
        dest.writeInt(isTokenSent ? 1 : 0);
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
    // endregion
}
