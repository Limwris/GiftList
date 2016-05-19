package com.nichesoftware.giflist;

import com.nichesoftware.giftlist.model.Person;
import com.nichesoftware.giftlist.service.ServiceAPI;

import java.util.List;

/**
 * Created by n_che on 25/04/2016.
 */
public class PersonListService implements ServiceAPI {
    @Override
    public void getAllPersons(PersonServiceCallback<List<Person>> callback) {
        // Todo
        callback.onLoaded(null);
    }
}
