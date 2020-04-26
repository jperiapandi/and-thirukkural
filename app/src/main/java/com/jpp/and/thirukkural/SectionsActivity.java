package com.jpp.and.thirukkural;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.FirebaseUiException;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jpp.and.thirukkural.adapters.ListItemAdapter;
import com.jpp.and.thirukkural.content.ContentHlpr;
import com.jpp.and.thirukkural.model.Chapter;
import com.jpp.and.thirukkural.model.ListItem;
import com.jpp.and.thirukkural.model.ListItemType;
import com.jpp.and.thirukkural.model.Part;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

public class SectionsActivity extends ThirukkuralBaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final int RC_SIGN_IN = 2000;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private static boolean isWelcomeDone = false;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sections);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        applyFontForToolbarTitle(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Load Data

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.sectionsPager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.sectionTabs);
        tabLayout.setupWithViewPager(mViewPager);
        createCustomTabs(tabLayout);

        //Check for user login
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            this.welcomeUser();
        }else {
            //No user is signed in
            this.invokeLogin();
        }
    }

    private void createCustomTabs(TabLayout tabLayout){
        int n = tabLayout.getTabCount();
        for(int i=0; i<n; i++){
            TextView tabTextView = (TextView) LayoutInflater.from(this).inflate(R.layout.tab_bar_item_layout, null);
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tabTextView.setText(tab.getText());
            tab.setCustomView(tabTextView);
        }
    }

    private void invokeLogin(){
//        this.printKeyHash();
        isWelcomeDone = false;
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build()
        );
        //Create a custom layout
        AuthMethodPickerLayout loginPickerLayout = new AuthMethodPickerLayout
                .Builder(R.layout.auth_picker_layout)
                .setGoogleButtonId(R.id.login_google_btn)
                .setFacebookButtonId(R.id.login_fb_btn)
                .build();
        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setIsSmartLockEnabled(false, false)
                        .setLogo(R.drawable.valluvar_icon)
                        .setAuthMethodPickerLayout(loginPickerLayout)
                        .build(),
                RC_SIGN_IN);
    }
    /*
    private void printKeyHash() {
        // Add code to print out the key hash
        try {

            PackageInfo info = getPackageManager().getPackageInfo("com.jpp.and.thirukkural", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("KeyHash:", e.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("KeyHash:", e.toString());
        }
    }
    */
    private void welcomeUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user==null){
            return;
        }
        String msg = "";
        //User is signed in
        int h =Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if(h>=4 && h<=11){
            //Morning
            msg = getResources().getString(R.string.good_morning)+" "+user.getDisplayName();
        }else if(h>=12 && h<=16){
            //Afternoon
            msg = getResources().getString(R.string.good_afternoon)+" "+user.getDisplayName();
        }else if(h>=17 && h<=19){
            //Evening
            msg = getResources().getString(R.string.good_evening)+" "+user.getDisplayName();
        }else{
            //Night
            msg = getResources().getString(R.string.good_night)+" "+user.getDisplayName();
        }

        if(!isWelcomeDone){
            isWelcomeDone = true;
            Snackbar.make(findViewById(R.id.drawer_layout), msg, Snackbar.LENGTH_LONG).show();
        }
        //

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        ImageView userPhoto = (ImageView) header.findViewById(R.id.user_photo_imageview);
        TextView displayName = (TextView) header.findViewById(R.id.display_name_txtview);
        TextView email = (TextView) header.findViewById(R.id.email_txtview);

        Glide.with(this).load(user.getPhotoUrl()).circleCrop().into(userPhoto);
        displayName.setText(user.getDisplayName());
        email.setText(user.getEmail());

        Menu menu = navigationView.getMenu();
        menu.setGroupVisible(R.id.user_menu_group, true);
        menu.findItem(R.id.login_menuItem).setVisible(false);
    }

    private void clearUserDetail() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        ImageView userPhoto = (ImageView) header.findViewById(R.id.user_photo_imageview);
        TextView displayName = (TextView) header.findViewById(R.id.display_name_txtview);
        TextView email = (TextView) header.findViewById(R.id.email_txtview);

        userPhoto.setImageResource(R.mipmap.ic_launcher);
        displayName.setText(getResources().getString(R.string.app_desc));
        email.setText("...");

        Menu menu = navigationView.getMenu();
        menu.setGroupVisible(R.id.user_menu_group, false);
        menu.findItem(R.id.login_menuItem).setVisible(true);
    }

    public void signOut(){
        AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                clearUserDetail();
                invokeLogin();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                this.welcomeUser();
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.

                this.onLoginError(response);
            }
        }
    }

    private void onLoginError(IdpResponse response) {
        this.clearUserDetail();
        if(response==null){
            //User pressed back button in device.
            return;
        }
        FirebaseUiException error = response.getError();
        switch (error.getErrorCode()){
            case ErrorCodes.NO_NETWORK:
                //No internet connection
                Snackbar.make(findViewById(R.id.drawer_layout), getResources().getString(R.string.err_no_internet), Snackbar.LENGTH_LONG).show();
                break;
            case ErrorCodes.PROVIDER_ERROR:
            default:
                Snackbar.make(findViewById(R.id.drawer_layout), getResources().getString(R.string.err_common_login_err), Snackbar.LENGTH_LONG).show();
                break;
        }
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.about_menuItem) {
            // Handle the camera action
            Intent i = new Intent(this, AboutThirukkuralActivity.class);
            startActivity(i);
        } else if(id == R.id.chapters_menuItem){
            Intent intent = new Intent(this, ChaptersListActivity.class);
            startActivity(intent);
        } else if(id == R.id.parts_menuItem){
            Intent intent = new Intent(this, PartListActivity.class);
            startActivity(intent);
        }  else if(id == R.id.couplets_menuItem){
            Intent intent = new Intent(this, AllCoupletsActivity.class);
            startActivity(intent);
        } else if(id == R.id.my_profile_menuItem){
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.my_favs_menuItem) {
            Intent intent = new Intent(this, FavoritesActivity.class);
            startActivity(intent);
        } else if(id == R.id.my_comments_menuItem){
            Intent intent = new Intent(this, CommentsActivity.class);
            startActivity(intent);
        }  else if(id == R.id.logout_menuItem){
            this.signOut();
        } else if(id == R.id.login_menuItem){
            this.invokeLogin();
        } else if (id == R.id.settings_menuItem) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

            ArrayList<Part> parts = ContentHlpr.getPartsBySection(sectionID);

            ArrayList<ListItem> items = new ArrayList<ListItem>();

            Iterator<Part> partsListIterator = parts.iterator();
            while (partsListIterator.hasNext()){
                Part part = partsListIterator.next();
                items.add((ListItem) part);

                ArrayList<Chapter> chapters = ContentHlpr.getChaptersByPart(part.get_id());
                part.setNumOfChapters(chapters.size());

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
            return ContentHlpr.SECTIONS.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title = ContentHlpr.SECTIONS.get(position).getTitle();
            return title;
        }
    }
}
