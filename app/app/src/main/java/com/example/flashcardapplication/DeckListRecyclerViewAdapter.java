package com.example.flashcardapplication;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.flashcardapplication.model.Deck;
import com.example.flashcardapplication.databinding.FragmentHomePageBinding;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Deck}.
 * TODO: Replace the implementation with code for your data type.
 */
public class DeckListRecyclerViewAdapter extends RecyclerView.Adapter<DeckListRecyclerViewAdapter.ViewHolder> {

    private final List<Deck> decks;

    public DeckListRecyclerViewAdapter(List<Deck> items) {
        decks = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentHomePageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.deck = decks.get(position);
        holder.nameTextView.setText(decks.get(position).getId().intValue());
        holder.subjectTextView.setText(decks.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return decks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView nameTextView;
        public final TextView subjectTextView;
        public Deck deck;

        public ViewHolder(FragmentHomePageBinding binding) {
            super(binding.getRoot());
<<<<<<< Updated upstream
            mIdView = binding.itemNumber;
            mContentView = binding.content;
=======
            nameTextView = binding.deckName;
            subjectTextView = binding.deckSubject;
>>>>>>> Stashed changes
        }

        @Override
        public String toString() {
            return super.toString() + " '" + subjectTextView.getText() + "'";
        }
    }
}