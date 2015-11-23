package com.bignerdranch.android.scrapbookapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class NoteListFragment extends Fragment{

    private RecyclerView mNoteRecyclerView;
    private NoteAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_list, container, false);

        mNoteRecyclerView = (RecyclerView)view.findViewById(R.id.note_recycler_view);
        mNoteRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    private class NoteHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private Note mNote;
        private TextView mNoteTitleTextView;
        private TextView mDateTextView;

        public NoteHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mNoteTitleTextView = (TextView) itemView.findViewById(R.id.list_item_note_title_text_view);
            mDateTextView = (TextView) itemView.findViewById(R.id.list_item_note_date_text_view);
        }

        @Override
        public void onClick(View v) {
            Intent intent = NotePagerActivity.newIntent(getActivity(), mNote.getID());
            startActivity(intent);
        }

        public void bindNote(Note note) {
            mNote = note;
            mNoteTitleTextView.setText(mNote.getMemoText());
            mDateTextView.setText(mNote.getDate().toString());
        }
    }

    private class NoteAdapter extends RecyclerView.Adapter<NoteHolder> {
        private List<Note> mNotes;

        public NoteAdapter(List<Note> notes) {
            mNotes = notes;
        }

        @Override
        public NoteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_note, parent, false);
            return new NoteHolder(view);
        }

        @Override
        public void onBindViewHolder(NoteHolder holder, int position) {
            Note note = mNotes.get(position);
            holder.bindNote(note);
        }

        @Override
        public int getItemCount() {
            return mNotes.size();
        }

        public void setNotes(List<Note> notes){
            mNotes = notes;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_note_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_note:
                Note note = new Note();
                NoteLab.get(getActivity()).addNote(note);
                Intent intent = NotePagerActivity.newIntent(getActivity(), note.getID());
                startActivity(intent);
                return true;
            case R.id.menu_item_search_note:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateTitle() {
        NoteLab noteLab = NoteLab.get(getActivity());
        int notesCount = noteLab.getNotes().size();
        String title = getString(R.string.title_format, notesCount);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setTitle(title);

    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        NoteLab noteLab = NoteLab.get(getActivity());
        List<Note> notes = noteLab.getNotes();

        if (mAdapter == null) {
            mAdapter = new NoteAdapter(notes);
            mNoteRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setNotes(notes);
            mAdapter.notifyDataSetChanged();
        }

        updateTitle();
    }


}
