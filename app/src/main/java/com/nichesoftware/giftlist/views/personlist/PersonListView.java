package com.nichesoftware.giftlist.views.personlist;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.nichesoftware.giftlist.BuildConfig;
import com.nichesoftware.giftlist.Injection;
import com.nichesoftware.giftlist.R;
import com.nichesoftware.giftlist.contracts.PersonListContract;
import com.nichesoftware.giftlist.model.Person;
import com.nichesoftware.giftlist.presenters.PersonListPresenter;
import com.nichesoftware.giftlist.views.ErrorView;
import com.nichesoftware.giftlist.views.giftlist.GiftListActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by n_che on 25/04/2016.
 */
public class PersonListView extends FrameLayout implements PersonListContract.View {
    private static final String TAG = PersonListView.class.getSimpleName();

    /**
     * Adapter lié à la RecyclerView
     */
    private PersonListAdapter personListAdapter;
    /**
     * Listener sur les actions de l'utilisateur
     */
    private PersonListContract.UserActionListener actionsListener;
    /**
     * Listener for clicks on person in the RecyclerView.
     */
    private PersonItemListener itemListener;

    public PersonListView(Context context) {
        super(context);
        init(context);
    }

    public PersonListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PersonListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PersonListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * Initialisation de la vue
     * @param context
     */
    protected void init(Context context) {

        actionsListener = new PersonListPresenter(this, Injection.getDataProvider());

        itemListener = new PersonItemListener() {
            @Override
            public void onPersonClick(Person clickedPerson) {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "Clic détecté sur la personne " + clickedPerson.getFirstName());
                }
                actionsListener.openPersonDetail(clickedPerson);
            }
        };

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View root = inflater.inflate(R.layout.persons_view, this, true);
        View root = inflater.inflate(R.layout.list_view, this, true);
//        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.persons_list);
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);
        personListAdapter = new PersonListAdapter(new ArrayList<Person>(0), itemListener);
        recyclerView.setAdapter(personListAdapter);
        int numColumns = getContext().getResources().getInteger(R.integer.num_persons_columns);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), numColumns));

        // Pull-to-refresh
        SwipeRefreshLayout swipeRefreshLayout =
                (SwipeRefreshLayout) root.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getContext(), R.color.colorPrimary),
                ContextCompat.getColor(getContext(), R.color.colorAccent),
                ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                actionsListener.loadPersons(true);
            }
        });

        // Charge les notes à l'ouverture de l'activité
        actionsListener.loadPersons(false);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        // Action sur le FAB -> La vue doit être attachée à la fenêtre pour être certain
        // que la vue ait accès à l'activité parente et que le FAB soit inflate correctement
        Activity activity = getActivity();
        if (activity != null) {
            activity.findViewById(R.id.fab_add_person).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (BuildConfig.DEBUG) {
                        Log.d(TAG, "onClick FAB");
                    }
                }
            });
        }
    }

    @Override
    public void showLoader() {
        setRefreshIndicator(true);
    }

    @Override
    public void hideLoader() {
        setRefreshIndicator(false);
    }

    private void setRefreshIndicator(final boolean doShow) {
        final SwipeRefreshLayout swipeRefreshLayout =
                (SwipeRefreshLayout) findViewById(R.id.refresh_layout);

        // Make sure setRefreshing() is called after the layout is done with everything else.
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(doShow);
            }
        });
    }

    @Override
    public void showPersons(List<Person> persons) {
//        personListAdapter.replaceData(persons);
        ErrorView errorView = (ErrorView) findViewById(R.id.error_view);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        if (persons.isEmpty()) {
            errorView.setMessage(getResources().getString(R.string.person_error_view_message));
            errorView.setVisibility(VISIBLE);
            recyclerView.setVisibility(GONE);
        } else {
            errorView.setVisibility(GONE);
            recyclerView.setVisibility(VISIBLE);
            personListAdapter.replaceData(persons);
        }
    }

    @Override
    public void showPersonDetail(@NonNull String personId) {
        Intent intent = new Intent(getContext(), GiftListActivity.class);
        intent.putExtra(GiftListActivity.EXTRA_PERSON_ID, personId);
        getContext().startActivity(intent);
    }

    private static class PersonListAdapter extends RecyclerView.Adapter<PersonListAdapter.ViewHolder> {

        /**
         * Données (liste de personnes)
         */
        private List<Person> persons;

        /**
         * Listener sur le clic de la personne
         */
        private PersonItemListener itemListener;

        /**
         * Context
         */
        private Context context;

        /**
         * Constructeur
         * @param notes
         * @param itemListener
         */
        public PersonListAdapter(List<Person> notes, PersonItemListener itemListener) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "PersonListAdapter");
            }
            setList(notes);
            this.itemListener = itemListener;
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "PersonListAdapter - itemListener: " + itemListener);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            this.context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View noteView = inflater.inflate(R.layout.person_list_item, parent, false);

            return new ViewHolder(noteView, itemListener);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            Person person = persons.get(position);

            viewHolder.name.setText(person.getFirstName());
            // The first parameter of getQuantityString is used to decide which format to use (single or plural)
            viewHolder.giftDescription.setText(context.getResources().getQuantityString(R.plurals.gift_description, 0, 0, person.getGiftList().size()));
        }

        public void replaceData(List<Person> persons) {
            setList(persons);
            notifyDataSetChanged();
        }

        private void setList(List<Person> persons) {
            this.persons = persons;
        }

        @Override
        public int getItemCount() {
            return persons.size();
        }

        public Person getItem(int position) {
            return persons.get(position);
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements android.view.View.OnClickListener {

            /**
             * Nom
             */
            public TextView name;

            /**
             * Description des cadeaux en cours
             */
            public TextView giftDescription;

            /**
             * Listener sur le clic de la personne
             */
            private PersonItemListener personItemListener;

            /**
             * Constructeur
             * @param itemView vue d'un item de la liste
             * @param listener listener sur le clic d'un item de la liste
             */
            public ViewHolder(View itemView, PersonItemListener listener) {
                super(itemView);
                personItemListener = listener;
                name = (TextView) itemView.findViewById(R.id.person_name);
                giftDescription = (TextView) itemView.findViewById(R.id.person_gift_description);
                itemView.findViewById(R.id.mainHolder).setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "Clic détecté dans la liste à la position: " + position);
                }
                Person person = getItem(position);
                if (personItemListener != null) {
                    personItemListener.onPersonClick(person);
                }

            }
        }
    }

    /**
     * Interface du listener du clic sur une personne
     */
    public interface PersonItemListener {
        void onPersonClick(Person clickedNote);
    }

    private Activity getActivity() {
        Context context = getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity)context;
            }
            context = ((ContextWrapper)context).getBaseContext();
        }
        return null;
    }
}
