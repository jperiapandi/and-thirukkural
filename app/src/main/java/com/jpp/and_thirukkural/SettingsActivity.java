package com.jpp.and_thirukkural;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ListView;

public class SettingsActivity extends ThirukkuralBaseActivity {
    public static final String KEY_COMM_1 = "commentary1";
    public static final String KEY_COMM_2 = "commentary2";
    public static final String KEY_COMM_3 = "commentary3";
    public static final String KEY_COMM_4 = "commentary4";
    public static final String KEY_COMM_5 = "commentary5";
    public static final String KEY_MORNING_COUPLET = "morningCouplet";
    public static final String KEY_EVENING_COUPLET = "eveningCouplet";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        applyFontForToolbarTitle(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getFragmentManager().
                beginTransaction().
                replace(R.id.frameLayout, new ThirukkuralPreferenceFragment()).
//                replace(android.R.id.content, new ThirukkuralPreferenceFragment()).
                commit();
    }
/*
    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        int horizontalMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics());
        int verticalMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics());
        int topMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (int) getResources().getDimension(R.dimen.activity_vertical_margin) + 30, getResources().getDisplayMetrics());

        View view = findViewById(android.R.id.content);
        view.setPadding(horizontalMargin, topMargin, horizontalMargin, verticalMargin);

        return super.onCreateView(name, context, attrs);

    }
*/
    public static class ThirukkuralPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.app_preference);
        }
    }
}
