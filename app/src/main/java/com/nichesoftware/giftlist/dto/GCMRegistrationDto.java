package com.nichesoftware.giftlist.dto;

/**
 * Created by Kattleya on 01/08/2016.
 */
public class GCMRegistrationDto {
    /**
     * Identifiant GCM
     */
    private String registerId;

    /**
     * Getter sur l'identifiant GCM
     * @return registerId
     */
    public String getRegisterId() {
        return registerId;
    }

    /**
     * Setter sur l'identifiant GCM
     * @param registerId
     */
    public void setRegisterId(String registerId) {
        this.registerId = registerId;
    }
}
