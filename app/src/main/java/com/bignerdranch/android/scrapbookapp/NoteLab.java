package com.bignerdranch.android.scrapbookapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bignerdranch.android.scrapbookapp.database.NoteCursorWrapper;
import com.bignerdranch.android.scrapbookapp.database.NoteDatabaseHelper;
import com.bignerdranch.android.scrapbookapp.database.NoteDbSchema.NoteTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NoteLab {
    private static NoteLab sNoteLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static NoteLab get(Context context) {
        if (sNoteLab == null) {
            sNoteLab = new NoteLab(context);
        }
        return sNoteLab;
    }

    private NoteLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new NoteDatabaseHelper(mContext).getWritableDatabase();
    }

    public void addNote(Note note){
        ContentValues values = getContentValues(note);
        mDatabase.insert(NoteTable.NAME, null, values);
    }

    public void deleteNote(Note note) {
        ContentValues values = getContentValues(note);
        mDatabase.delete(NoteTable.NAME, NoteTable.Cols.UUID + " = ?", new String[]{note.getID().toString()});
    }

    public void updateNote(Note note) {
        ContentValues values = getContentValues(note);
        mDatabase.update(NoteTable.NAME, values, NoteTable.Cols.UUID + " = ?", new String[]{note.getID().toString()});
    }

    public List<Note> getNotes() {
        List<Note> notes = new ArrayList<>();
        NoteCursorWrapper cursor = queryNotes(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                notes.add(cursor.getNote());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return notes;
    }

    public Note getNote(UUID id) {
        NoteCursorWrapper cursor = queryNotes(NoteTable.Cols.UUID + " = ?", new String[] { id.toString() });

        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getNote();
        } finally {
            cursor.close();
        }
    }

    private static ContentValues getContentValues(Note note) {
        ContentValues values = new ContentValues();
        values.put(NoteTable.Cols.UUID, note.getID().toString());
        values.put(NoteTable.Cols.NOTE, note.getMemoText());
        values.put(NoteTable.Cols.HASHTAG, note.getHashtag());
        values.put(NoteTable.Cols.DATE, note.getDate().toString());
        return values;
    }

    private NoteCursorWrapper queryNotes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                NoteTable.NAME,
                null, //columns
                whereClause,
                whereArgs,
                null, //groupBy
                null, //having
                null  //orderBy
        );
        return new NoteCursorWrapper(cursor);
    }
}
