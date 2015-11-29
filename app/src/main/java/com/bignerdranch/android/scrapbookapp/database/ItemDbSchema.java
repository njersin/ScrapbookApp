package com.bignerdranch.android.scrapbookapp.database;

public class ItemDbSchema {

    public static final class ItemTable {
        public static final String NAME = "items";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String NOTE = "note";
            public static final String HASHTAG = "hashtag";
            public static final String DATE = "date";
            public static final String TYPE = "type";
        }
    }
}
