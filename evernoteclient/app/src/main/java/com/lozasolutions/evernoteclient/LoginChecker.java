package com.lozasolutions.evernoteclient;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

import com.evernote.client.android.EvernoteOAuthActivity;
import com.evernote.client.android.login.EvernoteLoginActivity;
import com.lozasolutions.evernoteclient.data.remote.EvernoteAPI;
import com.lozasolutions.evernoteclient.features.login.LoginActivity;
import com.lozasolutions.evernoteclient.features.main.MainActivity;
import com.lozasolutions.evernoteclient.features.splashscreen.SplashActivity;

import java.util.Arrays;
import java.util.List;

/**
 * @author rwondratschek
 */
public class LoginChecker implements Application.ActivityLifecycleCallbacks {


    private EvernoteAPI evernoteAPI;

    public LoginChecker(EvernoteAPI evernoteAPI) {
        this.evernoteAPI = evernoteAPI;
    }

    private static final List<Class<? extends Activity>> IGNORED_ACTIVITIES = Arrays.asList(
            SplashActivity.class,
            LoginActivity.class,
            EvernoteLoginActivity.class,
            EvernoteOAuthActivity.class
    );

    private Intent mCachedIntent;

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if (!evernoteAPI.isLoggedIn() && !isIgnored(activity)) {
            mCachedIntent = activity.getIntent();
            LoginActivity.launch(activity);

            activity.finish();
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {
        if (activity instanceof LoginActivity && evernoteAPI.isLoggedIn()) {
            if (mCachedIntent != null) {
                activity.startActivity(mCachedIntent);
                mCachedIntent = null;
            } else {
                activity.startActivity(new Intent(activity, MainActivity.class));
            }
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    private boolean isIgnored(Activity activity) {
        return IGNORED_ACTIVITIES.contains(activity.getClass());
    }
}
