package com.test.android.palantr;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.test.android.palantr.Activities.NewPostActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class SendPostTest {

    private String testText = "Test";

    @Rule
    public ActivityTestRule<NewPostActivity> mActivityTestRule =
            new ActivityTestRule<>(NewPostActivity.class);

    @Test
    public void sendPostTest() {
        onView(withId(R.id.post_body)).perform(typeText(testText));
        onView(withId(R.id.send_post)).perform(click());
    }
}
