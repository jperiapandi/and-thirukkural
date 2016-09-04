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

            Couplet couplet = dlh.getCoupletById(coupletID, true);
            Chapter chapter = dlh.getChapterById(couplet.getChapterId());
            Section section = dlh.getSectionById(chapter.getSectionId());
            Part part = dlh.getPartById(chapter.getPartId());

            //Bind data to view
            TextView coupletIdView = (TextView) findViewById(R.id.couplet_id);
            TextView coupletTextView = (TextView) findViewById(R.id.couplet_text);
            TextView coupletTextViewEn = (TextView) findViewById(R.id.couplet_text_en);
            TextView chapterTitleView = (TextView) findViewById(R.id.chapter_title);
            TextView sectionTitleView = (TextView) findViewById(R.id.section_title);
            TextView partTitleView = (TextView) findViewById(R.id.part_title);

            TextView commentary1View = (TextView) findViewById(R.id.commentary1);
            TextView commentary2View = (TextView) findViewById(R.id.commentary2);
            TextView commentary3View = (TextView) findViewById(R.id.commentary3);
            TextView commentary4View = (TextView) findViewById(R.id.commentary4);
            TextView commentary5View = (TextView) findViewById(R.id.commentary5);

            coupletIdView.setText(getResources().getString(R.string.couplet)+"  "+couplet.get_id()+"");
            coupletTextView.setText(couplet.getCouplet());
            coupletTextViewEn.setText(couplet.getCouplet_en());

            chapterTitleView.setText(chapter.get_id()+". "+chapter.getTitle());
            sectionTitleView.setText(section.get_id()+". "+section.getTitle());
            partTitleView.setText(part.get_id()+". "+part.getTitle());

            commentary1View.setText(couplet.getExpln_muva());
            commentary2View.setText(couplet.getExpln_pappaiah());
            commentary3View.setText(couplet.getExpln_manakudavar());
            commentary4View.setText(couplet.getExpln_karunanidhi());
            commentary5View.setText(couplet.getExpln_en());
        }
    }

}
