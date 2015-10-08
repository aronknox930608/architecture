/*
 * Copyright 2015, The Android Open Source Project
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

package com.example.android.testing.notes.model;

import android.support.annotation.NonNull;

import com.google.common.collect.ImmutableList;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Concrete implementation to load notes from the a data source.
 */
public class InMemoryNotesRepository implements NotesRepository {

    private final NotesServiceApi mNotesServiceApi;

    private List<Note> mCachedNotes;

    public InMemoryNotesRepository(@NonNull NotesServiceApi notesServiceApi) {
        mNotesServiceApi = checkNotNull(notesServiceApi);
    }

    @Override
    public void getNotes(final LoadNotesCallback callback) {
        // Load from API only if needed.
        if (mCachedNotes == null) {
            mNotesServiceApi.getAllNotes(new NotesServiceApi.NotesServiceCallback<List<Note>>() {
                @Override
                public void onLoaded(List<Note> notes) {
                    mCachedNotes = ImmutableList.copyOf(notes);
                    callback.onNotesLoaded(mCachedNotes);
                }
            });
        } else {
            callback.onNotesLoaded(mCachedNotes);
        }
    }

    @Override
    public void invalidateCache() {
        mCachedNotes = null;
    }

}
