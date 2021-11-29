package com.example.flashcardapplication.model;

import com.example.flashcardapplication.enums.Subjects;
import com.example.flashcardapplication.sqlite.Identifiable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Deck implements Identifiable<Long> {
    private Long id;
    private String title;
    private List<Card> cards;
    private Date dueDate;
    private Subjects subject;

    public static List<Deck> getDefaultDecks(){
        List<Deck> cards = new ArrayList<>();
        cards.add(new Deck().setTitle("title"));
        cards.add(new Deck().setTitle("title"));
        cards.add(new Deck().setTitle("title"));
        return cards;
    }

    public Deck(){
        this.id = 0L;
        this.title = "";
    }

    public Deck(Long id, String title){
        this.id = id;
        this.title = title;
    }

    public Deck(Long id, String title, List<Card> cards, Date dueDate, Subjects subject){
        this.id = id;
        this.title = title;
        this.cards = cards;
        this.dueDate = dueDate;
        this.subject = subject;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public Deck setDueDate(Date dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public Subjects getSubject() {
        return subject;
    }

    public Deck setSubject(Subjects subject) {
        this.subject = subject;
        return this;
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
