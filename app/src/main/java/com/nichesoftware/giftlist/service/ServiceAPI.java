package com.nichesoftware.giftlist.service;

import com.nichesoftware.giftlist.model.Person;

import java.util.List;

/**
 * Created by n_che on 25/04/2016.
 */
public interface ServiceAPI {
    interface PersonServiceCallback<T> {
        void onLoaded(T person);
    }

    void getAllPersons(PersonServiceCallback<List<Person>> callback);
}
