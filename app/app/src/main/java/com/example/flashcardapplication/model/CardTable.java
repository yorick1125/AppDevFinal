package com.example.flashcardapplication.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.example.flashcardapplication.sqlite.Column;
import com.example.flashcardapplication.sqlite.DatabaseException;
import com.example.flashcardapplication.sqlite.Table;

import java.net.URI;

public class CardTable extends Table<Card> {

    public static final String TABLE_NAME = "card";
    public static final String COLUMN_FRONT = "front";
    public static final String COLUMN_BACK = "back";
    public static final String COLUMN_DECKID = "deckId";
    public static final String COLUMN_URI = "uri";


    /**
     * Create a database table
     *
     * @param dbh  the handler that connects to the sqlite database.
     */
    public CardTable(SQLiteOpenHelper dbh) {
        super(dbh, TABLE_NAME);
        addColumn(new Column(COLUMN_FRONT, Column.Type.TEXT));
        addColumn(new Column(COLUMN_BACK, Column.Type.TEXT));
        addColumn(new Column(COLUMN_DECKID , Column.Type.TEXT));
        addColumn(new Column(COLUMN_URI , Column.Type.TEXT));
    }

    @Override
    protected ContentValues toContentValues(Card card) throws DatabaseException {
        ContentValues values = new ContentValues();
        values.put(COLUMN_FRONT, card.getFront());
        values.put(COLUMN_BACK, card.getBack());
        values.put(COLUMN_DECKID, card.getDeckId());
        if(card.getUri() != null){
            values.put(COLUMN_URI, card.getUri().toString());
        }

        return values;
    }

    @Override
    protected Card fromCursor(Cursor cursor) throws DatabaseException {
        Card card = new Card()
                .setCardId(cursor.getLong(0))
                .setFront(cursor.getString(1))
                .setBack(cursor.getString(2))
                .setDeckId(cursor.getLong(3))
                .setUri(Uri.parse(cursor.getString(4)));

        return card;
    }

    @Override
    public boolean hasInitialData() {
        return true;
    }

    @Override
    public void initialize(SQLiteDatabase database) {
        // to do this is just placeholder code
        Card card = new Card("when did ww1 end?", "1918", 1L);
        Card card2 = new Card("when did ww2 end?", "1945", 1L);
        Card card3 = new Card("what is photo synthesis", "synthesizing of photos", 2L);

        try {
            database.insert(TABLE_NAME, null, toContentValues(card));
            database.insert(TABLE_NAME, null, toContentValues(card2));
            database.insert(TABLE_NAME, null, toContentValues(card3));
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }
}
