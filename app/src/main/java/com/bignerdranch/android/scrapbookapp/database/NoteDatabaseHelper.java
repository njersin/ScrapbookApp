package com.bignerdranch.android.scrapbookapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bignerdranch.android.scrapbookapp.database.NoteDbSchema.NoteTable;

public class NoteDatabaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "noteDatabase.db";

    public NoteDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + NoteTable.NAME + "(" +
                " _uniqueid integer primary key autoincrement, " +
                NoteTable.Cols.UUID + ", " +
                NoteTable.Cols.NOTE + ", " +
                NoteTable.Cols.DATE + ", " +
                NoteTable.Cols.HASHTAG +
                ")"
            );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
