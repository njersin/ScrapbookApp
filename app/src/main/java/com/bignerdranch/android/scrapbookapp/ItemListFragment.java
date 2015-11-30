package com.bignerdranch.android.scrapbookapp;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
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
    private String mSearchQuery;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mSearchQuery = "";
        // Get the search intent, verify the action and get the query
        handleSearchIntent(getActivity().getIntent());
    }

    private void handleSearchIntent(Intent intent) {
       if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            mSearchQuery = query;
//            processSearchQuery(query);
        }
    }

    private void processSearchQuery(String query) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

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
        inflater.inflate(R.menu.fragment_item_list, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_item_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));  // Assumes current activity is the searchable activity
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        Intent intent;
        switch (menuItem.getItemId()) {
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
            case R.id.menu_item_search:
                getActivity().onSearchRequested();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        ItemLab itemLab = ItemLab.get(getActivity());
        List<Item> items;

        if (mSearchQuery.isEmpty()) {
            items  = itemLab.getItems();
        } else {
            items = itemLab.searchItems(mSearchQuery);
            mSearchQuery = "";
        }

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
