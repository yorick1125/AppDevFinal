package com.example.flashcardapplication;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.flashcardapplication.model.Card;
import com.example.flashcardapplication.databinding.FragmentCardListItemBinding;
import com.example.flashcardapplication.model.Deck;
//import com.example.flashcardapplication.viewmodel.CardViewModel;
import com.example.flashcardapplication.sqlite.DatabaseException;
import com.example.flashcardapplication.viewmodel.CardViewModel;
import com.example.flashcardapplication.viewmodel.DeckViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Card}.
 * TODO: Replace the implementation with code for your data type.
 */
public class CardRecyclerViewAdapter extends RecyclerView.Adapter<CardRecyclerViewAdapter.ViewHolder> {

    private List<Card> cards;
    private List<Card> data;
    private MainActivity activity;

    public CardRecyclerViewAdapter(List<Card> items, Context context) {
        activity = (MainActivity) context;
        cards = items;
        data = items;
        if(cards == null){
            cards = new ArrayList<Card>();
        }
    }

    public List<Card> getTasks(){
        return cards;
    }
    public List<Card> getData(){
        return data;
    }
    public void setCards(List<Card> newTasks) {
        cards = newTasks;
    }
    public void setData(List<Card> newData){
        data = newData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentCardListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.card = cards.get(position);
        holder.question.setText(cards.get(position).getFront());
        holder.answer.setText(cards.get(position).getBack());
        holder.cardItemLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                activity.getCardViewModel().setCard(holder.card);
                activity.getCardViewModel().setState(CardViewModel.State.BEFORE_EDIT);
                activity.getCardViewModel().notifyChange();

                NavController navController = Navigation.findNavController(activity, R.id.nav_host_fragment_content_main);
                navController.navigate(R.id.action_cardListFragment_to_editCardFragment);
                return true;
            }
        });
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cards.remove(holder.card);
                data.remove(holder.card);
                try {
                    activity.getCardDBHandler().getCardTable().delete(holder.card);
                } catch (DatabaseException e) {
                    e.printStackTrace();
                }
                notifyDataSetChanged();
            }
        });
    }

    public void setCard(Card card) {
        int pos = 0;
        for(Card c : cards) {
            if (c.getId() == card.getId())
                break;
            pos++;
        }
        if(pos < cards.size()) {
            cards.set(pos, card);
            //notifyItemChanged(pos);
            notifyDataSetChanged();
        }
        // TODO error if not found?
    }

    public void addCard(Card card) {
        cards.add(card);
        notifyItemInserted(cards.size()-1);
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView question;
        public final TextView answer;
        public final ImageButton deleteButton;
        public Card card;
        public final LinearLayout cardItemLayout;

        public ViewHolder(FragmentCardListItemBinding binding) {
            super(binding.getRoot());
            question = binding.cardQuestion;
            answer = binding.cardAnswer;
            cardItemLayout = binding.cardItemLayout;
            deleteButton = binding.cardDeleteButton;

        }

        @Override
        public String toString() {
            return super.toString() + " '" + question.getText() + "'" + answer.getText() + "'";
        }
    }
}