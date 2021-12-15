package com.example.flashcardapplication;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.shapes.Shape;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.flashcardapplication.enums.Subjects;
import com.example.flashcardapplication.model.Deck;
import com.example.flashcardapplication.databinding.FragmentHomePageBinding;
import com.example.flashcardapplication.sqlite.DatabaseException;
import com.example.flashcardapplication.viewmodel.DeckViewModel;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Deck}.
 * TODO: Replace the implementation with code for your data type.
 */
public class DeckListRecyclerViewAdapter extends RecyclerView.Adapter<DeckListRecyclerViewAdapter.ViewHolder> {

    private List<Deck> decks;
    private List<Deck> data;
    private MainActivity activity;
    private ViewGroup parent;

    public DeckListRecyclerViewAdapter(List<Deck> items, Context context) {
        activity = (MainActivity) context;
        decks = items;
        data = items;
    }

    public List<Deck> getTasks(){
        return decks;
    }
    public List<Deck> getData(){
        return data;
    }
    public void setDecks(List<Deck> newTasks) {
        decks = newTasks;
    }
    public void setData(List<Deck> newData){
        data = newData;
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
        holder.setColor();
        holder.nameTextView.setText(String.valueOf(decks.get(position).getTitle()));
        if(decks.get(position).getSubject() != null){
            holder.subjectTextView.setText(decks.get(position).getSubject().toString());
        }
        if(decks.get(position).getDueDate() != null) {
            holder.dueDateTextView.setText(decks.get(position).getDueString());
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

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decks.remove(holder.deck);
                data.remove(holder.deck);
                try {
                    activity.getDeckDBHandler().getDeckTable().delete(holder.deck);
                } catch (DatabaseException e) {
                    e.printStackTrace();
                }
                notifyDataSetChanged();
            }
        });
    }

    public boolean setDeck(Deck deck) {
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
            return true;
        }
        return false;
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
        public final FragmentHomePageBinding binding;
        public final TextView nameTextView;
        public final TextView subjectTextView;
        public final TextView dueDateTextView;
        public final LinearLayout deckItemLayout;
        public final ImageButton playButton;
        public final ImageButton deleteButton;
        public Deck deck;

        public ViewHolder(FragmentHomePageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            nameTextView = binding.deckName;
            subjectTextView = binding.deckSubject;
            dueDateTextView = binding.deckDueDate;
            playButton = binding.deckPlayButton;
            deckItemLayout = binding.deckItemLayout;
            deleteButton = binding.deckDeleteButton;
            playButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(deck.getCards().size() > 0)
                    {
                        activity.getDeckViewModel().setDeck(deck);

                        Navigation.findNavController(view)
                                .navigate(R.id.action_homePageFragment_to_studyModeFragment);
                    }
                    else
                    {
                        activity.showSnackbar("Cannot play an empty deck");
                    }

                }
            });




        }

        public void setColor(){
            switch(deck.getSubject()){
                case English:
                    deckItemLayout.getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_ATOP);
                    break;
                case French:
                    deckItemLayout.getBackground().setColorFilter(Color.parseColor("#ff8c00"), PorterDuff.Mode.SRC_ATOP);
                    break;
                case Math:
                    deckItemLayout.getBackground().setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
                    break;
                case History:
                    deckItemLayout.getBackground().setColorFilter(Color.parseColor("#9400d3"), PorterDuff.Mode.SRC_ATOP);
                    break;
                case Science:
                    deckItemLayout.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);
                    break;
                case Art:
                    deckItemLayout.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                    break;
                case None:
                    deckItemLayout.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                    break;
            }
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