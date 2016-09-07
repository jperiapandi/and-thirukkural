package com.jpp.and_thirukkural;

/**
 * Created by jperiapandi on 05-09-2016.
 */

import android.app.Application;

import com.jpp.and_thirukkural.utils.LocaleHelper;

/**
 * When your application is launched this class is loaded before all of your activities.
 * And the instance of this class will live through whole application lifecycle.
 * <p/>
 */
public class ThirukkuralApplication extends Application {

    @Override
    public void onCreate() {
        LocaleHelper.setLocale(this, "ta");
        super.onCreate();
        LocaleHelper.onCreate(this);
    }
}