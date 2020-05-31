package com.jpp.and.thirukkural;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

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

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        getFragmentManager().
                beginTransaction().
                replace(R.id.frameLayout, new ThirukkuralPreferenceFragment()).
                commit();
    }

    public static class ThirukkuralPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.app_preference);
        }
    }
}
