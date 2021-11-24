package com.example.flashcardapplication;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.flashcardapplication.model.Card;
import com.example.flashcardapplication.databinding.FragmentCardListItemBinding;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Card}.
 * TODO: Replace the implementation with code for your data type.
 */
public class CardRecyclerViewAdapter extends RecyclerView.Adapter<CardRecyclerViewAdapter.ViewHolder> {

    private final List<Card> cards;

    public CardRecyclerViewAdapter(List<Card> items) {
        cards = items;
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
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView question;
        public final TextView answer;
        public Card card;

        public ViewHolder(FragmentCardListItemBinding binding) {
            super(binding.getRoot());
            question = binding.cardQuestion;
            answer = binding.cardAnswer;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + question.getText() + "'" + answer.getText() + "'";
        }
    }
}