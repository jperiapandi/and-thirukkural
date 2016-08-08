package com.jpp.and_thirukkural;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.jpp.and_thirukkural.db.DataLoadHelper;
import com.jpp.and_thirukkural.model.Chapter;
import com.jpp.and_thirukkural.model.Couplet;
import com.jpp.and_thirukkural.model.Part;
import com.jpp.and_thirukkural.model.Section;

public class CoupletActivity extends AppCompatActivity {
    private static DataLoadHelper dlh ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_couplet);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        if(dlh == null)
        {
            dlh = new DataLoadHelper(getApplicationContext());
        }
        //Load required data from DB
        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            int coupletID = extras.getInt(Couplet.COUPLET_ID, 1);

            Couplet couplet = dlh.getCoupletById(coupletID);
            Chapter chapter = dlh.getChapterById(couplet.getChapterId());
            Section section = dlh.getSectionById(chapter.getSectionId());
            Part part = dlh.getPartById(chapter.getPartId());

            //Bind data to view
            TextView coupletIdView = (TextView) findViewById(R.id.couplet_id);
            TextView coupletTextView = (TextView) findViewById(R.id.couplet_text);
            TextView chapterTitleView = (TextView) findViewById(R.id.chapter_title);
            TextView sectionTitleView = (TextView) findViewById(R.id.section_title);
            TextView partTitleView = (TextView) findViewById(R.id.part_title);

            coupletIdView.setText(couplet.get_id()+"");
            coupletTextView.setText(couplet.getCouplet());
            chapterTitleView.setText(chapter.getTitle());
            sectionTitleView.setText(section.getTitle());
            partTitleView.setText(part.getTitle());
        }
    }

}
