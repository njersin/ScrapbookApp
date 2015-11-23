package com.bignerdranch.android.scrapbookapp;

import android.support.v4.app.Fragment;

public class NoteListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment(){
        return new NoteListFragment();
    }
}
