package com.nichesoftware.giftlist;

import com.nichesoftware.giftlist.model.Gift;
import com.nichesoftware.giftlist.model.Person;
import com.nichesoftware.giftlist.service.ServiceAPI;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by n_che on 25/04/2016.
 */
public class MockService implements ServiceAPI {
    private static List<Person> PERSONS = new ArrayList<>();

    static {
        List<Gift> gifts = new ArrayList<>();
        Gift gift = new Gift();
        gift.setName("Ours en peluche");
        gift.setAmount(22.05);
        gifts.add(gift);
        gift = new Gift();
        gift.setName("Playstation 8");
        gift.setAmount(455.99);
        gifts.add(gift);
        PERSONS.add(new Person("0", "John", "Doe", gifts));
        PERSONS.add(new Person("1", "Jane", "Doe"));
        PERSONS.add(new Person("2", "Jean-Charles", "Dupond"));
    }
    @Override
    public void getAllPersons(PersonServiceCallback<List<Person>> callback) {
        callback.onLoaded(PERSONS);
    }
}
