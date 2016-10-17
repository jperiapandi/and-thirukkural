package com.jpp.and_thirukkural;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.jpp.and_thirukkural.adapters.ListItemAdapter;
import com.jpp.and_thirukkural.db.CoupletsTable;
import com.jpp.and_thirukkural.db.DataLoadHelper;
import com.jpp.and_thirukkural.model.Couplet;
import com.jpp.and_thirukkural.model.ListItem;

import java.util.ArrayList;

public class FavoritesActivity extends ThirukkuralBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        applyFontForToolbarTitle(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawFavorites();
    }

    @Override
    protected void onStart() {
        super.onStart();

        drawFavorites();
    }

    private void drawFavorites(){
        //Load favorite couplets from db

        String selection = CoupletsTable.COL_FAV + "=?";
        String[] selectionArgs = new String[]{"1"};
        ArrayList<Couplet> favoriteCouplets = DataLoadHelper.getInstance().getAllCouplets(false, selection, selectionArgs);
        ListView favCoupletsListView;
        TextView noFavTextView;

        if(favoriteCouplets != null && favoriteCouplets.size() > 0){

            noFavTextView = (TextView) findViewById(R.id.noFavTextView);
            noFavTextView.setVisibility(View.GONE);

            favCoupletsListView = (ListView) findViewById(R.id.favCoupletsListView);
            ListItem[] items = favoriteCouplets.toArray(new ListItem[favoriteCouplets.size()]);
            ListItemAdapter adapter = new ListItemAdapter(getBaseContext(), items);
            favCoupletsListView.setAdapter(adapter);
            favCoupletsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Couplet couplet = (Couplet) parent.getItemAtPosition(position);
                    final Intent intent = new Intent((Activity) view.getContext(), CoupletSwipeActivity.class);
                    Bundle extras = new Bundle();
                    extras.putInt(Couplet.COUPLET_ID, couplet.get_id());
                    intent.putExtras(extras);

                    startActivity(intent);
                }
            });
        }
        else
        {
            favCoupletsListView = (ListView) findViewById(R.id.favCoupletsListView);
            favCoupletsListView.setAdapter(null);
            favCoupletsListView.setVisibility(View.GONE);

            noFavTextView = (TextView) findViewById(R.id.noFavTextView);
            noFavTextView.setVisibility(View.VISIBLE);
        }
    }
}
