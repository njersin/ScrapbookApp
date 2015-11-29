package com.bignerdranch.android.scrapbookapp;

import java.util.UUID;

public class Note extends Item {

    public Note() {
        super();
        this.setItemType("note");
        count++;
    }

    public Note(UUID id) {
        super(id);
        this.setItemType("note");
        count++;
    }
}
