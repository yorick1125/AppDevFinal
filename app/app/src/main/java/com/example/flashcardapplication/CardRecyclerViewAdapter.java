package com.example.flashcardapplication;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.flashcardapplication.model.Card;
import com.example.flashcardapplication.databinding.FragmentCardListItemBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Card}.
 * TODO: Replace the implementation with code for your data type.
 */
public class CardRecyclerViewAdapter extends RecyclerView.Adapter<CardRecyclerViewAdapter.ViewHolder> {

    private List<Card> cards;
    private MainActivity activity;

    public CardRecyclerViewAdapter(List<Card> items, Context context) {
        activity = (MainActivity) context;
        cards = items;
        if(cards == null){
            cards = new ArrayList<Card>();
        }
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
                activity.getCardViewModel().notifyChange();

                NavController navController = Navigation.findNavController(activity, R.id.nav_host_fragment_content_main);
                navController.navigate(R.id.action_cardListFragment_to_editCardFragment);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView question;
        public final TextView answer;
        public Card card;
        public final LinearLayout cardItemLayout;

        public ViewHolder(FragmentCardListItemBinding binding) {
            super(binding.getRoot());
            question = binding.cardQuestion;
            answer = binding.cardAnswer;
            cardItemLayout = binding.cardItemLayout;


        }

        @Override
        public String toString() {
            return super.toString() + " '" + question.getText() + "'" + answer.getText() + "'";
        }
    }
}