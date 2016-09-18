package com.jpp.and_thirukkural;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.jpp.and_thirukkural.utils.FontCache;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ThirukkuralBaseActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void configureSearchMenu(Menu menu, int menuResource){
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(menuResource, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_menu_item).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(this, SearchResultsActivity.class)));

        searchView.setOnQueryTextListener(this);
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
}
