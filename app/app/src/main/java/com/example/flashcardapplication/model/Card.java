package com.example.flashcardapplication.model;

import android.net.Uri;

import com.example.flashcardapplication.sqlite.Identifiable;

import java.util.ArrayList;
import java.util.List;

public class Card implements Identifiable<Long> {
    private Long id;
    private String front;
    private String back;
    private Long deckId;
    private Uri imageUri;

    public static List<Card> getDefaultCards(){
        List<Card> cards = new ArrayList<>();
        cards.add(new Card("front", "back", 1L));
        cards.add(new Card("front", "back", 1L));
        cards.add(new Card("front", "back", 1L));
        return cards;
    }

    public Card(){
        this.id = 0L;
        this.front = "";
        this.back = "";
        this.deckId = -1L;
        this.imageUri = null;
    }
    public Card(String front, String back, Long deckId){
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

    public Long getDeckId(){
        return deckId;
    }
    public Card setDeckId(Long deckId){
        this.deckId = deckId;
        return this;
    }
    public Uri getUri() {
        return imageUri;
    }

    public Card setUri(Uri imageUri) {
        this.imageUri = imageUri;
        return this;
    }

    public Card setUri(String path) {
        if(path == null || path == ""){
            return this;
        }

        this.imageUri = Uri.parse(path);
        return this;
    }






}
