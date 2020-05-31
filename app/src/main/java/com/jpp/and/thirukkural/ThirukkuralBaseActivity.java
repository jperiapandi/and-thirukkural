package com.jpp.and.thirukkural;

import android.content.Context;
import android.content.Intent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

public class ThirukkuralBaseActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, View.OnClickListener{

    protected FloatingActionButton fab_main, fab_pdf, fab_search, fab_share, fab_favorite, fab_search_in_section;
    protected Animation animationFabOpen, animationFabClose, animationRotateForward, animationRotateBackward;
    public boolean isFabOpen = false;

    @Override
    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
        super.attachBaseContext(newBase);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();

        isFabOpen = false;
        fab_main = (FloatingActionButton) findViewById(R.id.fab_main);
        fab_pdf = (FloatingActionButton) findViewById(R.id.fab_pdf);
        fab_share = (FloatingActionButton) findViewById(R.id.fab_share);
        fab_search = (FloatingActionButton) findViewById(R.id.fab_search);
        fab_search_in_section = (FloatingActionButton) findViewById(R.id.fab_search_in_section);
        fab_favorite = (FloatingActionButton) findViewById(R.id.fab_favorite);


        animationFabOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        animationFabClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        animationRotateForward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_forward);
        animationRotateBackward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_backward);

        if(fab_main != null)
        {
            fab_main.setOnClickListener(this);
        }

        if(fab_search != null){
            fab_search.setOnClickListener(this);
        }
        if(fab_search_in_section != null){
            fab_search_in_section.setOnClickListener(this);
        }

        if(fab_share != null){
            fab_share.setOnClickListener(this);
        }

        if(fab_pdf!=null){
            fab_pdf.setOnClickListener(this);
        }

        if(fab_favorite!=null){
            fab_favorite.setOnClickListener(this);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.i("Search submit" , query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.fab_main:
                toggleFABState();
                break;
            case R.id.fab_pdf:
                quickCloseFAB();
                Toast.makeText(v.getContext(), "PDF option is coming soon", Toast.LENGTH_SHORT).show();
                break;
            case R.id.fab_share:
                quickCloseFAB();
//                Toast.makeText(v.getContext(), "Share option is coming soon", Toast.LENGTH_SHORT).show();
                break;
            case R.id.fab_favorite:
                quickCloseFAB();
                break;
            case R.id.fab_search:
                quickCloseFAB();
                Intent intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.fab_search_in_section:
                Intent intent2 = new Intent(this, SearchActivity.class);
                startActivity(intent2);
                break;
        }
    }

    private void toggleFABState(){

        if(isFabOpen){
            closeFAB(null);
        } else {
            openFAB(null);
        }
    }

    public void quickCloseFAB(){
        isFabOpen = false;
        animationRotateBackward.setDuration(0);
        animationFabClose.setDuration(0);

        fab_main.startAnimation(animationRotateBackward);
        fab_pdf.startAnimation(animationFabClose);
        fab_pdf.setClickable(isFabOpen);
        fab_share.startAnimation(animationFabClose);
        fab_share.setClickable(isFabOpen);
        fab_search.startAnimation(animationFabClose);
        fab_search.setClickable(isFabOpen);
        if(fab_favorite != null){
            fab_favorite.startAnimation(animationFabClose);
            fab_favorite.setClickable(isFabOpen);
        }
    }

    public void closeFAB(Animation.AnimationListener animationListener){
        isFabOpen = false;
        fab_main.startAnimation(animationRotateBackward);
        fab_pdf.startAnimation(animationFabClose);
        fab_pdf.setClickable(isFabOpen);
        fab_share.startAnimation(animationFabClose);
        fab_share.setClickable(isFabOpen);
        fab_search.startAnimation(animationFabClose);
        fab_search.setClickable(isFabOpen);
        if(fab_favorite != null){
            fab_favorite.startAnimation(animationFabClose);
            fab_favorite.setClickable(isFabOpen);
        }

        if(animationListener!=null){
            animationRotateBackward.setAnimationListener(animationListener);
        }
    }



    public void openFAB(Animation.AnimationListener animationListener){
        isFabOpen = true;
        fab_main.startAnimation(animationRotateForward);
        fab_pdf.startAnimation(animationFabOpen);
        fab_pdf.setClickable(isFabOpen);
        fab_share.startAnimation(animationFabOpen);
        fab_share.setClickable(isFabOpen);
        fab_search.startAnimation(animationFabOpen);
        fab_search.setClickable(isFabOpen);
        if(fab_favorite != null){
            fab_favorite.startAnimation(animationFabOpen);
            fab_favorite.setClickable(isFabOpen);
        }

        if(animationListener!=null){
            animationRotateForward.setAnimationListener(animationListener);
        }
    }
}
