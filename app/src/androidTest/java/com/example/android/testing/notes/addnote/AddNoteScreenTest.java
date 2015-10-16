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

package com.example.android.testing.notes.addnote;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.app.Activity;
import android.app.Instrumentation.ActivityResult;
import android.provider.MediaStore;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.example.android.testing.notes.R;
import com.example.android.testing.notes.notes.NotesActivity;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.example.android.testing.notes.custom.matcher.ImageViewHasDrawableMatcher.hasDrawable;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AddNoteScreenTest {

    @Rule
    public IntentsTestRule<AddNoteActivity> mAddNoteIntentsTestRule =
            new IntentsTestRule<>(AddNoteActivity.class);

    @Test
    public void addImageToNote_ShowsThumbnailInUi() {
        // Stub take image Intent.
        ActivityResult result = createImageCaptureActivityResultStub();
        intending(hasAction(MediaStore.ACTION_IMAGE_CAPTURE)).respondWith(result);

        // Open add notes screen
        onView(withId(R.id.fab_notes)).perform(click());

        // Check thumbnail view is not shown
        onView(withId(R.id.add_note_image_thumbnail)).check(matches(not(isDisplayed())));
        selectTakeImageFromMenu();

        onView(withId(R.id.add_note_image_thumbnail))
                .check(matches(allOf(
                        hasDrawable(),
                        isDisplayed())));
    }

    @Before
    public void registerIdlingResource() {
        Espresso.registerIdlingResources(
                mAddNoteIntentsTestRule.getActivity().getCountingIdlingResource());
    }

    @Test
    public void errorShownOnEmptyMessage() {
        onView(withId(R.id.fab_notes)).perform(click());
        // Add note title and description
        onView(withId(R.id.add_note_title)).perform(typeText(""));
        onView(withId(R.id.add_note_description)).perform(typeText(""),
                closeSoftKeyboard());
        // Save the note
        onView(withId(R.id.fab_notes)).perform(click());

        // Verify empty notes snackbar is shown
        String emptyNoteMessageText = getTargetContext().getString(R.string.empty_note_message);
        onView(withText(emptyNoteMessageText)).check(matches(isDisplayed()));
    }

    @After
    public void unregisterIdlingResource() {
        Espresso.unregisterIdlingResources(
                mAddNoteIntentsTestRule.getActivity().getCountingIdlingResource());
    }


    private void selectTakeImageFromMenu() {
        openActionBarOverflowOrOptionsMenu(getTargetContext());
        onView(withText(R.string.add_picture)).perform(click());
    }

    private ActivityResult createImageCaptureActivityResultStub() {
        // Create the ActivityResult with a null Intent.
        return new ActivityResult(Activity.RESULT_OK, null);
    }

}