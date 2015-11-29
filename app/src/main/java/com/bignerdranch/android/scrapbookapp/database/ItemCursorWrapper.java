package com.bignerdranch.android.scrapbookapp.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.bignerdranch.android.scrapbookapp.Item;
import com.bignerdranch.android.scrapbookapp.Note;
import com.bignerdranch.android.scrapbookapp.database.ItemDbSchema.ItemTable;

import java.util.Date;
import java.util.UUID;

public class ItemCursorWrapper extends CursorWrapper {

    public ItemCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Item getItem() {
        String uuidString = getString(getColumnIndex(ItemTable.Cols.UUID));
        String noteText = getString(getColumnIndex(ItemTable.Cols.NOTE));
        String hashtag = getString(getColumnIndex(ItemTable.Cols.HASHTAG));
        String type = getString(getColumnIndex(ItemTable.Cols.TYPE));
        long date = getLong(getColumnIndex(ItemTable.Cols.DATE));

        Item item = new Item(UUID.fromString(uuidString));
        item.setMemoText(noteText);
        item.setHashtag(hashtag);
        item.setItemType(type);
        item.setDate(new Date(date));

        return item;
    }
}
