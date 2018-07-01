package com.example.dent.journalapp;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class LoadJournalViewTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void TestMainActivity() {

//        onView((withId(R.id.sign_in_button))).perform(click());
//
//        onView(withId(R.id.activity_journal_list)).check(matches(isDisplayed()));

    }

}
