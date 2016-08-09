package com.jpp.and_thirukkural;

import android.app.Activity;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ListView;

import com.jpp.and_thirukkural.adapters.ListItemAdapter;
import com.jpp.and_thirukkural.db.DataLoadHelper;
import com.jpp.and_thirukkural.model.Chapter;
import com.jpp.and_thirukkural.model.ListItem;
import com.jpp.and_thirukkural.model.ListItemType;
import com.jpp.and_thirukkural.model.Part;
import com.jpp.and_thirukkural.model.Section;

import java.util.ArrayList;
import java.util.Iterator;

public class SectionsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {
    private ArrayList<Section> sections;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private DataLoadHelper dataLoadHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sections);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Load Data
        dataLoadHelper = new DataLoadHelper(getApplicationContext());
        sections = dataLoadHelper.getAllSections();


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.sectionsPager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.sectionTabs);
        tabLayout.setupWithViewPager(mViewPager);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sections, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_menu_item).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(this, SearchResultsActivity.class)));

        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.about_thirukkural) {
            // Handle the camera action
            Intent i = new Intent(this, AboutThirukkuralActivity.class);
            startActivity(i);
        } else if (id == R.id.book_index) {
            Intent i = new Intent(this, SectionsActivity.class);
            startActivity(i);
        } else if (id == R.id.fav_couplets) {
            Intent intent = new Intent(this, FavoritesActivity.class);
            startActivity(intent);
        } /*else if (id == R.id.about_app_creator) {
            Intent intent = new Intent(this, AboutDeveloperActivity.class);
            startActivity(intent);
        } */else if (id == R.id.settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.i("Search submit" , query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_sections, container, false);
            int sectionID = getArguments().getInt(ARG_SECTION_NUMBER);

            DataLoadHelper dlh = new DataLoadHelper(getContext());
            ArrayList<Part> parts = dlh.getPartsBySectionId(sectionID);

            ArrayList<ListItem> items = new ArrayList<ListItem>();

            Iterator<Part> partsListIterator = parts.iterator();
            while (partsListIterator.hasNext()){
                Part part = partsListIterator.next();
                items.add((ListItem) part);

                ArrayList<Chapter> chapters = dlh.getChaptersByPartId(part.get_id());
                Iterator<Chapter> chapterIterator = chapters.iterator();
                while (chapterIterator.hasNext()){
                    Chapter chapter = chapterIterator.next();
                    items.add((ListItem) chapter);
                }
            }


            ListView partsAndChaptersList = (ListView) rootView.findViewById(R.id.partsAndChaptersList);
            ListItem[] values = items.toArray(new ListItem[items.size()]);
            ListItemAdapter adapter = new ListItemAdapter(getContext(), values);
            partsAndChaptersList.setAdapter(adapter);

            partsAndChaptersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
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
            return sections.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return sections.get(position).getTitle();
        }
    }
}
