package com.example.flashcardapplication.model;

import com.example.flashcardapplication.enums.Subjects;
import com.example.flashcardapplication.sqlite.Identifiable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class   Deck implements Identifiable<Long> {
    private static SimpleDateFormat formatter =  new SimpleDateFormat("EEEE, MMMM dd, YYYY @ h:mm aa");

    private Long id;
    private String title;
    private List<Card> cards;
    private Date dueDate;
    private Subjects subject;

    public static List<Deck> getDefaultDecks(){
        List<Deck> cards = new ArrayList<>();
        cards.add(new Deck().setTitle("Deck 1"));
        cards.add(new Deck().setTitle("Deck 2"));
        cards.add(new Deck().setTitle("Deck 3"));
        return cards;
    }

    public Deck(){
        this.id = 0L;
        this.title = "";
        this.cards = new ArrayList<>();
    }

    public Deck(String title){

        this.title = title;
    }

    public Deck(String title, List<Card> cards, Date dueDate, Subjects subject){
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

    public String getDueString() {
        return dueDate != null ? formatter.format(dueDate): "";
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


    public List<Card> getCards() {
        return cards;
    }

    public Deck setCards(List<Card> cards) {
        this.cards = cards;
        return this;
    }

}
