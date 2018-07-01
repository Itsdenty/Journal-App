package com.example.dent.journalapp;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by dent4 on 6/30/2018.
 */
@RunWith(AndroidJUnit4.class)
public class SingleJournalActivityTest {

    @Rule
    public ActivityTestRule<SingleJournalActivity> mActivityTestRule =
            new ActivityTestRule<>(SingleJournalActivity.class);
    @Test
    public void TestJournalActivity() {

        onView(withId(R.id.activity_single_journal)).check(matches(isDisplayed()));

        onView(withId(R.id.update_button)).perform(click());

        onView(withId(R.id.activity_update)).check(matches((isDisplayed())));

        onView(withId(R.id.tv_update_journal)).check(matches(withText(R.string.add_journal)));
    }
}
