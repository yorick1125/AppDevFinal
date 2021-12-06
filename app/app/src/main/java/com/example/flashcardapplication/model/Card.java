package com.example.flashcardapplication.model;

import com.example.flashcardapplication.sqlite.Identifiable;

import java.util.ArrayList;
import java.util.List;

public class Card implements Identifiable<Long> {
    private Long id;
    private String front;
    private String back;
    private Long deckId;

    public static List<Card> getDefaultCards(){
        List<Card> cards = new ArrayList<>();
        cards.add(new Card(1L, "front", "back", 1L));
        cards.add(new Card(1L, "front", "back", 1L));
        cards.add(new Card(1L, "front", "back", 1L));
        return cards;
    }

    public Card(){
        this.id = 0L;
        this.front = "";
        this.back = "";
    }
    public Card(Long id, String front, String back, Long deckId){
        this.id = id;
        this.front = front;
        this.back = back;
        this.deckId = deckId;
    }

    public Long getId(){
        return id;
    }



    @Override
    public Identifiable<Long> setId(Long id) {
        this.id = id;
        return this;
    }

    public Card setCardId(Long id) {
        this.id = id;
        return this;
    }


    public String getFront() {
        return front;
    }

    public Card setFront(String front) {
        this.front = front;
        return this;
    }

    public String getBack() {
        return back;
    }

    public Card setBack(String back) {
        this.back = back;
        return this;
    }



}
