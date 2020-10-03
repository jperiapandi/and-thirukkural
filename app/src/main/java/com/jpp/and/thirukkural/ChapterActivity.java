package com.jpp.and.thirukkural;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.jpp.and.thirukkural.adapters.ListItemAdapter;
import com.jpp.and.thirukkural.content.ContentHlpr;
import com.jpp.and.thirukkural.db.DataLoadHelper;
import com.jpp.and.thirukkural.model.Chapter;
import com.jpp.and.thirukkural.model.Couplet;
import com.jpp.and.thirukkural.model.Part;
import com.jpp.and.thirukkural.model.Section;

import java.util.ArrayList;
import java.util.Objects;

public class ChapterActivity extends ThirukkuralBaseActivity implements SearchView.OnQueryTextListener {

    private static ArrayList<Chapter> allChapters;

    private ViewPager2 mChapterPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chapter);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // Load data
        DataLoadHelper dlh = DataLoadHelper.getInstance();
        allChapters = dlh.getAllChapters();


        // Create the adapter that will return a fragment for each of the chapters
        ChapterPagerAdapter2 mChapterPagerAdapter = new ChapterPagerAdapter2(this);

        // Set up the ViewPager with the sections adapter.
        mChapterPager = findViewById(R.id.chapterPager);
        mChapterPager.setAdapter(mChapterPagerAdapter);
        mChapterPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setChapterTitle(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

        TabLayout tabLayout = findViewById(R.id.chapterTabs);
        new TabLayoutMediator(tabLayout, mChapterPager, (tab, position)->tab.setText(allChapters.get(position).get_id()+"")).attach();

        //Set selected tab based on extras received in the intent
        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            int chapterID = extras.getInt(Chapter.CHAPTER_ID, 1);
            TabLayout.Tab targetTab = tabLayout.getTabAt(chapterID-1);
            assert targetTab != null;
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
        Objects.requireNonNull(getSupportActionBar()).setTitle(title);
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
                StringBuilder shareableText = new StringBuilder();

                //Add details
                shareableText.append(getResources().getString(R.string.chapter)).append(": ").append(chapter.get_id()).append(". ").append(chapter.getTitle());
                shareableText.append("\n").append(getResources().getString(R.string.section)).append(": ").append(section.get_id()).append(". ").append(section.getTitle());
                shareableText.append("\n").append(getResources().getString(R.string.part)).append(": ").append(part.get_id()).append(". ").append(part.getTitle()).append("\n\n");

                ArrayList<Couplet> couplets = dlh.getCoupletsByChapter(chapter.get_id(), false);
                for(Couplet couplet:couplets){
                    shareableText.append("\n").append(couplet.get_id()).append("\n").append(couplet.getCouplet()).append("\n");
                }

                shareableText.append("\n\nhttps://play.google.com/store/apps/details?id=com.jpp.and.thirukkural");

                //Start SHARE the Chapter content
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareableText.toString());
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                startActivity(Intent.createChooser(shareIntent, getResources().getString(R.string.share_a_chapter)));
                break;
            case R.id.fab_pdf:
                break;
        }
    }

    /*Create Fragment for Chapter Pager*/
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
            assert getArguments() != null;
            int chapterID = getArguments().getInt(CHAPTER_ID);
            Chapter chapter = ContentHlpr.CHAPTERS.get(chapterID);

            //Load couplets in a chapter and display in a list
            DataLoadHelper dlh = DataLoadHelper.getInstance();
            ArrayList<Couplet> couplets = dlh.getCoupletsByChapter(chapter.get_id(), false);
            ListView coupletsListView = chapterPageFragmentView.findViewById(R.id.chapterCoupletsListView);
            Couplet[] values = couplets.toArray(new Couplet[0]);
            ListItemAdapter adapter = new ListItemAdapter(getContext(), values);
            coupletsListView.setAdapter(adapter);

            coupletsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Couplet couplet = (Couplet) parent.getItemAtPosition(position);
                    final Intent intent = new Intent(view.getContext(), CoupletSwipeActivity.class);
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

    public static class ChapterPagerAdapter2 extends FragmentStateAdapter {

        public ChapterPagerAdapter2(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return ChapterPageFragment.newInstance(position);
        }

        @Override
        public int getItemCount() {
            return allChapters.size();
        }
    }
}
