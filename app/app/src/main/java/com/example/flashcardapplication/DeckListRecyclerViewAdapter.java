package com.example.flashcardapplication;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
    private MainActivity activity;
    private ViewGroup parent;

    public DeckListRecyclerViewAdapter(List<Deck> items, MainActivity activity) {
        decks = items;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        parent = parent;
        return new ViewHolder(FragmentHomePageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.deck = decks.get(position);
        holder.nameTextView.setText(String.valueOf(decks.get(position).getTitle()));
        holder.subjectTextView.setText(decks.get(position).getSubject().toString());
        holder.dueDateTextView.setText(decks.get(position).getDueDate().toString());
        holder.deckItemLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                NavController navController = Navigation.findNavController(activity, R.id.nav_host_fragment_content_main);
                navController.navigate(R.id.action_homePageFragment_to_cardListFragment);
                return true;
            }
        });
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
        }

        @Override
        public String toString() {
            return super.toString() + " '" + subjectTextView.getText() + "'";
        }
    }
}