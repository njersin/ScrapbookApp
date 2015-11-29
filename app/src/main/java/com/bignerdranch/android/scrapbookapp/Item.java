package com.bignerdranch.android.scrapbookapp;

import java.util.Date;
import java.util.UUID;

public class Item {

    private UUID mID;
    private String mHashtag;
    private Date mDate;
    private String mItemType;
    private String mMemoText;

    protected static int count = 0;

    public Item() {
        this(UUID.randomUUID());
        this.mDate = new Date();
    }

    public Item(UUID id) {
        mID = id;
        mDate = new Date();
    }

    public UUID getID() {
        return mID;
    }

    public String getHashtag() {
        return mHashtag;
    }

    public void setHashtag(String hashtag) {
        mHashtag = hashtag;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public String getItemType() {
        return mItemType;
    }

    public void setItemType(String itemType) {
        mItemType = itemType;
    }

    public String getMemoText() {
        return mMemoText;
    }

    public void setMemoText(String memoText) {
        mMemoText = memoText;
    }

    public static int getCount() {
        return count;
    }

    public String getPictureFilename() {
        return "IMG_" + getID().toString() + ".jpg";
    }

}
