package com.bignerdranch.android.scrapbookapp;

import java.util.Date;
import java.util.UUID;

public class Picture extends Item {

    public Picture() {
        super();
        this.setItemType("picture");
        count++;
    }

    public Picture(UUID id) {
        super(id);
        this.setItemType("picture");
        count++;
    }
}
