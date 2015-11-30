package com.bignerdranch.android.scrapbookapp;

import android.content.Intent;
import android.support.v4.app.Fragment;

public class ItemListActivity extends SingleFragmentActivity {

    @Override
    public void onNewIntent(Intent intent) {
        setIntent(intent);
    }

    @Override
    protected Fragment createFragment(){
        return new ItemListFragment();
    }
}
