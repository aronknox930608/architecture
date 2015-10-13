/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.testing.notes.view;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.android.testing.notes.Injection;
import com.example.android.testing.notes.R;
import com.example.android.testing.notes.presenter.NoteDetailPresenter;
import com.example.android.testing.notes.presenter.NoteDetailPresenterImpl;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class NoteDetailFragment extends Fragment implements NoteDetailView {

    public static final String ARGUMENT_NOTE_ID = "NOTE_ID";

    private NoteDetailPresenter mNoteDetailPresenter;

    private TextView mDetailTitle;

    private TextView mDetailDescription;

    private ImageView mDetailImage;

    public static NoteDetailFragment newInstance(String noteId) {
        Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_NOTE_ID, noteId);
        NoteDetailFragment fragment = new NoteDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mNoteDetailPresenter = new NoteDetailPresenterImpl(Injection.provideNotesRepository(),
                this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_detail, container, false);
        mDetailTitle = (TextView) root.findViewById(R.id.note_detail_title);
        mDetailDescription = (TextView) root.findViewById(R.id.note_detail_description);
        mDetailImage = (ImageView) root.findViewById(R.id.note_detail_image);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        final String noteId = getArguments().getString(ARGUMENT_NOTE_ID);
        mNoteDetailPresenter.openNote(noteId);
    }

    @Override
    public void setProgressIndicator(boolean active) {
        // TODO
        if (active) {
            mDetailTitle.setText("");
            mDetailDescription.setText("LOADING");
        }
    }

    @Override
    public void hideDescription() {
        mDetailDescription.setVisibility(View.GONE);
    }

    @Override
    public void hideTitle() {
        mDetailTitle.setVisibility(View.GONE);
    }

    @Override
    public void showDescription(String description) {
        mDetailDescription.setVisibility(View.VISIBLE);
        mDetailDescription.setText(description);
    }

    @Override
    public void showTitle(String title) {
        mDetailTitle.setVisibility(View.VISIBLE);
        mDetailTitle.setText(title);
    }

    @Override
    public void showImage(String imageUrl) {
        mDetailImage.setVisibility(View.VISIBLE);
        Glide.with(this)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(mDetailImage);
    }

    @Override
    public void hideImage() {
        mDetailImage.setImageDrawable(null); //TODO: check if this is right/useful.
        mDetailImage.setVisibility(View.GONE);
    }

    @Override
    public void showMissingNote() {
        // TODO
        mDetailTitle.setText("");
        mDetailDescription.setText("No data");
    }
}
