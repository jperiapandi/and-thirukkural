package com.jpp.and_thirukkural;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
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
import com.jpp.and_thirukkural.model.Commentary;
import com.jpp.and_thirukkural.model.Couplet;
import com.jpp.and_thirukkural.model.Part;
import com.jpp.and_thirukkural.model.Section;

import java.util.ArrayList;

public class CoupletSwipeActivity extends ThirukkuralBaseActivity {
    private static ArrayList<Couplet> allCouplets;
    static DataLoadHelper dlh;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private CoupletsPagerAdapter mCoupletsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mCoupletPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_couplet_swipe);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(allCouplets==null)
        {
            //Load data
            dlh = new DataLoadHelper(getApplicationContext());
            allCouplets = dlh.getAllCouplets(true);
        }


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mCoupletsPagerAdapter = new CoupletsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mCoupletPager = (ViewPager) findViewById(R.id.coupletPager);
        mCoupletPager.setAdapter(mCoupletsPagerAdapter);

        //Set selected tab based on extras received in the intent
        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            int coupletID = extras.getInt(Couplet.COUPLET_ID, 1);
            mCoupletPager.setCurrentItem(coupletID-1);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        configureSearchMenu(menu, R.menu.menu_couplet);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_COUPLET_ID = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_COUPLET_ID, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View coupletPageFragmentView = inflater.inflate(R.layout.fragment_couplet_swipe, container, false);

            int position = getArguments().getInt(ARG_COUPLET_ID) - 1;
            Couplet couplet = allCouplets.get(position);
            Chapter chapter = dlh.getChapterById(couplet.getChapterId());
            Section section = dlh.getSectionById(chapter.getSectionId());
            Part part = dlh.getPartById(chapter.getPartId());

            //Bind data to view
            TextView coupletIdView = (TextView) coupletPageFragmentView.findViewById(R.id.couplet_id);
            TextView coupletTextView = (TextView) coupletPageFragmentView.findViewById(R.id.couplet_text);
            TextView chapterTitleView = (TextView) coupletPageFragmentView.findViewById(R.id.chapter_title);
            TextView sectionTitleView = (TextView) coupletPageFragmentView.findViewById(R.id.section_title);
            TextView partTitleView = (TextView) coupletPageFragmentView.findViewById(R.id.part_title);

            ListView commentaryList = (ListView) coupletPageFragmentView.findViewById(R.id.commentaryList);
            ArrayList<Commentary> commentaries = new ArrayList<Commentary>();

            Commentary c1 = new Commentary();
            c1.setCommentaryBy(getResources().getString(R.string.commentaryBySolomonPappaiah));
            c1.setCommentary(couplet.getExpln_pappaiah());
            commentaries.add(c1);

            Commentary c2 = new Commentary();
            c2.setCommentaryBy(getResources().getString(R.string.commentaryByMuVaradarajan));
            c2.setCommentary(couplet.getExpln_muva());
            commentaries.add(c2);

            Commentary c3 = new Commentary();
            c3.setCommentaryBy(getResources().getString(R.string.commentaryByMuKarunanidhi));
            c3.setCommentary(couplet.getExpln_karunanidhi());
            commentaries.add(c3);

            Commentary c4 = new Commentary();
            c4.setCommentaryBy(getResources().getString(R.string.commentaryByManakudavar));
            c4.setCommentary(couplet.getExpln_manakudavar());
            commentaries.add(c4);


            Commentary c5 = new Commentary();
            c5.setCommentaryBy(getResources().getString(R.string.english));
            c5.setCommentary(couplet.getCouplet_en());
            commentaries.add(c5);


            Commentary c6 = new Commentary();
            c6.setCommentaryBy(getResources().getString(R.string.englishCommentary));
            c6.setCommentary(couplet.getExpln_en());
            commentaries.add(c6);

            coupletIdView.setText(getResources().getString(R.string.couplet)+"  "+couplet.get_id()+"");
            coupletTextView.setText(couplet.getCouplet());

            chapterTitleView.setText(chapter.get_id()+". "+chapter.getTitle());
            sectionTitleView.setText(section.get_id()+". "+section.getTitle());
            partTitleView.setText(part.get_id()+". "+part.getTitle());

            Commentary[] items = commentaries.toArray(new Commentary[commentaries.size()]);
            ListItemAdapter adapter = new ListItemAdapter(getContext(), items);
            commentaryList.setAdapter(adapter);

            return coupletPageFragmentView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class CoupletsPagerAdapter extends FragmentPagerAdapter {

        public CoupletsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 1330 total pages.
            return allCouplets.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return position+"";
        }
    }
}
