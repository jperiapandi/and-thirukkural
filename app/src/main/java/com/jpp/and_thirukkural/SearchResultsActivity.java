package com.jpp.and_thirukkural;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jpp.and_thirukkural.db.DataLoadHelper;
import com.jpp.and_thirukkural.model.Chapter;
import com.jpp.and_thirukkural.model.Couplet;
import com.jpp.and_thirukkural.model.Part;
import com.jpp.and_thirukkural.model.SearchResult;
import com.jpp.and_thirukkural.model.Section;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class SearchResultsActivity extends AppCompatActivity {

    private DataLoadHelper dlh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dlh = new DataLoadHelper(getBaseContext());

        setContentView(R.layout.activity_search_results);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data-base
            SearchResult searchResult = dlh.search(query);

            Log.i("Search Result", "Results received");

            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View childView;
            if(searchResult.isSearchResultFound())
            {
                //Display results
                childView = inflater.inflate(R.layout.content_search_success, (ViewGroup) findViewById(R.id.search_result_layout));

                TextView qText = (TextView) childView.findViewById(R.id.qText);
                TextView numberOfCouplets = (TextView) childView.findViewById(R.id.numberOfCouplets);
                TextView numberOfChapters = (TextView) childView.findViewById(R.id.numberOfChapters);
                TextView numberOfParts = (TextView) childView.findViewById(R.id.numberOfParts);
                TextView numberOfSections = (TextView) childView.findViewById(R.id.numberOfSections);

                ArrayList<Couplet> couplets = searchResult.getCouplets();
                ArrayList<Chapter> chapters = searchResult.getChapters();
                ArrayList<Part> parts = searchResult.getParts();
                ArrayList<Section> sections = searchResult.getSections();

                String strNumberOfCouplets = getResources().getString(R.string.none);
                String strNumberOfChapters = getResources().getString(R.string.none);
                String strNumberOfParts = getResources().getString(R.string.none);
                String strNumberOfSections = getResources().getString(R.string.none);

                if(couplets != null){
                    strNumberOfCouplets = couplets.size()+"";
                }
                if(chapters != null){
                    strNumberOfChapters = chapters.size()+"";
                }
                if(parts != null){
                    strNumberOfParts = parts.size()+"";
                }
                if(sections != null){
                    strNumberOfSections = sections.size()+"";
                }

                qText.setText(searchResult.getQ());
                numberOfCouplets.setText(strNumberOfCouplets);
                numberOfChapters.setText(strNumberOfChapters);
                numberOfParts.setText(strNumberOfParts);
                numberOfSections.setText(strNumberOfSections);
            }
            else
            {
                //Display failure message if there were no results found
                childView = inflater.inflate(R.layout.content_search_fail, (ViewGroup) findViewById(R.id.search_result_layout));

                TextView infoView = (TextView) childView.findViewById(R.id.searchFailureInfo);
                String warningInfo = getResources().getString(R.string.searchFailureInfo);
                warningInfo = String.format(warningInfo, searchResult.getQ());
                infoView.setText(Html.fromHtml(warningInfo));
            }

        }
    }

}
