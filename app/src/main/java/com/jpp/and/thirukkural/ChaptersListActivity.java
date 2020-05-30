package com.jpp.and.thirukkural;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jpp.and.thirukkural.adapters.AllChaptersListItemAdapter;
import com.jpp.and.thirukkural.adapters.ListItemAdapter;
import com.jpp.and.thirukkural.db.DataLoadHelper;
import com.jpp.and.thirukkural.model.Chapter;
import com.jpp.and.thirukkural.model.ListItem;
import com.jpp.and.thirukkural.model.ListItemType;

import java.util.ArrayList;

public class ChaptersListActivity extends ThirukkuralBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapters_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        applyFontForToolbarTitle(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ListView mList = (ListView) findViewById(android.R.id.list);
        //
        DataLoadHelper dlh = DataLoadHelper.getInstance();
        ArrayList<Chapter> chapters = dlh.getAllChapters();
        Chapter[] values = chapters.toArray(new Chapter[chapters.size()]);

        ListItemAdapter adapter = new AllChaptersListItemAdapter(getBaseContext(), values);
        mList.setAdapter(adapter);

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListItem clickedItem = (ListItem) parent.getItemAtPosition(position);
                if(clickedItem!=null && clickedItem.getListItemType() == ListItemType.CHAPTER)
                {
                    //Open the clicked chapter in Main Activity
                    Chapter chapter = (Chapter) clickedItem;
                    Intent intent = new Intent((Activity) view.getContext(), ChapterActivity.class);
                    Bundle extras = new Bundle();
                    extras.putInt(Chapter.CHAPTER_ID, chapter.get_id());
                    intent.putExtras(extras);
                    intent.setAction(Intent.ACTION_MAIN);
                    startActivity(intent);
                }
            }
        });
    }
}
