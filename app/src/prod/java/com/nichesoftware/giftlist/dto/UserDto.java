package com.nichesoftware.giftlist.dto;

/**
 * Created by n_che on 08/06/2016.
 */
public class UserDto {
    /**
     * Identifiant de l'utilisateur
     */
    private String username;
    /**
     * Mot de passe de l'utilisateur
     */
    private String password;
    /**
     * Numéro de téléphone
     */
    private String phoneNumber;

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
     * Getter sur le mot de passe de l'utilisateur
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setter sur le mot de passe de l'utilisateur
     * @param password - mot de passe de l'utilisateur
     */
    public void setPassword(String password) {
        this.password = password;
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
}
