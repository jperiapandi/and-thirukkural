package com.jpp.and_thirukkural;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.jpp.and_thirukkural.adapters.SearchResultListItemAdapter;
import com.jpp.and_thirukkural.db.DataLoadHelper;
import com.jpp.and_thirukkural.model.Chapter;
import com.jpp.and_thirukkural.model.Couplet;
import com.jpp.and_thirukkural.model.ListItem;
import com.jpp.and_thirukkural.model.Part;
import com.jpp.and_thirukkural.model.SearchResult;
import com.jpp.and_thirukkural.model.Section;
import com.jpp.and_thirukkural.model.SubHeader;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    private static String SEARCH_QUERY = "search_query";
    private String query = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DataLoadHelper dlh = DataLoadHelper.getInstance();

        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Listen for search action
        EditText ipQueryText = (EditText) findViewById(R.id.ip_query_text);
        ipQueryText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;

                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    handled = true;
                    search(v.getText().toString());
                }
                return handled;
            }
        });

        if(savedInstanceState != null){
            this.query = savedInstanceState.getString(SEARCH_QUERY);
            if(query != null && query.length() > 0){
                search(query);
            }
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(SEARCH_QUERY, this.query);
        super.onSaveInstanceState(outState);
    }


    private void search(String q) {

        this.query = q.trim();
        //use the query to search your data-base
        DataLoadHelper dlh = DataLoadHelper.getInstance();
        SearchResult searchResult = dlh.search(query);

        Log.i("Search Result", "Results received");

        View noSearchesYet = findViewById(R.id.no_searches_yet);
        View searchSuccessView = findViewById(R.id.content_search_success);
        View searchFailView = findViewById(R.id.content_search_fail);
        View searchRecents = findViewById(R.id.content_search_recents);
        try{
            noSearchesYet.setVisibility(View.GONE);
            searchFailView.setVisibility(View.GONE);
            searchSuccessView.setVisibility(View.GONE);
            searchRecents.setVisibility(View.GONE);
        }catch (Exception e){
            //ignore error
        }


            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View childView;

            if(searchResult.isSearchResultFound())
            {
                //Display results
                childView = searchSuccessView;
                if(childView == null)
                {
                    childView = inflater.inflate(R.layout.content_search_success, (ViewGroup) findViewById(R.id.content_search));
                }
                childView.setVisibility(View.VISIBLE);
                ArrayList<ListItem> resultItems = new ArrayList<ListItem>();

//                TextView qText = (TextView) childView.findViewById(R.id.qText);
//                TextView numberOfCouplets = (TextView) childView.findViewById(R.id.numberOfCouplets);
//                TextView numberOfChapters = (TextView) childView.findViewById(R.id.numberOfChapters);
//                TextView numberOfParts = (TextView) childView.findViewById(R.id.numberOfParts);
//                TextView numberOfSections = (TextView) childView.findViewById(R.id.numberOfSections);

                ListView searchResultsListView = (ListView) childView.findViewById(R.id.listView);

                ArrayList<Couplet> couplets = searchResult.getCouplets();
                ArrayList<Chapter> chapters = searchResult.getChapters();
                ArrayList<Part> parts = searchResult.getParts();
                ArrayList<Section> sections = searchResult.getSections();

                String strNumberOfCouplets = getResources().getString(R.string.none);
                String strNumberOfChapters = getResources().getString(R.string.none);
                String strNumberOfParts = getResources().getString(R.string.none);
                String strNumberOfSections = getResources().getString(R.string.none);


                if(sections != null){
                    strNumberOfSections = sections.size()+"";
                    //
                    SubHeader header = new SubHeader();
                    header.setTitle(getResources().getString(R.string.sections));
                    resultItems.add(header);
                    resultItems.addAll(sections);
                }


                if(parts != null){
                    strNumberOfParts = parts.size()+"";
                    //
                    SubHeader header = new SubHeader();
                    header.setTitle(getResources().getString(R.string.parts));
                    resultItems.add(header);
                    resultItems.addAll(parts);
                }


                if(chapters != null){
                    strNumberOfChapters = chapters.size()+"";
                    //
                    SubHeader header = new SubHeader();
                    header.setTitle(getResources().getString(R.string.chapters));
                    resultItems.add(header);
                    resultItems.addAll(chapters);
                }

                if(couplets != null){
                    strNumberOfCouplets = couplets.size()+"";
                    //
                    SubHeader header = new SubHeader();
                    header.setTitle(getResources().getString(R.string.couplets));
                    resultItems.add(header);
                    resultItems.addAll(couplets);
                }

//                qText.setText(searchResult.getQ());
//                numberOfCouplets.setText(strNumberOfCouplets);
//                numberOfChapters.setText(strNumberOfChapters);
//                numberOfParts.setText(strNumberOfParts);
//                numberOfSections.setText(strNumberOfSections);

                //
                ListItem[] items = resultItems.toArray(new ListItem[resultItems.size()]);
                SearchResultListItemAdapter adapter = new SearchResultListItemAdapter(getBaseContext(), items);
                searchResultsListView.setAdapter(adapter);

                searchResultsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ListItem item = (ListItem) parent.getItemAtPosition(position);
                        Intent intent;
                        Bundle extras;

                        switch (item.getListItemType()){
                            case COUPLET:
                                Couplet couplet = (Couplet) item;

                                intent = new Intent((Activity) view.getContext(), CoupletSwipeActivity.class);
                                extras = new Bundle();
                                extras.putInt(Couplet.COUPLET_ID, couplet.get_id());
                                intent.putExtras(extras);
                                startActivity(intent);

                                break;
                            case CHAPTER:
                                Chapter chapter = (Chapter) item;
                                intent = new Intent((Activity) view.getContext(), ChapterActivity.class);
                                extras = new Bundle();
                                extras.putInt(Chapter.CHAPTER_ID, chapter.get_id());
                                intent.putExtras(extras);
//                                intent.setAction(Intent.ACTION_MAIN);
                                startActivity(intent);
                                break;
                        }
                    }
                });
            }
            else
            {
                //Display failure message if there were no results found
                childView = searchFailView;
                if(childView == null)
                {
                    childView = inflater.inflate(R.layout.content_search_fail, (ViewGroup) findViewById(R.id.content_search));
                }
                childView.setVisibility(View.VISIBLE);
                TextView infoView = (TextView) childView.findViewById(R.id.searchFailureInfo);
                String warningInfo = getResources().getString(R.string.searchFailureInfo);
                warningInfo = String.format(warningInfo, searchResult.getQ());
                infoView.setText(Html.fromHtml(warningInfo));
            }
    }
}
