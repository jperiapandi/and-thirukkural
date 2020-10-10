package com.jpp.and.thirukkural;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class ProfileActivity extends ThirukkuralBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        //
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        ImageView photoImageView = findViewById(R.id.photo_imageview);
        TextView emailTxtView = findViewById(R.id.email_txtview);
        TextView nameTxtView = findViewById(R.id.name_txtview);

        assert user != null;
        emailTxtView.setText(user.getEmail());
        nameTxtView.setText(user.getDisplayName());
        Glide.with(this).load(user.getPhotoUrl()).circleCrop().into(photoImageView);
    }
}
