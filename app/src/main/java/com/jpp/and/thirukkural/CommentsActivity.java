package com.jpp.and.thirukkural;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

public class CommentsActivity extends ThirukkuralBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }
}
