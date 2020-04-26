package com.jpp.and.thirukkural;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends ThirukkuralBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        applyFontForToolbarTitle(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        ImageView photoImageView = (ImageView) findViewById(R.id.photo_imageview);
        TextView emailTxtView = (TextView) findViewById(R.id.email_txtview);
        TextView nameTxtView = (TextView) findViewById(R.id.name_txtview);

        emailTxtView.setText(user.getEmail());
        nameTxtView.setText(user.getDisplayName());
        Glide.with(this).load(user.getPhotoUrl()).circleCrop().into(photoImageView);
    }
}
