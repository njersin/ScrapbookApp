package com.bignerdranch.android.scrapbookapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bignerdranch.android.scrapbookapp.database.ItemDbSchema.ItemTable;

public class ItemDatabaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "itemDatabase.db";

    public ItemDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + ItemTable.NAME + "(" +
                " _uniqueid integer primary key autoincrement, " +
                ItemTable.Cols.UUID + ", " +
                ItemTable.Cols.NOTE + ", " +
                ItemTable.Cols.DATE + ", " +
                ItemTable.Cols.HASHTAG + ", " +
                ItemTable.Cols.TYPE +
                ")"
            );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
