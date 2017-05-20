package com.nichesoftware.giftlist.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Gift model
 */
public class Gift implements Parcelable, Cloneable {
    // Constants
    private static final String TAG = Gift.class.getSimpleName();

    /**
     * Default contructor
     */
    public Gift() {
        // Nothing
    }

    /**
     * Constructor used to add a {@link Gift}
     *
     * @param name          Name of the {@link Gift}
     * @param price         Price of the {@link Gift}
     * @param amount        Amount dedicated by the {@link User} to the {@link Gift}
     * @param description   Description of the {@link Gift}
     * @param filePath      Local file path where lies the picture of the {@link Gift}
     */
    public Gift(String name, double price, double amount,
                final String description, final String filePath) {
        this.name = name;
        this.price = price;
        this.amount = amount;
        this.description = description;
        this.imageUrl = filePath;
        this.isImageFileLocal = true;
    }

    /**
     * Constructor used in DB
     *
     * @param id            Id of the {@link Gift}
     * @param name          Name of the {@link Gift}
     * @param amount        Amount dedicated by the {@link User} to the {@link Gift}
     * @param price         Price of the {@link Gift}
     * @param remainder     Name of the {@link Gift}
     * @param description   Description of the {@link Gift}
     * @param url           Url linking to a webpage describing the {@link Gift}
     * @param imageUrl      Url pointing at a picture of the {@link Gift}
     * @param isLocalImage  Flag which indicates whether or not the imageUrl point a local image or a remote one
     */
    public Gift(final String id, final String name, double amount, double price, double remainder,
                final String description, final String url, final String imageUrl, boolean isLocalImage) {
        this.id = id;
        this.price = price;
        this.amount = amount;
        this.name = name;
        this.description = description;
        this.url = url;
        this.imageUrl = imageUrl;
        this.isImageFileLocal = isLocalImage;
        this.remainder = remainder;
    }

    /**
     * Indique si la cadeau a été acheté
     * @return isBought
     */
//    public boolean isBought() {
//        double amount = 0;
//        for (Map.Entry<String, Double> entry : amountByUser.entrySet()) {
//            amount += entry.getValue();
//        }
//        return amount >= price;
//    }

    /**
     * Indicates the remainder due to buy the gift
     * @param username
     * @return
     */
//    public double remainder(final String username) {
//        double remainder = price;
//        for (Map.Entry<String, Double> entry : amountByUser.entrySet()) {
//            if (!entry.getKey().equals(username)) {
//                remainder -= entry.getValue();
//            }
//        }
//        return remainder;
//    }

    // Fields ----------------------------------------------------------------------------------------------------------
    /**
     * Identifiant unique du cadeau
     */
    private String id;

    /**
     * Prix du cadeau
     */
    private double price;

    /**
     * Montant du cadeau
     */
    private double amount;

    /**
     * Flag indiquant si la cadeau a été acheté
     */
    private boolean isBought;

    /**
     * Nom du cadeau
     */
    private String name;

    /**
     * Description du cadeau
     */
    private String description;

    /**
     * Url pointant vers un site proposant le cadeau
     */
    private String url;

    /**
     * Url pointant vers l'image associée au cadeau
     */
    private String imageUrl;

    /**
     * Flag which indicates whether the picture is still stored on disk
     */
    private transient boolean isImageFileLocal;

    /**
     * Montants alloués par utilisateur au cadeau
     */
    private List<AmountByUser> amountByUser = new ArrayList<>();

    /**
     * Montant restant afin d'acheter le cadeau
     */
    private double remainder;

    // Getter ----------------------------------------------------------------------------------------------------------
    /**
     * Getter sur l'identifiant unique du cadeau
     * @return id
     */
    @NonNull
    public String getId() {
        return id;
    }

    /**
     * Getter sur le prix du cadeau
     * @return price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Getter sur le montant du cadeau
     * @return price
     */
    public double getAmount() {
        return amount;
    }

    public boolean isBought() {
        return isBought;
    }

    /**
     * Getter sur le nom du cadeau
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Getter sur la description du cadeau
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Getter sur l'url pointant vers un site proposant le cadeau
     * @return url
     */
    public String getUrl() {
        return url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * Getter sur les montants alloués par utilisateur au cadeau
     * @return
     */
    public List<AmountByUser> getAmountByUser() {
        return amountByUser;
    }

    // Setter ----------------------------------------------------------------------------------------------------------
    /**
     * Setter sur l'identifiant unique du cadeau
     * @return id
     */
    public void setId(@NonNull final String id) {
        this.id = id;
    }

    /**
     * Setter sur le prix du cadeau
     * @param price
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Setter sur le montant du cadeau
     * @param amount
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setBought(boolean bought) {
        isBought = bought;
    }

    /**
     * Setter sur le nom du cadeau
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Setter sur la description du cadeau
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Setter sur l'url pointant vers un site proposant le cadeau
     * @param url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setImageFileLocal(boolean imageFileLocal) {
        isImageFileLocal = imageFileLocal;
    }

    public boolean isImageFileLocal() {
        return isImageFileLocal;
    }

    /**
     * Setter sur les montants alloués par utilisateur au cadeau
     * @param amountByUser
     */
    public void setAmountByUser(List<AmountByUser> amountByUser) {
        this.amountByUser = amountByUser;
    }

    public double getRemainder() {
        return remainder;
    }

    public void setRemainder(double remainder) {
        this.remainder = remainder;
    }

    /**********************************************************************************************/
    /********************************          PARCELABLE          ********************************/
    /**********************************************************************************************/

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(id);
        parcel.writeDouble(price);
        parcel.writeDouble(amount);
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeString(url);
        parcel.writeTypedList(amountByUser);
    }

    public Gift(Parcel in) {
        this.id = in.readString();
        this.price = in.readDouble();
        this.amount = in.readDouble();
        this.name = in.readString();
        this.description = in.readString();
        this.url = in.readString();
        in.readTypedList(amountByUser, AmountByUser.CREATOR);
    }

    public static final Parcelable.Creator<Gift> CREATOR = new Parcelable.Creator<Gift>() {

        @Override
        public Gift createFromParcel(Parcel in) {
            return new Gift(in);
        }

        @Override
        public Gift[] newArray(int size) {
            return new Gift[size];
        }
    };

    @Override
    public String toString() {
        return "Gift{" +
                "id=" + id +
                ", price=" + price +
                ", amount=" + amount +
                ", isBought=" + isBought +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public Gift clone() {
        Gift gift = null;
        try {
            // On récupère l'instance à renvoyer par l'appel de la méthode super.clone()
            gift = (Gift) super.clone();
        } catch(CloneNotSupportedException cnse) {
            // Ne devrait jamais arriver car nous implémentons l'interface Cloneable
            Log.e(TAG, "clone not supported", cnse);
        }

        // on renvoie le clone
        return gift;
    }
}
