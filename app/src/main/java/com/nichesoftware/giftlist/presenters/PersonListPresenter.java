package com.nichesoftware.giftlist.presenters;

import android.support.annotation.NonNull;
import android.util.Log;

import com.nichesoftware.giftlist.BuildConfig;
import com.nichesoftware.giftlist.contracts.PersonListContract;
import com.nichesoftware.giftlist.dataproviders.DataProvider;
import com.nichesoftware.giftlist.model.Person;

import java.util.List;

/**
 * Created by n_che on 25/04/2016.
 */
public class PersonListPresenter implements PersonListContract.UserActionListener {
    private static final String TAG = PersonListPresenter.class.getSimpleName();

    /**
     * Data provider
     */
    private DataProvider dataProvider;

    /**
     * View
     */
    private PersonListContract.View personListView;

    /**
     * Constructeur
     * @param view
     * @param noteDataProvider
     */
    public PersonListPresenter(@NonNull PersonListContract.View view, @NonNull DataProvider noteDataProvider) {
        this.dataProvider = noteDataProvider;
        this.personListView = view;
    }

    @Override
    public void openPersonDetail(Person person) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, String.format("La personne [id=%s, prenom=%s, nom=%s] a été cliquée...", person.getId(), person.getFirstName(), person.getLastName()));
        }
        personListView.showPersonDetail(person.getId());
    }

    @Override
    public void loadPersons(boolean forceUpdate) {
        // Show loader
        personListView.showLoader();

        if (forceUpdate) {
            dataProvider.refreshData();
        }

        dataProvider.getPersons(new DataProvider.LoadPersonsCallback() {
            @Override
            public void onPersonsLoaded(List<Person> notes) {
                // Cache le loader
                personListView.hideLoader();

                if (notes != null) {
                    personListView.showPersons(notes);
                } else {
                    // Gérer erreurs webservice
                }
            }
        });

    }

    @Override
    public void addNewPerson() {
        // Todo
    }
}
