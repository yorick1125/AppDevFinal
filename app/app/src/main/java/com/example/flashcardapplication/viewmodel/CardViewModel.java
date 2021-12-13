package com.example.flashcardapplication.viewmodel;

import com.example.flashcardapplication.model.Card;
import com.example.flashcardapplication.model.Deck;

public class CardViewModel extends ObservableModel<CardViewModel> {

    public enum State { NONE, BEFORE_EDIT, EDITED, BEFORE_CREATE, CREATED }

    private State state;
    private Card card;
    private Card updatedCard;

    public CardViewModel() {
        state = State.NONE;
    }

    @Override
    protected CardViewModel get() {
        return this;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }


    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public Card getUpdatedCard() {
        return updatedCard;
    }

    public void setUpdatedCard(Card updatedCard) {
        this.updatedCard = updatedCard;
    }
}
