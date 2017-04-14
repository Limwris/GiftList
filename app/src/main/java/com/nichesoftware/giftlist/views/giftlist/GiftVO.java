package com.nichesoftware.giftlist.views.giftlist;

import android.text.SpannableString;

import com.nichesoftware.giftlist.views.adapter.ViewHolderVO;

import java.util.List;

/**
 * View object used in {@link GiftListAdapter}
 */
public class GiftVO extends ViewHolderVO {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GiftVO giftVO = (GiftVO) o;

        if (id != null ? !id.equals(giftVO.id) : giftVO.id != null) return false;
        if (price != null ? !price.equals(giftVO.price) : giftVO.price != null) return false;
        if (amount != null ? !amount.equals(giftVO.amount) : giftVO.amount != null) return false;
        if (name != null ? !name.equals(giftVO.name) : giftVO.name != null) return false;
        if (imageUrl != null ? !imageUrl.equals(giftVO.imageUrl) : giftVO.imageUrl != null)
            return false;
        if (remainder != null ? !remainder.equals(giftVO.remainder) : giftVO.remainder != null)
            return false;
        return detailVO != null ? detailVO.equals(giftVO.detailVO) : giftVO.detailVO == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (imageUrl != null ? imageUrl.hashCode() : 0);
        result = 31 * result + (remainder != null ? remainder.hashCode() : 0);
        result = 31 * result + (detailVO != null ? detailVO.hashCode() : 0);
        return result;
    }
}
