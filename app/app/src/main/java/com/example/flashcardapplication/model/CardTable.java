package com.example.flashcardapplication.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.flashcardapplication.sqlite.Column;
import com.example.flashcardapplication.sqlite.DatabaseException;
import com.example.flashcardapplication.sqlite.Table;

public class CardTable extends Table<Card> {

    public static final String TABLE_NAME = "card";
    public static final String COLUMN_FRONT = "front";
    public static final String COLUMN_BACK = "back";


    /**
     * Create a database table
     *
     * @param dbh  the handler that connects to the sqlite database.
     */
    public CardTable(SQLiteOpenHelper dbh) {
        super(dbh, TABLE_NAME);
        addColumn(new Column(COLUMN_FRONT, Column.Type.TEXT));
        addColumn(new Column(COLUMN_BACK, Column.Type.TEXT));
    }

    @Override
    protected ContentValues toContentValues(Card card) throws DatabaseException {
        ContentValues values = new ContentValues();
        values.put(COLUMN_FRONT, card.getFront());
        values.put(COLUMN_BACK, card.getBack());
        return values;
    }

    @Override
    protected Card fromCursor(Cursor cursor) throws DatabaseException {
        Card card = new Card()
                .setCardId(cursor.getLong(0))
                .setFront(cursor.getString(1))
                .setBack(cursor.getString(3));
        return card;
    }

    @Override
    public boolean hasInitialData() {
        return true;
    }

    @Override
    public void initialize(SQLiteDatabase database) {
        // to do this is just placeholder code
        Card card = new Card();
        try {
            database.insert(TABLE_NAME, null, toContentValues(card));
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }
}
