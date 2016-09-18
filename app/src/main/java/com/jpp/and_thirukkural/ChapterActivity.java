package com.jpp.and_thirukkural;

import android.app.Activity;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.jpp.and_thirukkural.adapters.ListItemAdapter;
import com.jpp.and_thirukkural.db.DataLoadHelper;
import com.jpp.and_thirukkural.model.Chapter;
import com.jpp.and_thirukkural.model.Couplet;
import com.jpp.and_thirukkural.model.Part;
import com.jpp.and_thirukkural.model.Section;

import java.util.ArrayList;

public class ChapterActivity extends ThirukkuralBaseActivity implements SearchView.OnQueryTextListener {

    private static ArrayList<Chapter> allChapters;
    static DataLoadHelper dlh;

    private ChapterPagerAdapter mChapterPagerAdapter;
    private ViewPager mChapterPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chapter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        applyFontForToolbarTitle(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

       //Load data

        dlh = new DataLoadHelper(getApplicationContext());
        allChapters = dlh.getAllChapters();


        // Create the adapter that will return a fragment for each of the chapters
        mChapterPagerAdapter = new ChapterPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mChapterPager = (ViewPager) findViewById(R.id.chapterPager);
        mChapterPager.setAdapter(mChapterPagerAdapter);
        mChapterPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                /*
                Chapter c = allChapters.get(position);
                String chapterName = c.get_id()+" "+c.getTitle();
                getSupportActionBar().setTitle(chapterName);
                */
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.chapterTabs);
        tabLayout.setupWithViewPager(mChapterPager);

        //Set selected tab based on extras received in the intent
        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            int chapterID = extras.getInt(Chapter.CHAPTER_ID, 1);
            TabLayout.Tab targetTab = tabLayout.getTabAt(chapterID-1);
            targetTab.select();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        configureSearchMenu(menu, R.menu.menu_chapter);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }

    /*Create Framgment for Chapter Pager*/
    public static class ChapterPageFragment extends Fragment{
        public static final String CHAPTER_NUMBER="chapterNumber";

        public ChapterPageFragment(){

        }

        public static ChapterPageFragment newInstance(int position){
            ChapterPageFragment fragment = new ChapterPageFragment();
            Bundle args = new Bundle();
            args.putInt(CHAPTER_NUMBER, position);
            fragment.setArguments(args);
            return fragment;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View chapterPageFragmentView = inflater.inflate(R.layout.fragment_chapterpage, container, false);
            int position = getArguments().getInt(CHAPTER_NUMBER);
            Chapter chapter = allChapters.get(position);
            Section section = dlh.getSectionById(chapter.getSectionId());
            Part part = dlh.getPartById(chapter.getPartId());

            TextView chapterId = (TextView) chapterPageFragmentView.findViewById(R.id.chapter_id);
            TextView chapterName = (TextView) chapterPageFragmentView.findViewById(R.id.chapter_name);
            TextView sectionName = (TextView) chapterPageFragmentView.findViewById(R.id.section_name);
            TextView partName = (TextView) chapterPageFragmentView.findViewById(R.id.part_name);
            TextView chapterSerial = (TextView) chapterPageFragmentView.findViewById(R.id.chapter_serial);

            chapterId.setText(chapter.get_id()+".");
            chapterName.setText(chapter.getTitle());

            sectionName.setText(section.get_id()+". "+section.getTitle());
            partName.setText(part.get_id()+". "+part.getTitle());
            chapterSerial.setText(getResources().getString(R.string.serial)+": "+chapter.getSerial());


            //Load couplets in a chapter and display in a list
            ArrayList<Couplet> couplets = dlh.getCoupletsByChapter(chapter.get_id(), false);
            ListView coupletsListView = (ListView) chapterPageFragmentView.findViewById(R.id.chapterCoupletsListView);
            Couplet[] values = couplets.toArray(new Couplet[couplets.size()]);
            ListItemAdapter adapter = new ListItemAdapter(getContext(), values);
            coupletsListView.setAdapter(adapter);

            coupletsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Couplet couplet = (Couplet) parent.getItemAtPosition(position);
                    Intent intent = new Intent((Activity) view.getContext(), CoupletSwipeActivity.class);
                    Bundle extras = new Bundle();
                    extras.putInt(Couplet.COUPLET_ID, couplet.get_id());
                    intent.putExtras(extras);
                    startActivity(intent);
                }
            });
            return chapterPageFragmentView;
        }
    }

    /*Create Fragment Adapter for ChapterPager*/
    public class ChapterPagerAdapter extends FragmentPagerAdapter {

        public ChapterPagerAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return ChapterPageFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return allChapters.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return allChapters.get(position).get_id()+"";
        }

    }
}
