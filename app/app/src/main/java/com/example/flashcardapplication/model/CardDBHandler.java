package com.example.flashcardapplication.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.flashcardapplication.sqlite.Table;

public class CardDBHandler extends SQLiteOpenHelper {


    public static final String DATABASE_FILE_NAME = "tasks.db";
    public static final int DATABASE_VERSION = 1;

    private Table<Card> cardTable;

    public CardDBHandler(@Nullable Context context) {
        super(context, DATABASE_FILE_NAME, null, DATABASE_VERSION);
        cardTable = new CardTable(this);
    }

    public Table<Card> getCardTable() {
        return cardTable;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        cardTable.createTable(database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int i, int i1) {
        // not sure what needs to go in here
        //database.execSQL("DROP TABLE IF EXISTS " + cardTable.getName());
        //onCreate(database);
    }
}
