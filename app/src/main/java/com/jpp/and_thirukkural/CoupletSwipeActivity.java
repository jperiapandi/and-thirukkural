package com.jpp.and_thirukkural;

import android.app.Activity;
import android.content.Intent;
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

import com.jpp.and_thirukkural.adapters.CoupletListItemAdapter;
import com.jpp.and_thirukkural.db.DataLoadHelper;
import com.jpp.and_thirukkural.model.Chapter;
import com.jpp.and_thirukkural.model.Couplet;
import com.jpp.and_thirukkural.model.Part;
import com.jpp.and_thirukkural.model.Section;

import java.util.ArrayList;

public class CoupletSwipeActivity extends AppCompatActivity {
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
        mCoupletPager = (ViewPager) findViewById(R.id.container);
        mCoupletPager.setAdapter(mCoupletsPagerAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_couplet_swipe, menu);
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
            TextView coupletTextViewEn = (TextView) coupletPageFragmentView.findViewById(R.id.couplet_text_en);
            TextView chapterTitleView = (TextView) coupletPageFragmentView.findViewById(R.id.chapter_title);
            TextView sectionTitleView = (TextView) coupletPageFragmentView.findViewById(R.id.section_title);
            TextView partTitleView = (TextView) coupletPageFragmentView.findViewById(R.id.part_title);

            TextView commentary1View = (TextView) coupletPageFragmentView.findViewById(R.id.commentary1);
            TextView commentary2View = (TextView) coupletPageFragmentView.findViewById(R.id.commentary2);
            TextView commentary3View = (TextView) coupletPageFragmentView.findViewById(R.id.commentary3);
            TextView commentary4View = (TextView) coupletPageFragmentView.findViewById(R.id.commentary4);
            TextView commentary5View = (TextView) coupletPageFragmentView.findViewById(R.id.commentary5);

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
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }
}
