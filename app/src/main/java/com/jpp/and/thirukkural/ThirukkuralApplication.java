package com.jpp.and.thirukkural;

import android.app.Application;
import android.content.Context;

import com.jpp.and.thirukkural.content.ContentHlpr;
import com.jpp.and.thirukkural.db.DataLoadHelper;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

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
        //
        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/NotoSansTamil-Regular.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
}