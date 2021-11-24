package com.example.flashcardapplication.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.flashcardapplication.sqlite.Column;
import com.example.flashcardapplication.sqlite.DatabaseException;
import com.example.flashcardapplication.sqlite.Table;

public class DeckTable extends Table<Deck> {

    public static final String TABLE_NAME = "deck";
    public static final String COLUMN_TITLE = "title";


    /**
     * Create a database table
     *
     * @param dbh  the handler that connects to the sqlite database.
     */
    public DeckTable(SQLiteOpenHelper dbh) {
        super(dbh, TABLE_NAME);
        addColumn(new Column(COLUMN_TITLE, Column.Type.TEXT));
    }

    @Override
    protected ContentValues toContentValues(Deck deck) throws DatabaseException {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, deck.getTitle());
        return values;
    }

    @Override
    protected Deck fromCursor(Cursor cursor) throws DatabaseException {
        Deck deck = new Deck()
                .setDeckId(cursor.getLong(0))
                .setTitle(cursor.getString(1));
        return deck;
    }

    @Override
    public boolean hasInitialData() {
        return true;
    }

    @Override
    public void initialize(SQLiteDatabase database) {
        // to do this is just placeholder code
        Deck deck = new Deck();
        try {
            database.insert(TABLE_NAME, null, toContentValues(deck));
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }
}
