package com.jpp.and.thirukkural;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

public class AllCoupletsActivity extends ThirukkuralBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_couplets);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        applyFontForToolbarTitle(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
