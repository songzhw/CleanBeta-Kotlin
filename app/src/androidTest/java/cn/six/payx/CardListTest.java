package cn.six.payx;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingPolicies;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import cn.six.payx.core.AsyncIdlingRes;
import cn.six.payx.core.IIdlingFlag;
import cn.six.payx.ui.CardActivity;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.internal.util.Checks.checkNotNull;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class CardListTest implements IIdlingFlag {

    @Rule
    public ActivityTestRule<CardActivity> actvRule = new ActivityTestRule<CardActivity>(CardActivity.class);

    @Override
    public boolean isFinish() {
        CardActivity actv = actvRule.getActivity();
        return actv.getPresenter().isFinishedLoading();
    }

    @Test
    public void deleteAndRevert(){
        // Make sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(40, TimeUnit.SECONDS);
        IdlingPolicies.setIdlingResourceTimeout(80, TimeUnit.SECONDS);

        AsyncIdlingRes idlingResource = new AsyncIdlingRes(this);
        Espresso.registerIdlingResources(idlingResource);

        // ===============================
        // delete
        onData(withItemContent("Royal Bank"))
                .perform(click());
        onView(withId(R.id.tvCardsTop))
                .check(matches(isDisplayed()));
/*
        //error. Already delete it, this will throw a exception for not founding this data.
        onData(withItemContent("Royal Bank"))
                .check(matches(not(isDisplayed())));
*/

        // revert
        onView(withId(R.id.tvCardsTop))
                .perform(click());
        onView(withId(R.id.tvCardsTop))
                .check(matches(not(isDisplayed())));
        onData(withItemContent("Royal Bank"))
                .check(matches(isDisplayed()));

        // ===============================

        idlingResource.removeListener(); // for memory leak
        Espresso.unregisterIdlingResources(idlingResource);
    }








    //matcher
    public Matcher<Object> withItemContent(String expectedText) {
        return withItemContent(equalTo(expectedText));
    }

    public Matcher<Object> withItemContent(final Matcher<String> itemTextMatcher) {
        checkNotNull(itemTextMatcher);
        return new BoundedMatcher<Object, String>(String.class) {
            @Override
            public boolean matchesSafely(String item) {
                return itemTextMatcher.matches(item);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with bankName: " + description);
            }
        };
    }

}
