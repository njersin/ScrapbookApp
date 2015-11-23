package com.bignerdranch.android.scrapbookapp;


import java.util.Date;
import java.util.UUID;

public class Note {

    private UUID mID;
    private String mMemoText;
    private String mHashtag;
    private Date mDate;

    public Note() {
        this(UUID.randomUUID());
    }

    public Note(UUID id) {
        mID = id;
        mDate = new Date();
    }

    public UUID getID() {
        return mID;
    }

    public String getMemoText() {
        return mMemoText;
    }

    public void setMemoText(String memoText) {
        mMemoText = memoText;
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

}
