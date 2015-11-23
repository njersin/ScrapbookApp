package com.bignerdranch.android.scrapbookapp.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.bignerdranch.android.scrapbookapp.Note;
import com.bignerdranch.android.scrapbookapp.database.NoteDbSchema.NoteTable;

import java.util.Date;
import java.util.UUID;

public class NoteCursorWrapper extends CursorWrapper {
    public NoteCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Note getNote() {
        String uuidString = getString(getColumnIndex(NoteTable.Cols.UUID));
        String noteText = getString(getColumnIndex(NoteTable.Cols.NOTE));
        String hashtag = getString(getColumnIndex(NoteTable.Cols.HASHTAG));
        long date = getLong(getColumnIndex(NoteTable.Cols.DATE));

        Note note = new Note(UUID.fromString(uuidString));
        note.setMemoText(noteText);
        note.setHashtag(hashtag);
        note.setDate(new Date(date));

        return note;
    }
}
