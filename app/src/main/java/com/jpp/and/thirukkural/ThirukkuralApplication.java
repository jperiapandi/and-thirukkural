package com.jpp.and.thirukkural;

/**
 * Created by jperiapandi on 05-09-2016.
 */

import android.app.Application;

import com.jpp.and.thirukkural.content.ContentHlpr;
import com.jpp.and.thirukkural.db.DataLoadHelper;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

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

        CalligraphyConfig.initDefault(
                new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/NotoSansTamil-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}