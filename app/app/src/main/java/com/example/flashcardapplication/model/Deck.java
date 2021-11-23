package com.example.flashcardapplication.model;

import com.example.flashcardapplication.sqlite.Identifiable;

public class Deck implements Identifiable<Long> {
    private Long id;
    private String title;

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
    public void update(int id, String front, String back){

    }
    public void delete(){

    }
}
