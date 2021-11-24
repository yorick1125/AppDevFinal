package com.example.flashcardapplication.model;

import com.example.flashcardapplication.sqlite.Identifiable;

import java.util.List;

public class Deck implements Identifiable<Long> {
    private Long id;
    private String title;
    private List<Card> cards;

    public Deck(){
        this.id = 0L;
        this.title = "";
    }

    public Deck(Long id, String title){
        this.id = id;
        this.title = title;
    }

    public Long getId(){
        return id;
    }

    @Override
    public Identifiable<Long> setId(Long id) {
        this.id = (long)id;
        return this;
    }


    public Deck setDeckId(Long id){
        this.id = id;
        return this;
    }

    public String getTitle(){
        return title;
    }

    public Deck setTitle(String title){
        this.title = title;
        return this;
    }

    public static void create(){

    }
    public static void read(int id){

    }

    public List<Card> getCards() {
        return cards;
    }

    public Deck setCards(List<Card> cards) {
        this.cards = cards;
        return this;
    }

    public void update(int id, String title){

    }
    public void delete(){

    }
}
