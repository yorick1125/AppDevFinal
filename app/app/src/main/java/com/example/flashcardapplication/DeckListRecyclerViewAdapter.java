package com.example.flashcardapplication;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.flashcardapplication.model.Deck;
import com.example.flashcardapplication.databinding.FragmentHomePageBinding;
import com.example.flashcardapplication.viewmodel.DeckViewModel;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Deck}.
 * TODO: Replace the implementation with code for your data type.
 */
public class DeckListRecyclerViewAdapter extends RecyclerView.Adapter<DeckListRecyclerViewAdapter.ViewHolder> {

    private final List<Deck> decks;
    private MainActivity activity;
    private ViewGroup parent;

    public DeckListRecyclerViewAdapter(List<Deck> items, Context context) {
        activity = (MainActivity) context;
        decks = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        parent = parent;
        return new ViewHolder(FragmentHomePageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.bind(decks.get(position));

        holder.deck = decks.get(position);
        holder.nameTextView.setText(String.valueOf(decks.get(position).getTitle()));
        if(decks.get(position).getSubject() != null){
            holder.subjectTextView.setText(decks.get(position).getSubject().toString());
        }
        if(decks.get(position).getDueDate() != null) {
            holder.dueDateTextView.setText(decks.get(position).getDueDate().toString());
        }
        
        holder.deckItemLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                activity.getDeckViewModel().setDeck(holder.deck);
                activity.getDeckViewModel().setState(DeckViewModel.State.BEFORE_EDIT);
                activity.getDeckViewModel().notifyChange();

                NavController navController = Navigation.findNavController(activity, R.id.nav_host_fragment_content_main);
                navController.navigate(R.id.action_homePageFragment_to_cardListFragment);
                return true;
            }
        });
    }

    public void setDeck(Deck deck) {
        int pos = 0;
        for(Deck d : decks) {
            if (d.getId() == deck.getId())
                break;
            pos++;
        }
        if(pos < decks.size()) {
            decks.set(pos, deck);
            //notifyItemChanged(pos);
            notifyDataSetChanged();
        }
        // TODO error if not found?
    }

    public void addDeck(Deck deck) {
        decks.add(deck);
        notifyItemInserted(decks.size()-1);
    }

    @Override
    public int getItemCount() {
        return decks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView nameTextView;
        public final TextView subjectTextView;
        public final TextView dueDateTextView;
        public final LinearLayout deckItemLayout;
        public final ImageButton playButton;
        public Deck deck;

        public ViewHolder(FragmentHomePageBinding binding) {
            super(binding.getRoot());
            nameTextView = binding.deckName;
            subjectTextView = binding.deckSubject;
            dueDateTextView = binding.deckDueDate;
            playButton = binding.deckPlayButton;
            deckItemLayout = binding.deckItemLayout;

            playButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    activity.getDeckViewModel().setDeck(deck);

                    Navigation.findNavController(view)
                            .navigate(R.id.action_homePageFragment_to_studyModeFragment);
                }
            });


        }

        public void bind(Deck deck) {
            this.deck = deck;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + subjectTextView.getText() + "'";
        }
    }
}