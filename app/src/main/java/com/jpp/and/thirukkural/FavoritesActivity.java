package com.jpp.and.thirukkural;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import com.jpp.and.thirukkural.adapters.ListItemAdapter;
import com.jpp.and.thirukkural.db.CoupletsTable;
import com.jpp.and.thirukkural.db.DataLoadHelper;
import com.jpp.and.thirukkural.model.Couplet;
import com.jpp.and.thirukkural.model.ListItem;

import java.util.ArrayList;
import java.util.Objects;

public class FavoritesActivity extends ThirukkuralBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        drawFavorites();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.favorites_menu, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        drawFavorites();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.remove_favorites){
            //Mark favorites couplets as not favorite
            String selection = CoupletsTable.COL_FAV + "=?";
            String[] selectionArgs = new String[]{"1"};
            final DataLoadHelper dlh = DataLoadHelper.getInstance();
            final ArrayList<Couplet> favoriteCouplets = dlh.getAllCouplets(false, selection, selectionArgs);



            if(favoriteCouplets !=null && favoriteCouplets.size()>0){

                //Build confirm dialog
                final AlertDialog.Builder confirmDialog = new AlertDialog.Builder(FavoritesActivity.this);
                confirmDialog.setTitle(getResources().getString(R.string.remove_all_alert_title));
                confirmDialog.setMessage(getResources().getString(R.string.remove_all_alert_msg));
                confirmDialog.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        for (Couplet couplet : favoriteCouplets) {
                            dlh.unmarkFavoriteCouplet(couplet.get_id());
                        }
                        drawFavorites();
                    }
                });

                confirmDialog.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                confirmDialog.show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void drawFavorites(){
        //Load favorite couplets from db

        String selection = CoupletsTable.COL_FAV + "=?";
        String[] selectionArgs = new String[]{"1"};
        ArrayList<Couplet> favoriteCouplets = DataLoadHelper.getInstance().getAllCouplets(false, selection, selectionArgs);
        ListView favCoupletsListView;
        TextView noFavTextView;

        if(favoriteCouplets != null && favoriteCouplets.size() > 0){

            noFavTextView = findViewById(R.id.noFavTextView);
            noFavTextView.setVisibility(View.GONE);

            favCoupletsListView = findViewById(R.id.favCoupletsListView);
            ListItem[] items = favoriteCouplets.toArray(new ListItem[0]);
            ListItemAdapter adapter = new ListItemAdapter(getBaseContext(), items);
            favCoupletsListView.setAdapter(adapter);
            favCoupletsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Couplet couplet = (Couplet) parent.getItemAtPosition(position);
                    final Intent intent = new Intent(view.getContext(), CoupletSwipeActivity.class);
                    Bundle extras = new Bundle();
                    extras.putInt(Couplet.COUPLET_ID, couplet.get_id());
                    intent.putExtras(extras);

                    startActivity(intent);
                }
            });
        }
        else
        {
            favCoupletsListView = findViewById(R.id.favCoupletsListView);
            favCoupletsListView.setAdapter(null);
            favCoupletsListView.setVisibility(View.GONE);

            noFavTextView = findViewById(R.id.noFavTextView);
            noFavTextView.setVisibility(View.VISIBLE);
        }
    }
}
