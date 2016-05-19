package com.nichesoftware.giftlist.contracts;

import android.support.annotation.NonNull;

import com.nichesoftware.giftlist.model.Person;

import java.util.List;

/**
 * Created by n_che on 25/04/2016.
 */
public interface PersonListContract {
    interface View {
        /**
         * Affiche les personnes passées en paramètre
         * @param persons
         */
        void showPersons(List<Person> persons);
        /**
         * Affiche le détail de la personne passée en paramètre (id)
         * @param personId   Identifiant de la personne à afficher
         */
        void showPersonDetail(@NonNull String personId);
        /**
         * Affiche le loader
         */
        void showLoader();
        /**
         * Supprime le loader
         */
        void hideLoader();
    }
    interface UserActionListener {
        /**
         * Action permettant d'ouvrir le détail d'une personne
         * @param person   Note à ouvrir
         */
        void openPersonDetail(Person person);
        /**
         * Action permettant de charger la liste des personnes
         * @param forceUpdate   Flag indiquant si la mise à jour doit être forcée
         */
        void loadPersons(boolean forceUpdate);
        /**
         * Action permettant l'ajout d'une personne
         */
        void addNewPerson();
    }
}
