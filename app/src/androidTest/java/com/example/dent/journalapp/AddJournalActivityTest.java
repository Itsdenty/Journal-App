package com.example.dent.journalapp;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by dent4 on 6/30/2018.
 */
@RunWith(AndroidJUnit4.class)
public class AddJournalActivityTest {
    @Rule
    public ActivityTestRule<AddJournalActivity> mActivityTestRule = new ActivityTestRule<>(AddJournalActivity.class);
        private IdlingResource mIdlingResource;


        // Registers any resource that needs to be synchronized with Espresso before the test is run.
        @Before
        public void registerIdlingResource() {
            mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
            // To prove that the test fails, omit this call:
            Espresso.registerIdlingResources(mIdlingResource);
        }

        @Test
        public void idlingResourceTest() {
            onView(withId(R.id.activity_update)).check(matches((isDisplayed())));

            onView(withId(R.id.et_journal_title)).perform(typeText("Sample Title"), closeSoftKeyboard());

            onView(withId(R.id.et_journal_details)).perform(typeText("Sample Details"), closeSoftKeyboard());

            onView(withId(R.id.tv_update_journal)).check(matches(withText(R.string.add_journal)));

            onView(withId(R.id.add_button)).perform(click());

            onView((withId(R.id.rv_journal_list)))
                            .check(matches(hasDescendant(withText("Sample Title"))));

            onView((withId(R.id.rv_journal_list)))
                    .check(matches(hasDescendant(withText("Sample Details"))));
        }

        // Remember to unregister resources when not needed to avoid malfunction.
        @After
        public void unregisterIdlingResource() {
            if (mIdlingResource != null) {
                Espresso.unregisterIdlingResources(mIdlingResource);
            }
        }
}
