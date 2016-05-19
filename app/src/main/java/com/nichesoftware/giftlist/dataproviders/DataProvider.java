package com.nichesoftware.giftlist.dataproviders;

import android.support.annotation.NonNull;

import com.nichesoftware.giftlist.model.Gift;
import com.nichesoftware.giftlist.model.Person;
import com.nichesoftware.giftlist.service.ServiceAPI;

import java.util.List;

/**
 * Created by n_che on 25/04/2016.
 */
public class DataProvider {
    /**
     * Liste des personnes en mémoire
     */
    protected List<Person> cachedPersons;
    /**
     * Service
     */
    private ServiceAPI serviceApi;

    /**
     * Constructeur
     * @param serviceApi
     */
    public DataProvider(ServiceAPI serviceApi) {
        this.serviceApi = serviceApi;
    }

    public interface LoadPersonsCallback {
        void onPersonsLoaded(List<Person> persons);
    }

    public interface LoadGiftsCallback {
        void onGiftsLoaded(List<Gift> gifts);
    }

    /**
     * Récupère l'ensemble des personnes
     * @param callback
     */
    public void getPersons(@NonNull final LoadPersonsCallback callback) {
        // Load from API only if needed.
        if (cachedPersons == null) {
            serviceApi.getAllPersons(new ServiceAPI.PersonServiceCallback<List<Person>>() {
                @Override
                public void onLoaded(List<Person> persons) {
                    cachedPersons = persons;
                    callback.onPersonsLoaded(cachedPersons);
                }
            });
        } else {
            callback.onPersonsLoaded(cachedPersons);
        }
    }

    /**
     * Méthode appelée pour forcer le rafraîchissement des données lors du prochain appel
     */
    public void refreshData() {
        cachedPersons = null;
    }

    /**
     * Récupère les cadeaux associés à une personne
     * @param personId
     * @param callback
     */
    public void getGifts(@NonNull final String personId, @NonNull final LoadGiftsCallback callback) {
        if (cachedPersons == null) {
            serviceApi.getAllPersons(new ServiceAPI.PersonServiceCallback<List<Person>>() {
                @Override
                public void onLoaded(List<Person> persons) {
                    cachedPersons = persons;
                    retreiveGifts(personId, callback);
                }
            });
        } else {
            retreiveGifts(personId, callback);
        }
    }

    private void retreiveGifts(@NonNull final String personId, @NonNull final LoadGiftsCallback callback) {
        Person person = getPersonFromId(personId);

        if (person != null && cachedPersons.contains(person)) {
            callback.onGiftsLoaded(person.getGiftList());
        } else {
            // Todo: gestion d'erreur
            callback.onGiftsLoaded(null);
        }
    }

    private Person getPersonFromId(final String personId) {
        for (Person person : cachedPersons) {
            if (person.getId().equals(personId)) {
                return person;
            }
        }
        return null;
    }
}
