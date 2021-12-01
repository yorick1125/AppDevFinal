package com.example.flashcardapplication;

import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
    private ViewGroup parent;

    public DeckListRecyclerViewAdapter(List<Deck> items) {
        decks = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        parent = parent;
        return new ViewHolder(FragmentHomePageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.deck = decks.get(position);
        holder.nameTextView.setText(String.valueOf(decks.get(position).getId().intValue()));
        holder.subjectTextView.setText(decks.get(position).getTitle());

        holder.playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view)
                        .navigate(R.id.action_homePageFragment_to_studyModeFragment);
            }
        });
    }

    @Override
    public int getItemCount() {
        return decks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView nameTextView;
        public final TextView subjectTextView;
        public final ImageButton playButton;
        public Deck deck;

        public ViewHolder(FragmentHomePageBinding binding) {
            super(binding.getRoot());
            nameTextView = binding.deckName;
            subjectTextView = binding.deckSubject;
            playButton = binding.deckPlayButton;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + subjectTextView.getText() + "'";
        }
    }
}