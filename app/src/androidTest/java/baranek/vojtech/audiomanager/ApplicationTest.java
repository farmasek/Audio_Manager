package baranek.vojtech.audiomanager;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import baranek.vojtech.audiomanager.profileActivity.ProfileActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ApplicationTest{
    @Rule
    public ActivityTestRule<ProfileActivity> mActivityActivityTestRule = new ActivityTestRule<>(ProfileActivity.class);


    @Test
       public void showTextTest(){

        onView(withId(R.id.et_TimerName)).perform(clearText());
        onView(withId(R.id.et_TimerName)).perform(typeText("Test"));
        onView(withId(R.id.et_TimerName)).check(matches(withText("Test")));

    }

}