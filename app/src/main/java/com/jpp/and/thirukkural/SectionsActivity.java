package com.jpp.and.thirukkural;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.FirebaseUiException;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jpp.and.thirukkural.adapters.ListItemAdapter;
import com.jpp.and.thirukkural.content.ContentHlpr;
import com.jpp.and.thirukkural.model.Chapter;
import com.jpp.and.thirukkural.model.ListItem;
import com.jpp.and.thirukkural.model.ListItemType;
import com.jpp.and.thirukkural.model.Part;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class SectionsActivity extends ThirukkuralBaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final int RC_SIGN_IN = 2000;

    private static boolean isWelcomeDone = false;

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                @Override
                public void onActivityResult(FirebaseAuthUIAuthenticationResult o) {
                    IdpResponse response = o.getIdpResponse();
                    if (o.getResultCode() == RESULT_OK) {
                        // Successfully signed in
                        welcomeUser();
                    } else {
                        onLoginError(response);
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sections);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(this);

        // Set up the ViewPager with the sections adapter.
        ViewPager2 mSectionsPager = findViewById(R.id.sectionsPager);
        mSectionsPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.sectionTabs);
        new TabLayoutMediator(tabLayout, mSectionsPager, (tab, position) -> tab.setText(ContentHlpr.SECTIONS.get(position).getTitle())).attach();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //Log in FirebaseAnalytics
                Bundle fbb = new Bundle();
                fbb.putString("section_id", ContentHlpr.SECTIONS.get(tab.getPosition()).get_id() + "");
                fbb.putString("section_title", ContentHlpr.SECTIONS.get(tab.getPosition()).getTitle());
                mFireBaseAnalytics.logEvent(Constants.VIEW_SECTION, fbb);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });
        //Check for user login
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            this.welcomeUser();
        } else {
            //No user is signed in
            this.invokeLogin();
        }
    }

    private void invokeLogin() {
        isWelcomeDone = false;
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );
        //Create a custom layout
        AuthMethodPickerLayout loginPickerLayout = new AuthMethodPickerLayout
                .Builder(R.layout.auth_picker_layout)
                .setGoogleButtonId(R.id.login_google_btn)
                .build();

        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build();
        signInLauncher.launch(signInIntent);
    }

    private void welcomeUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            return;
        }
        String msg;
        //User is signed in
        int h = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if (h >= 4 && h <= 11) {
            //Morning
            msg = getResources().getString(R.string.good_morning) + " " + user.getDisplayName();
        } else if (h >= 12 && h <= 16) {
            //Afternoon
            msg = getResources().getString(R.string.good_afternoon) + " " + user.getDisplayName();
        } else if (h >= 17 && h <= 19) {
            //Evening
            msg = getResources().getString(R.string.good_evening) + " " + user.getDisplayName();
        } else {
            //Night
            msg = getResources().getString(R.string.good_night) + " " + user.getDisplayName();
        }

        if (!isWelcomeDone) {
            isWelcomeDone = true;
            Snackbar.make(findViewById(R.id.drawer_layout), msg, Snackbar.LENGTH_LONG).show();
        }
        //
        // Set users profile picture, display name and email values.
        NavigationView navigationView = findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        ImageView userPhoto = header.findViewById(R.id.user_photo_imageview);
        TextView displayName = header.findViewById(R.id.display_name_txtview);
        TextView email = header.findViewById(R.id.email_txtview);

        Glide.with(this).load(user.getPhotoUrl()).circleCrop().into(userPhoto);
        displayName.setText(user.getDisplayName());
        email.setText(user.getEmail());

        Menu menu = navigationView.getMenu();
        menu.setGroupVisible(R.id.user_menu_group, true);
        menu.findItem(R.id.login_menuItem).setVisible(false);
    }

    private void clearUserDetail() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        ImageView userPhoto = header.findViewById(R.id.user_photo_imageview);
        TextView displayName = header.findViewById(R.id.display_name_txtview);
        TextView email = header.findViewById(R.id.email_txtview);

        userPhoto.setImageResource(R.mipmap.ic_launcher);
        displayName.setText(getResources().getString(R.string.app_desc));
        email.setText("...");

        Menu menu = navigationView.getMenu();
        menu.setGroupVisible(R.id.user_menu_group, false);
        menu.findItem(R.id.login_menuItem).setVisible(true);
    }

    public void signOut() {
        AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                clearUserDetail();
            }
        });
    }


    private void onLoginError(IdpResponse response) {
        this.clearUserDetail();
        if (response == null) {
            //User pressed back button in device.
            return;
        }
        FirebaseUiException error = response.getError();
        assert error != null;
        if (error.getErrorCode() == ErrorCodes.NO_NETWORK) {
            //No internet connection
            Snackbar.make(findViewById(R.id.drawer_layout), getResources().getString(R.string.err_no_internet), Snackbar.LENGTH_LONG).show();
        } else {
            Snackbar.make(findViewById(R.id.drawer_layout), getResources().getString(R.string.err_common_login_err), Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.about_menuItem) {
            // Handle the camera action
            Intent i = new Intent(this, AboutThirukkuralActivity.class);
            startActivity(i);
        } else if (id == R.id.chapters_menuItem) {
            Intent intent = new Intent(this, ChaptersListActivity.class);
            startActivity(intent);
        } else if (id == R.id.parts_menuItem) {
            Intent intent = new Intent(this, PartListActivity.class);
            startActivity(intent);
        } else if (id == R.id.couplets_menuItem) {
            Intent intent = new Intent(this, AllCoupletsActivity.class);
            startActivity(intent);
        } else if (id == R.id.my_profile_menuItem) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.my_favs_menuItem) {
            Intent intent = new Intent(this, FavoritesActivity.class);
            startActivity(intent);
        } else if (id == R.id.my_comments_menuItem) {
            Intent intent = new Intent(this, CommentsActivity.class);
            startActivity(intent);
        } else if (id == R.id.logout_menuItem) {
            this.signOut();
        } else if (id == R.id.login_menuItem) {
            this.invokeLogin();
        } else if (id == R.id.settings_menuItem) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class SectionFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public SectionFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static SectionFragment newInstance(int sectionNumber) {
            SectionFragment fragment = new SectionFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View sectionFragmentView = inflater.inflate(R.layout.fragment_sections, container, false);
            assert getArguments() != null;
            int sectionID = getArguments().getInt(ARG_SECTION_NUMBER);

            ArrayList<Part> parts = ContentHlpr.getPartsBySection(sectionID);

            ArrayList<ListItem> items = new ArrayList<>();

            for (Part part : parts) {
                items.add(part);

                ArrayList<Chapter> chapters = ContentHlpr.getChaptersByPart(part.get_id());
                part.setNumOfChapters(chapters.size());

                items.addAll(chapters);
            }


            ListView partsAndChaptersList = sectionFragmentView.findViewById(R.id.partsAndChaptersList);
            ListItem[] values = items.toArray(new ListItem[0]);
            ListItemAdapter adapter = new ListItemAdapter(getContext(), values);
            partsAndChaptersList.setAdapter(adapter);

            partsAndChaptersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ListItem clickedItem = (ListItem) parent.getItemAtPosition(position);
                    if (clickedItem != null && clickedItem.getListItemType() == ListItemType.CHAPTER) {

                        Chapter chapter = (Chapter) clickedItem;
                        //Log event in Firebase
                        FragmentActivity activity = requireActivity();
                        FirebaseAnalytics fba = FirebaseAnalytics.getInstance(activity);
                        Bundle fbBundle = new Bundle();
                        fbBundle.putString(FirebaseAnalytics.Param.ITEM_ID, chapter.get_id() + "");
                        fbBundle.putString(FirebaseAnalytics.Param.ITEM_NAME, chapter.getTitle());
                        fbBundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Chapter");
                        fba.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, fbBundle);

                        //Open the clicked chapter in Main Activity
                        Intent intent = new Intent(view.getContext(), ChapterActivity.class);
                        Bundle extras = new Bundle();
                        extras.putInt(Chapter.CHAPTER_ID, chapter.get_id());
                        intent.putExtras(extras);
                        intent.setAction(Intent.ACTION_MAIN);
                        startActivity(intent);
                    }
                }
            });
            return sectionFragmentView;
        }
    }

    public static class SectionsPagerAdapter extends FragmentStateAdapter {
        public SectionsPagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @Override
        public Fragment createFragment(int position) {
            return SectionFragment.newInstance(position + 1);
        }

        @Override
        public int getItemCount() {
            return ContentHlpr.SECTIONS.size();
        }
    }
}
