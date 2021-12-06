package com.example.flashcardapplication.viewmodel;

import com.example.flashcardapplication.model.Deck;

public class DeckViewModel extends ObservableModel<DeckViewModel> {

    public enum State { NONE, BEFORE_EDIT, EDITED, BEFORE_CREATE, CREATED }

    private State state;
    private Deck deck;
    private Deck updatedDeck;

    public DeckViewModel() {
        state = State.NONE;
    }

    @Override
    protected DeckViewModel get() {
        return this;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }


    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public Deck getUpdatedDeck() {
        return updatedDeck;
    }

    public void setUpdatedDeck(Deck updatedDeck) {
        this.updatedDeck = updatedDeck;
    }
}
