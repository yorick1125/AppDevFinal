package com.example.flashcardapplication.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.flashcardapplication.sqlite.Table;

public class DeckDBHandler extends SQLiteOpenHelper {


    public static final String DATABASE_FILE_NAME = "tasks.db";
    public static final int DATABASE_VERSION = 1;

    private Table<Deck> deckTable;

    public DeckDBHandler(@Nullable Context context) {
        super(context, DATABASE_FILE_NAME, null, DATABASE_VERSION);
        deckTable = new DeckTable(this);
    }

    public Table<Deck> getDeckTable() {
        return deckTable;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        deckTable.createTable(database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int i, int i1) {
        // to do im not sure what goes here
        //database.execSQL("DROP TABLE IF EXISTS " + deckTable.getName());
        //onCreate(database);
    }
}