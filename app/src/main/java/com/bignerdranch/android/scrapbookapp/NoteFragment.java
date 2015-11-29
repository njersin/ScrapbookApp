package com.bignerdranch.android.scrapbookapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.UUID;

public class NoteFragment extends Fragment {

    private static final String ARG_NOTE_ID = "note_id";

    private Item mNote;
    private EditText mNoteField;
    private EditText mHashtagField;
    private Button mDateButton;

    public static NoteFragment newInstance(UUID noteID) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_NOTE_ID, noteID);

        NoteFragment noteFragment = new NoteFragment();
        noteFragment.setArguments(args);
        return noteFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        UUID noteID = (UUID) getArguments().getSerializable(ARG_NOTE_ID);
        mNote = ItemLab.get(getActivity()).getItem(noteID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note, container, false);

        mNoteField = (EditText)view.findViewById(R.id.note_text);
        mNoteField.setText(mNote.getMemoText());
        mNoteField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mNote.setMemoText(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mHashtagField = (EditText)view.findViewById(R.id.note_hashtag);
        mHashtagField.setText(mNote.getHashtag());
        mHashtagField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mNote.setHashtag(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mDateButton = (Button)view.findViewById(R.id.note_date_button);
        mDateButton.setText(mNote.getDate().toString());
        mDateButton.setEnabled(false);

        updateTitle();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_note, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_delete_note:
                ItemLab.get(getActivity()).deleteItem(mNote);
                getActivity().finish();
                return true;
            case R.id.menu_item_save_note:
                ItemLab.get(getActivity()).updateItem(mNote);
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateTitle() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setTitle("");
    }

    @Override
    public void onPause() {
        super.onPause();
        ItemLab.get(getActivity()).updateItem(mNote);
    }
}
