package com.jpp.and.thirukkural;

import android.app.Application;

import com.jpp.and.thirukkural.content.ContentHlpr;
import com.jpp.and.thirukkural.db.DataLoadHelper;

/**
 * When your application is launched this class is loaded before all of your activities.
 * And the instance of this class will live through whole application lifecycle.
 * <p/>
 */
public class ThirukkuralApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DataLoadHelper.setContext(getApplicationContext());
        ContentHlpr.init();
    }
}