package com.nichesoftware.giftlist.views.giftlist;

import android.text.SpannableString;

import java.util.List;

/**
 * View object used in {@link GiftListAdapter}
 */
public class GiftVO {
    /**
     * Identifiant unique du cadeau
     */
    private String id;

    /**
     * Prix du cadeau
     */
    private SpannableString price;

    /**
     * Montant du cadeau
     */
    private SpannableString amount;

    /**
     * Nom du cadeau
     */
    private String name;

    /**
     * Url pointant vers l'image associée au cadeau
     */
    private String imageUrl;

    /**
     * Montant restant afin d'acheter le cadeau
     */
    private SpannableString remainder;

    /**
     * View object associated to the detail
     */
    private List<GiftListDetailVO> detailVO;

    /**
     * Flag which indicates if the card is expanded or not
     */
    private boolean isExpanded;

    /**
     * Default constructor
     *
     * @param id
     * @param price
     * @param amount
     * @param name
     * @param imageUrl
     * @param remainder
     */
    public GiftVO(String id, SpannableString price, SpannableString amount, String name, String imageUrl, SpannableString remainder) {
        this.id = id;
        this.price = price;
        this.amount = amount;
        this.name = name;
        this.imageUrl = imageUrl;
        this.remainder = remainder;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SpannableString getPrice() {
        return price;
    }

    public void setPrice(SpannableString price) {
        this.price = price;
    }

    public SpannableString getAmount() {
        return amount;
    }

    public void setAmount(SpannableString amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public SpannableString getRemainder() {
        return remainder;
    }

    public void setRemainder(SpannableString remainder) {
        this.remainder = remainder;
    }

    public List<GiftListDetailVO> getDetailVO() {
        return detailVO;
    }

    public void setDetailVO(List<GiftListDetailVO> detailVO) {
        this.detailVO = detailVO;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }
}
