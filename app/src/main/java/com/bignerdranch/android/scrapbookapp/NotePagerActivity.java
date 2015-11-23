package com.bignerdranch.android.scrapbookapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.UUID;

public class NotePagerActivity extends AppCompatActivity {

    private static final String EXTRA_NOTE_ID = "com.bignerdranch.android.scrapbookapp.note_id";

    private ViewPager mViewPager;
    private List<Note> mNotes;

    public static Intent newIntent(Context packageContext, UUID noteID) {
        Intent intent = new Intent(packageContext, NotePagerActivity.class);
        intent.putExtra(EXTRA_NOTE_ID, noteID);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_pager);

        UUID noteID = (UUID) getIntent().getSerializableExtra(EXTRA_NOTE_ID);

        mViewPager = (ViewPager) findViewById(R.id.activity_note_pager_view_pager);

        mNotes = NoteLab.get(this).getNotes();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentPagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Note note = mNotes.get(position);
                return NoteFragment.newInstance(note.getID());
            }

            @Override
            public int getCount() {
                return mNotes.size();
            }
        });

        for (int i = 0; i < mNotes.size(); i++) {
            if (mNotes.get(i).getID().equals(noteID)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }

    }
}
