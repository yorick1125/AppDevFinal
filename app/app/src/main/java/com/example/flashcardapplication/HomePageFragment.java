package com.example.flashcardapplication;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


import com.example.flashcardapplication.model.Card;
import com.example.flashcardapplication.model.Deck;
import com.example.flashcardapplication.model.DeckTable;
import com.example.flashcardapplication.sqlite.DatabaseException;
import com.example.flashcardapplication.viewmodel.DeckViewModel;
import com.example.flashcardapplication.viewmodel.ObservableModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A fragment representing a list of Items.
 */
public class HomePageFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private MainActivity activity;
    private DeckListRecyclerViewAdapter adapter;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public HomePageFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static HomePageFragment newInstance(int columnCount) {
        HomePageFragment fragment = new HomePageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity)getActivity();
        DeckTable deckTable = (DeckTable) activity.getDeckDBHandler().getDeckTable();

        activity.getDeckViewModel().addOnUpdateListener(this, new ObservableModel.OnUpdateListener<DeckViewModel>() {
            @Override
            public void onUpdate(DeckViewModel item){
                switch (item.getState()) {
                    case EDITED :
                        adapter.setDeck(item.getUpdatedDeck());
                        adapter.notifyDataSetChanged();
                        try {
                            activity.getDeckDBHandler().getDeckTable().update(item.getUpdatedDeck());
                        } catch (DatabaseException e) {
                            e.printStackTrace();
                        }
                        break;
                    case CREATED:
                        adapter.addDeck(item.getUpdatedDeck());
                        adapter.notifyDataSetChanged();
                        try {
                            activity.getDeckDBHandler().getDeckTable().create(item.getUpdatedDeck());
                            System.out.println(item.getUpdatedDeck().getTitle());
                            System.out.println(activity.getDeckDBHandler().getDeckTable().readAll().size());
                        } catch (DatabaseException e) {
                            e.printStackTrace();
                        }
                        break;
                    case BEFORE_EDIT:
                    case BEFORE_CREATE:
                    case NONE:
                        // do nothing
                }
                // TODO: maybe? item.setState(TasksViewModel.State.NONE);

            }
        });
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_page_list, container, false);
        activity = (MainActivity) getActivity();

        // Set the adapter
        //if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.deckRecyclerView);
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

        try {
            List<Deck> decks = activity.getDeckDBHandler().getDeckTable().readAll();
            List<Card> cards = activity.getCardDBHandler().getCardTable().readAll();

            for(int i = 0; i < decks.size(); i++)
            {
                for(int j = 0; j < cards.size(); j++)
                {
                    if(cards.get(j).getDeckId() == decks.get(i).getId())
                    {
                        decks.get(i).getCards().add(cards.get(j));
                    }
                }
            }

            adapter = new DeckListRecyclerViewAdapter(decks, activity);
            recyclerView.setAdapter(adapter);
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
        //}

        FloatingActionButton fab = view.findViewById(R.id.addDeckFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.getDeckViewModel().setState(DeckViewModel.State.BEFORE_CREATE);
                activity.getDeckViewModel().setDeck(new Deck());
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
                navController.navigate(R.id.action_homePageFragment_to_cardListFragment);
            }
        });

        // Filter Edit Text
        EditText filterText = (EditText) view.findViewById(R.id.filterEditText);
        filterText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                DeckListRecyclerViewAdapter adapter = (DeckListRecyclerViewAdapter) recyclerView.getAdapter();
                List<Deck> filteredList = adapter.getData().stream().filter(d -> d.getTitle().toLowerCase().contains(charSequence)).collect(Collectors.toList());
                adapter.setDecks(filteredList);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return view;
    }



}