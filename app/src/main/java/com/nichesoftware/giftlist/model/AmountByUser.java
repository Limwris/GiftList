package com.nichesoftware.giftlist.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Wrapping class for amount by {@link User}
 */
public class AmountByUser implements Parcelable {
    private User user;
    private double amount;

    /**
     * Default constructor
     */
    public AmountByUser() {
        // Nothing
    }

    public AmountByUser(User user, double amount) {
        this.user = user;
        this.amount = amount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    protected AmountByUser(Parcel in) {
        user = in.readParcelable(User.class.getClassLoader());
                amount = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeDouble(amount);
        dest.writeParcelable(user, flags);
    }

    public static final Creator<AmountByUser> CREATOR = new Creator<AmountByUser>() {
        @Override
        public AmountByUser createFromParcel(Parcel in) {
            return new AmountByUser(in);
        }

        @Override
        public AmountByUser[] newArray(int size) {
            return new AmountByUser[size];
        }
    };
}
