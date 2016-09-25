package com.jpp.and_thirukkural;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.jpp.and_thirukkural.utils.FontCache;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ThirukkuralBaseActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, View.OnClickListener{

    protected FloatingActionButton fab_main, fab_pdf, fab_search, fab_share, fab_favorite;
    protected Animation animationFabOpen, animationFabClose, animationRotateForward, animationRotateBackward;
    public boolean isFabOpen = false;

    @Override
    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
        super.attachBaseContext(newBase);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

    public void configureSearchMenu(Menu menu, int menuResource){
        /*
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(menuResource, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_menu_item).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(this, SearchResultsActivity.class)));

        searchView.setOnQueryTextListener(this);
        */
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


    public void applyFontForToolbarTitle(Toolbar toolbar){
/*
        for(int i = 0; i < toolbar.getChildCount(); i++){
            View view = toolbar.getChildAt(i);
            if(view instanceof TextView){
                TextView tv = (TextView) view;

                tv.setTypeface(FontCache.getTypeface("fonts/NotoSansTamil-Regular.ttf", getApplicationContext()));
            }
        }*/
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.fab_main:
                toggleFABState();
                break;
            case R.id.fab_pdf:
                Toast.makeText(v.getContext(), "PDF option is coming soon", Toast.LENGTH_SHORT).show();
                break;
            case R.id.fab_share:
                Toast.makeText(v.getContext(), "Share option is coming soon", Toast.LENGTH_SHORT).show();
                break;
            case R.id.fab_favorite:
                Toast.makeText(v.getContext(), "Marking as favorite is coming soon", Toast.LENGTH_SHORT).show();
                break;
            case R.id.fab_search:
                Intent intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
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
