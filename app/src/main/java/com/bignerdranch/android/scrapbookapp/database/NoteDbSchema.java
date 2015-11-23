package com.bignerdranch.android.scrapbookapp.database;

public class NoteDbSchema {

    public static final class NoteTable {
        public static final String NAME = "notes";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String NOTE = "note";
            public static final String HASHTAG = "hashtag";
            public static final String DATE = "date";
        }
    }
}
