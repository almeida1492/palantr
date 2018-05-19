package com.test.android.palantr;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.test.android.palantr.Activities.MainActivity;
import com.test.android.palantr.Adapters.PostsAdapter;

import org.hamcrest.Description;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class IdlingResourceMainActivityTest {

    public static BoundedMatcher<RecyclerView.ViewHolder,
                PostsAdapter.ViewHolder> withHolderRecipeSteps(final String text) {

        return new BoundedMatcher<RecyclerView.ViewHolder,
                PostsAdapter.ViewHolder>(PostsAdapter.ViewHolder.class) {
            @Override
            protected boolean matchesSafely(PostsAdapter.ViewHolder item) {
                TextView nameTextView = item.bodyView;
                return nameTextView.getText().toString().equals(text);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("No ViewHolder found with text equal to: " + text);
            }
        };
    }

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @Test
    public void idlingResourceTest() {
        onView(withId(R.id.post_list)).check(matches(isDisplayed()));
        onView(withId(R.id.post_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource!= null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }
}
