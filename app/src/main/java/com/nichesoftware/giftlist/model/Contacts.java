package com.nichesoftware.giftlist.model;

import java.util.List;

/**
 * List of available contacts
 */
public class Contacts {
    // List of phone number of user's contacts
    private List<String> phoneNumbers;

    public List<String> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(List<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }
}
