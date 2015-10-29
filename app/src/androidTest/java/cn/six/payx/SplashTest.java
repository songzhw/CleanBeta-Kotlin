package cn.six.payx;

import android.app.Activity;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingPolicies;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import cn.six.payx.core.AsyncIdlingRes;
import cn.six.payx.core.IIdlingFlag;
import cn.six.payx.ui.SplashActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * @author songzhw
 * @date 2015/10/22
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class SplashTest  implements IIdlingFlag {
    @Rule
    public ActivityTestRule<SplashActivity> actvRule = new ActivityTestRule<SplashActivity>(SplashActivity.class);

    @Override
    public boolean isFinish() {
        Log.d("szw", "SplashTest : isFinish = " + actvRule.getActivity().getIsFinishedSplash());
        return actvRule.getActivity().getIsFinishedSplash();
    }
    @Test
    public void checkCopyRight(){
        Activity actv = actvRule.getActivity();
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        String orginStr = actv.getString(R.string.splash_copyright);

        onView(withId(R.id.tv_splash_copyright))
                .check(matches(withText(String.format(orginStr, year))));

    }

    @Test
    public void splash2Home(){
        onView(withId(R.id.tv_splash_copyright))
                .check(matches(isDisplayed()));

        // Make sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(40, TimeUnit.SECONDS);
        IdlingPolicies.setIdlingResourceTimeout(80, TimeUnit.SECONDS);

        AsyncIdlingRes idlingResource = new AsyncIdlingRes(this);
        Espresso.registerIdlingResources(idlingResource);

        onView(withId(R.id.vp_home))
                .check(matches(isDisplayed()));

        idlingResource.removeListener(); // for memory leak
        Espresso.unregisterIdlingResources(idlingResource);


    }


}
