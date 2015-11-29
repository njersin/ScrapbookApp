package com.bignerdranch.android.scrapbookapp;

import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

public class ItemListFragment extends Fragment{

    private RecyclerView mItemRecyclerView;
    private ItemAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_list, container, false);

        mItemRecyclerView = (RecyclerView)view.findViewById(R.id.item_recycler_view);
        mItemRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    private class ItemHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private Item mItem;
        private ImageView mThumbnailImageView;
        private TextView mNoteTitleTextView;
        private TextView mDateTextView;

        public ItemHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mThumbnailImageView = (ImageView) itemView.findViewById(R.id.list_item_thumbnail_view);
            mNoteTitleTextView = (TextView) itemView.findViewById(R.id.list_item_note_title_text_view);
            mDateTextView = (TextView) itemView.findViewById(R.id.list_item_note_date_text_view);
        }

        @Override
        public void onClick(View v) {
            Intent intent = ItemPagerActivity.newIntent(getActivity(), mItem.getID());
            startActivity(intent);
        }

        public void bindItem(Item item) {
            mItem = item;
            if (item.getItemType().equalsIgnoreCase("picture")) {
                File pictureFile = ItemLab.get(getActivity()).getPictureFile(item);
                if (pictureFile == null || !pictureFile.exists()) {
                    mThumbnailImageView.setImageDrawable(null);
                } else {
                    Bitmap bitmap = PictureUtils.getScaledBitmap(pictureFile.getPath(), 60, 60);
                    mThumbnailImageView.setImageBitmap(bitmap);
                }
            }
            mNoteTitleTextView.setText(mItem.getMemoText());
            mDateTextView.setText(mItem.getDate().toString());
        }
    }

    private class ItemAdapter extends RecyclerView.Adapter<ItemHolder> {
        private List<Item> mItems;

        public ItemAdapter(List<Item> items) {
            mItems = items;
        }

        @Override
        public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_note, parent, false);
            return new ItemHolder(view);
        }

        @Override
        public void onBindViewHolder(ItemHolder holder, int position) {
            Item item = mItems.get(position);
            holder.bindItem(item);
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        public void setItems(List<Item> items){
            mItems = items;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_note_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.menu_item_new_note:
                Note note = new Note();
                ItemLab.get(getActivity()).addItem(note);
                intent = ItemPagerActivity.newIntent(getActivity(), note.getID());
                startActivity(intent);
                return true;
            case R.id.menu_item_new_picture:
                Picture picture = new Picture();
                ItemLab.get(getActivity()).addItem(picture);
                intent = ItemPagerActivity.newIntent(getActivity(), picture.getID());
                startActivity(intent);
                return true;
            case R.id.menu_item_search_note:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        ItemLab itemLab = ItemLab.get(getActivity());
        List<Item> items = itemLab.getItems();

        if (mAdapter == null) {
            mAdapter = new ItemAdapter(items);
            mItemRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setItems(items);
            mAdapter.notifyDataSetChanged();
        }

        updateTitle();
    }

    public void updateTitle() {
        ItemLab itemLab = ItemLab.get(getActivity());
        int itemsCount = itemLab.getItems().size();
        String title = getString(R.string.title_format, itemsCount);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setTitle(title);
    }

}
