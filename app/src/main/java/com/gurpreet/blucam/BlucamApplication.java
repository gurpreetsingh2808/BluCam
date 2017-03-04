package com.gurpreet.blucam;

import android.app.Application;

/**
 * This is the main starting point of the application.
 *
 * @author Gurpreet Singh
 * @since June-3-2016
 */

public class BlucamApplication extends Application {

    private static BlucamApplication mApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
    }

    public static BlucamApplication getInstance() {
        return mApplication;
    }

}
