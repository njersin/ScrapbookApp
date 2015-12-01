package com.bignerdranch.android.scrapbookapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.bignerdranch.android.scrapbookapp.database.ItemCursorWrapper;
import com.bignerdranch.android.scrapbookapp.database.ItemDatabaseHelper;
import com.bignerdranch.android.scrapbookapp.database.ItemDbSchema.ItemTable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ItemLab {
    private static ItemLab sItemLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static ItemLab get(Context context) {
        if (sItemLab == null) {
            sItemLab = new ItemLab(context);
        }
        return sItemLab;
    }

    private ItemLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new ItemDatabaseHelper(mContext).getWritableDatabase();
    }

    public void addItem(Item item){
        ContentValues values = getContentValues(item);
        mDatabase.insert(ItemTable.NAME, null, values);
    }

    public void deleteItem(Item item) {
        ContentValues values = getContentValues(item);
        mDatabase.delete(ItemTable.NAME, ItemTable.Cols.UUID + " = ?", new String[]{item.getID().toString()});
    }

    public void updateItem(Item item) {
        ContentValues values = getContentValues(item);
        mDatabase.update(ItemTable.NAME, values, ItemTable.Cols.UUID + " = ?", new String[]{item.getID().toString()});
    }

    public List<Item> getItems() {
        List<Item> items = new ArrayList<>();
        ItemCursorWrapper cursor = queryItems(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                items.add(cursor.getItem());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return items;
    }

    public List<Item> searchItems(String query) {
        List<Item> items = new ArrayList<>();
        ItemCursorWrapper cursor = queryItems(ItemTable.Cols.HASHTAG + " = ?", new String[]{query});

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                items.add(cursor.getItem());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return items;
    }

    public Item getItem(UUID id) {
        ItemCursorWrapper cursor = queryItems(ItemTable.Cols.UUID + " = ?", new String[]{id.toString()});

        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getItem();
        } finally {
            cursor.close();
        }
    }

    public File getPictureFile(Item item) {
        File externalFilesDir = new File(Environment.getExternalStorageDirectory(), item.getPictureFilename());
        if (externalFilesDir == null) {
            return null;
        } else {
            return externalFilesDir;
        }
    }

    private static ContentValues getContentValues(Item item) {
        ContentValues values = new ContentValues();
        values.put(ItemTable.Cols.UUID, item.getID().toString());
        values.put(ItemTable.Cols.NOTE, item.getMemoText());
        values.put(ItemTable.Cols.HASHTAG, item.getHashtag());
        values.put(ItemTable.Cols.TYPE, item.getItemType());
        values.put(ItemTable.Cols.DATE, item.getDate().getTime());
        return values;
    }

    private ItemCursorWrapper queryItems(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                ItemTable.NAME,
                null, //columns
                whereClause,
                whereArgs,
                null, //groupBy
                null, //having
                ItemTable.Cols.DATE  + " DESC"//orderBy
        );
        return new ItemCursorWrapper(cursor);
    }

}
