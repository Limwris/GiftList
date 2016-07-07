package com.nichesoftware.giftlist.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Kattleya on 07/07/2016.
 */
public class NotificationDto {
    /**
     * Corps du message de la notification
     */
    private String body;
    /**
     * Titre de la notification
     */
    private String title;
    /**
     * Icône de la notification
     */
    private String icon;

    /**
     * Getter sur le corps du message de la notification
     * @return
     */
    public String getBody() {
        return body;
    }

    /**
     * Setter sur le corps du message de la notification
     * @param body
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * Getter sur le titre de la notification
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter sur le titre de la notification
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Getter sur l'icône de la notification
     * @return
     */
    public String getIcon() {
        return icon;
    }

    /**
     * Setter sur l'icône de la notification
     * @param icon
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }
}
