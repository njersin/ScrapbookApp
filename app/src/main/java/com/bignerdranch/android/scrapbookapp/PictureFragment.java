package com.bignerdranch.android.scrapbookapp;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.util.UUID;

public class PictureFragment extends Fragment {

    private static final String ARG_PICTURE_ID = "picture_id";
    private static final int REQUEST_PICTURE = 3;

    private Item mPictureItem;

    private ImageView mPictureView;
    private ImageButton mPictureButton;
    private EditText mPictureHashtagField;
    private File mPictureFile;

    public static PictureFragment newInstance(UUID pictureID) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_PICTURE_ID, pictureID);

        PictureFragment pictureFragment = new PictureFragment();
        pictureFragment.setArguments(args);
        return pictureFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        UUID itemID = (UUID) getArguments().getSerializable(ARG_PICTURE_ID);
        mPictureItem = ItemLab.get(getActivity()).getItem(itemID);
        mPictureFile = ItemLab.get(getActivity()).getPictureFile(mPictureItem);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_picture, container, false);
        PackageManager packageManager = getActivity().getPackageManager();

        mPictureButton = (ImageButton)view.findViewById(R.id.picture_button);
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        boolean canTakePhoto = mPictureFile != null && captureImage.resolveActivity(packageManager) != null;
        mPictureButton.setEnabled(canTakePhoto);

        if (canTakePhoto) {
            Uri uri = Uri.fromFile(mPictureFile);
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }

        mPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(captureImage, REQUEST_PICTURE);
            }
        });

        mPictureHashtagField = (EditText)view.findViewById(R.id.picture_hashtag_field);
        mPictureHashtagField.setText(mPictureItem.getHashtag());
        mPictureHashtagField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPictureItem.setHashtag(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mPictureView = (ImageView)view.findViewById(R.id.picture_image_view);
        updatePictureView();

        updateTitle();
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_PICTURE) {
            updatePictureView();
        }
    }

    public void updateTitle() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setTitle("");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_picture, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_delete_picture:
                ItemLab.get(getActivity()).deleteItem(mPictureItem);
                getActivity().finish();
                return true;
            case R.id.menu_item_save_picture:
                ItemLab.get(getActivity()).updateItem(mPictureItem);
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        ItemLab.get(getActivity()).updateItem(mPictureItem);
    }

    private void updatePictureView() {
        if (mPictureFile == null || !mPictureFile.exists()) {
            mPictureView.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaleBitmap(mPictureFile.getPath(), getActivity());
            mPictureView.setImageBitmap(bitmap);
        }
    }
}
