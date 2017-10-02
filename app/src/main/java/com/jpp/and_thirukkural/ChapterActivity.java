package com.jpp.and_thirukkural;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jpp.and_thirukkural.adapters.ListItemAdapter;
import com.jpp.and_thirukkural.content.ContentHlpr;
import com.jpp.and_thirukkural.db.DataLoadHelper;
import com.jpp.and_thirukkural.model.Chapter;
import com.jpp.and_thirukkural.model.Couplet;
import com.jpp.and_thirukkural.model.Part;
import com.jpp.and_thirukkural.model.Section;

import java.util.ArrayList;

import static com.jpp.and_thirukkural.R.string.chapter;

public class ChapterActivity extends ThirukkuralBaseActivity implements SearchView.OnQueryTextListener {

    private static ArrayList<Chapter> allChapters;

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

        DataLoadHelper dlh = DataLoadHelper.getInstance();
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
                setChapterTitle(position);
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

        setChapterTitle(tabLayout.getSelectedTabPosition());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }

    private void setChapterTitle(int tabPosition){
        Chapter chapter = allChapters.get(tabPosition);
        String title = chapter.get_id()+". "+chapter.getTitle();
        getSupportActionBar().setTitle(title);
    }


    @Override
    public void onBackPressed() {
        if(isFabOpen){
            closeFAB(null);
            return;
        }

        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.fab_share:
                //Share this Chapter
                DataLoadHelper dlh = DataLoadHelper.getInstance();

                int chapterIndex = mChapterPager.getCurrentItem();
                int partIndex = 0;
                int sectionIndex = 0;
                Chapter chapter;
                Part part;
                Section section;
                try {
                    chapter = ContentHlpr.CHAPTERS.get(chapterIndex);
                }catch(IndexOutOfBoundsException indexExcp){
                    String msg = "Failed to get Chapter by index "+chapterIndex+" ContentHlpr.CHAPTERS ArrayList";
                    throw new Error(msg, new Throwable(msg));
                }
                try {
                    partIndex=chapter.getPartId()-1;
                    part = ContentHlpr.PARTS.get(partIndex);
                }catch(IndexOutOfBoundsException indexExcp){
                    String msg = "Failed to get Part by index "+partIndex+" ContentHlpr.PARTS ArrayList";
                    throw new Error(msg, new Throwable(msg));
                }
                try {
                    sectionIndex=chapter.getSectionId()-1;
                    section = ContentHlpr.SECTIONS.get(sectionIndex);
                }catch(IndexOutOfBoundsException indexExcp){
                    String msg = "Failed to get Section by index "+sectionIndex+" ContentHlpr.SECTIONS ArrayList";
                    throw new Error(msg, new Throwable(msg));
                }
                //
                String subject = getResources().getString(R.string.app_name)+" - "+chapter.get_id()+". "+chapter.getTitle();
                //
                String shareableText = "";

                //Add details
                shareableText += getResources().getString(R.string.chapter)+": "+chapter.get_id()+". "+chapter.getTitle();
                shareableText += "\n"+getResources().getString(R.string.section)+": "+section.get_id()+". "+section.getTitle();
                shareableText += "\n"+getResources().getString(R.string.part)+": "+part.get_id()+". "+part.getTitle()+"\n\n";

                ArrayList<Couplet> couplets = dlh.getCoupletsByChapter(chapter.get_id(), false);
                for(Couplet couplet:couplets){
                    shareableText+="\n"+couplet.get_id()+"\n"+couplet.getCouplet()+"\n";
                }

                shareableText +="\n\nhttps://play.google.com/store/apps/details?id=com.jpp.and_thirukkural";

                //Start SHARE the Chapter content
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareableText);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                startActivity(Intent.createChooser(shareIntent, getResources().getString(R.string.share_a_chapter)));
                break;
            case R.id.fab_pdf:
                break;
        }
    }

    private void createPDF(){

    }

    /*Create Framgment for Chapter Pager*/
    public static class ChapterPageFragment extends Fragment{
        public static final String CHAPTER_ID ="chapterId";

        public ChapterPageFragment(){

        }

        public static ChapterPageFragment newInstance(int position){
            ChapterPageFragment fragment = new ChapterPageFragment();
            Bundle args = new Bundle();
            args.putInt(CHAPTER_ID, position);
            fragment.setArguments(args);
            return fragment;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View chapterPageFragmentView = inflater.inflate(R.layout.fragment_chapterpage, container, false);
            int chapterID = getArguments().getInt(CHAPTER_ID);
            Chapter chapter = ContentHlpr.CHAPTERS.get(chapterID);

            //Load couplets in a chapter and display in a list
            DataLoadHelper dlh = DataLoadHelper.getInstance();
            ArrayList<Couplet> couplets = dlh.getCoupletsByChapter(chapter.get_id(), false);
            ListView coupletsListView = (ListView) chapterPageFragmentView.findViewById(R.id.chapterCoupletsListView);
            Couplet[] values = couplets.toArray(new Couplet[couplets.size()]);
            ListItemAdapter adapter = new ListItemAdapter(getContext(), values);
            coupletsListView.setAdapter(adapter);

            coupletsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Couplet couplet = (Couplet) parent.getItemAtPosition(position);
                    final Intent intent = new Intent((Activity) view.getContext(), CoupletSwipeActivity.class);
                    Bundle extras = new Bundle();
                    extras.putInt(Couplet.COUPLET_ID, couplet.get_id());
                    intent.putExtras(extras);


                    ThirukkuralBaseActivity myActivity = (ThirukkuralBaseActivity) view.getContext();
                    if(myActivity!=null && myActivity.isFabOpen)
                    {
                        myActivity.closeFAB(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                startActivity(intent);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                    }
                    else
                    {
                        startActivity(intent);
                    }
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
