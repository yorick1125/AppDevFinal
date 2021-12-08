package com.example.flashcardapplication.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.flashcardapplication.enums.Subjects;
import com.example.flashcardapplication.sqlite.Column;
import com.example.flashcardapplication.sqlite.DatabaseException;
import com.example.flashcardapplication.sqlite.Table;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DeckTable extends Table<Deck> {

    public static final String TABLE_NAME = "deck";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DUEDATE = "duedate";
    public static final String COLUMN_SUBJECT = "subject";


    /**
     * Create a database table
     *
     * @param dbh  the handler that connects to the sqlite database.
     */
    public DeckTable(SQLiteOpenHelper dbh) {
        super(dbh, TABLE_NAME);
        addColumn(new Column(COLUMN_TITLE, Column.Type.TEXT));
        addColumn(new Column(COLUMN_DUEDATE, Column.Type.TEXT));
        addColumn(new Column(COLUMN_SUBJECT, Column.Type.TEXT));
    }

    @Override
    protected ContentValues toContentValues(Deck deck) throws DatabaseException {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, deck.getTitle());
        if(deck.getDueDate() != null){
            values.put(COLUMN_DUEDATE, deck.getDueDate().getTime());
        }
        if(deck.getSubject() != null){
            values.put(COLUMN_SUBJECT, deck.getSubject().ordinal());
        }
        return values;
    }

    @Override
    protected Deck fromCursor(Cursor cursor) throws DatabaseException {
        Deck deck = new Deck()
                .setDeckId(cursor.getLong(0))
                .setTitle(cursor.getString(1))
                .setDueDate(new Date(cursor.getLong(2)))
                .setSubject(Subjects.values()[cursor.getInt(3)]);
        return deck;
    }

    @Override
    public boolean hasInitialData() {
        return true;
    }

    @Override
    public void initialize(SQLiteDatabase database) {
        // to do this is just placeholder code
        Deck deck = new Deck("history");
        deck.setDueDate(new Date());
        Deck deck2 = new Deck("science");
        deck2.setDueDate(new Date());
        List<Card> cardList = new ArrayList<>();
        List<Card> cardList2 = new ArrayList<>();
        cardList.add(new Card(3L, "when did ww1 end?", "1918", 1L));
        cardList.add(new Card(3L, "when did ww2 end?", "1945", 1L));
        cardList2.add(new Card(3L, "another name for h20", "wata", 2L));
        cardList2.add(new Card(3L, "3 states of matter", "solid, liquid, gas", 2L));
        deck.setCards(cardList);
        deck2.setCards(cardList2);
        try {
            database.insert(TABLE_NAME, null, toContentValues(deck));
            database.insert(TABLE_NAME, null, toContentValues(deck2));
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }
}
