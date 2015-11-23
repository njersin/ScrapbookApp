package com.bignerdranch.android.scrapbookapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

public class NoteActivity extends SingleFragmentActivity {

    private static final String EXTRA_NOTE_ID = "com.bignerdranch.android.scrapbookapp.note_id";

    public static Intent newIntent(Context packageContext, UUID noteID) {
        Intent intent = new Intent(packageContext, NoteActivity.class);
        intent.putExtra(EXTRA_NOTE_ID, noteID);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        UUID noteID = (UUID) getIntent().getSerializableExtra(EXTRA_NOTE_ID);
        return NoteFragment.newInstance(noteID);
    }

}
