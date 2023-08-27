package com.jpp.and.thirukkural;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.jpp.and.thirukkural.content.ContentHlpr;
import com.jpp.and.thirukkural.db.DataLoadHelper;
import com.jpp.and.thirukkural.model.Chapter;
import com.jpp.and.thirukkural.model.Couplet;
import com.jpp.and.thirukkural.model.Part;
import com.jpp.and.thirukkural.model.Section;

import java.util.Locale;
import java.util.Objects;

public class CoupletSwipeActivity extends ThirukkuralBaseActivity {

    private ViewPager2 mCoupletPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_couplet_swipe);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        CoupletsPagerAdapter mCoupletsPagerAdapter = new CoupletsPagerAdapter(this);

        // Set up the ViewPager with the sections adapter.
        mCoupletPager = findViewById(R.id.coupletViewPager);
        mCoupletPager.setAdapter(mCoupletsPagerAdapter);
        mCoupletPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setActivityTitle(position);

                //Log in FirebaseAnalytics
                Bundle fbb = new Bundle();
                fbb.putString("couplet_id", ContentHlpr.COUPLETS.get(position).get_id() + "");
                fbb.putString("couplet_desc", ContentHlpr.COUPLETS.get(position).getShortDesc());
                mFireBaseAnalytics.logEvent(Constants.VIEW_COUPLET, fbb);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

        //Set selected tab based on extras received in the intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int coupletID = extras.getInt(Couplet.COUPLET_ID, 1);
            boolean isFromWidget = extras.getBoolean(Constants.IS_FROM_WIDGET);
            boolean isFromNotification = extras.getBoolean(Constants.IS_FROM_NOTIFICATION);
            mCoupletPager.setCurrentItem(coupletID - 1);

            if (isFromWidget) {
                Bundle fbBundle = new Bundle();
                fbBundle.putString(FirebaseAnalytics.Param.ITEM_ID, coupletID + "");
                fbBundle.putString(FirebaseAnalytics.Param.ITEM_NAME, ContentHlpr.COUPLETS.get(coupletID - 1).getShortDesc());
                fbBundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "CoupletFromWidget");
                mFireBaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, fbBundle);
            }
            if (isFromNotification) {
                Bundle fbBundle = new Bundle();
                fbBundle.putString(FirebaseAnalytics.Param.ITEM_ID, coupletID + "");
                fbBundle.putString(FirebaseAnalytics.Param.ITEM_NAME, ContentHlpr.COUPLETS.get(coupletID - 1).getShortDesc());
                fbBundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "CoupletFromNotification");
                mFireBaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, fbBundle);
            }
        }

        setActivityTitle(mCoupletPager.getCurrentItem());
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


    private void setActivityTitle(int position) {
        Couplet couplet = ContentHlpr.COUPLETS.get(position);
        String title = getResources().getString(R.string.couplet) + " " + couplet.get_id();
        Objects.requireNonNull(getSupportActionBar()).setTitle(title);

        Chapter chapter = ContentHlpr.CHAPTERS.get(couplet.getChapterId() - 1);
        Section section = ContentHlpr.SECTIONS.get(chapter.getSectionId() - 1);
        Part part = ContentHlpr.PARTS.get(chapter.getPartId() - 1);

        String detail = section.get_id() + ". " + section.getTitle();
        detail += "   " + part.get_id() + ". " + part.getTitle();
        detail += "   " + chapter.get_id() + ". " + chapter.getTitle();

        TextView coupletDetailTextView = findViewById(R.id.coupletDetail);
        coupletDetailTextView.setText(detail);
    }

    @Override
    public void onBackPressed() {
        if (isFabOpen) {
            closeFAB(null);
            return;
        }

        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int coupletIndex;
        Couplet couplet;

        switch (v.getId()) {
            case R.id.fab_favorite:
                coupletIndex = mCoupletPager.getCurrentItem();

                couplet = ContentHlpr.COUPLETS.get(coupletIndex);

                if (couplet.getFav() == 1) {
                    couplet.setFav(0);
                    DataLoadHelper.getInstance().unmarkFavoriteCouplet(couplet.get_id());
                    Toast.makeText(getBaseContext(), getResources().getText(R.string.unmarked_as_favourite), Toast.LENGTH_SHORT).show();
                } else {
                    couplet.setFav(1);
                    DataLoadHelper.getInstance().markFavoriteCouplet(couplet.get_id());
                    Toast.makeText(getBaseContext(), getResources().getText(R.string.marked_as_favourite), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.fab_share:
                //Share a couplet
                shareCouplet();
                break;
        }
    }

    private void shareCouplet() {

        int coupletIndex = mCoupletPager.getCurrentItem();
        Couplet couplet = ContentHlpr.COUPLETS.get(coupletIndex);

        int chapterIndex = couplet.getChapterId() - 1;
        int partIndex = 0;
        int sectionIndex = 0;

        Chapter chapter;
        Part part;
        Section section;

        try {
            chapter = ContentHlpr.CHAPTERS.get(chapterIndex);
        } catch (IndexOutOfBoundsException indexExcp) {
            String msg = "Failed to get Chapter by index " + chapterIndex + " ContentHlpr.CHAPTERS ArrayList";
            throw new Error(msg, new Throwable(msg));
        }
        try {
            partIndex = chapter.getPartId() - 1;
            part = ContentHlpr.PARTS.get(partIndex);
        } catch (IndexOutOfBoundsException indexExcp) {
            String msg = "Failed to get Part by index " + partIndex + " ContentHlpr.PARTS ArrayList";
            throw new Error(msg, new Throwable(msg));
        }
        try {
            sectionIndex = chapter.getSectionId() - 1;
            section = ContentHlpr.SECTIONS.get(sectionIndex);
        } catch (IndexOutOfBoundsException indexExcp) {
            String msg = "Failed to get Section by index " + sectionIndex + " ContentHlpr.SECTIONS ArrayList";
            throw new Error(msg, new Throwable(msg));
        }

        //
        boolean showCom1 = PreferenceManager.getDefaultSharedPreferences(this.getBaseContext()).getBoolean(SettingsActivity.KEY_COMM_1, true);
        boolean showCom2 = PreferenceManager.getDefaultSharedPreferences(this.getBaseContext()).getBoolean(SettingsActivity.KEY_COMM_2, true);
        boolean showCom3 = PreferenceManager.getDefaultSharedPreferences(this.getBaseContext()).getBoolean(SettingsActivity.KEY_COMM_3, true);
        boolean showCom4 = PreferenceManager.getDefaultSharedPreferences(this.getBaseContext()).getBoolean(SettingsActivity.KEY_COMM_4, true);
        boolean showCom5 = PreferenceManager.getDefaultSharedPreferences(this.getBaseContext()).getBoolean(SettingsActivity.KEY_COMM_5, true);
        //
        String subject = getResources().getString(R.string.app_name) + " - " + getResources().getString(R.string.couplet) + " " + couplet.get_id();
        //
        String shareableText = "%1$s: %2$d\n%3$s";
        shareableText = String.format(new Locale("ta", "in"), shareableText,
                getResources().getString(R.string.couplet),
                couplet.get_id(),
                couplet.getCouplet());

        //Add details
        shareableText += "\n\n" + getResources().getString(R.string.section) + ": " + section.get_id() + ". " + section.getTitle();
        shareableText += "\n" + getResources().getString(R.string.part) + ": " + part.get_id() + ". " + part.getTitle();
        shareableText += "\n" + getResources().getString(R.string.chapter) + ": " + chapter.get_id() + ". " + chapter.getTitle();
        //Add Pappaiah commentary
        if (showCom1) {
            shareableText += "\n\n" + getResources().getString(R.string.commentaryBySolomonPappaiah) + ":\n" + couplet.getExpln_pappaiah();
        }

        if (showCom2) {
            //Add Mu.Va commentary
            shareableText += "\n\n" + getResources().getString(R.string.commentaryByMuVaradarajan) + ":\n" + couplet.getExpln_muva();
        }
        if (showCom3) {
            //Add Karunanidhi commentary
            shareableText += "\n\n" + getResources().getString(R.string.commentaryByMuKarunanidhi) + ":\n" + couplet.getExpln_karunanidhi();
        }
        if (showCom4) {
            if (couplet.getExpln_manakudavar() != null) {
                //Add Manakudavar commentary
                shareableText += "\n\n" + getResources().getString(R.string.commentaryByManakudavar) + ":\n" + couplet.getExpln_manakudavar();
            }
        }
        if (showCom5) {
            //Add English couplet
            shareableText += "\n\n" + getResources().getString(R.string.english) + ":\n" + couplet.getCouplet_en();

            //Add English commentary
            shareableText += "\n\n" + getResources().getString(R.string.englishCommentary) + ":\n" + couplet.getExpln_en();
        }

        shareableText += "\n\nhttps://play.google.com/store/apps/details?id=com.jpp.and.thirukkural";

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareableText);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        startActivity(Intent.createChooser(shareIntent, getResources().getString(R.string.share_a_couplet)));

        Bundle fbb = new Bundle();
        fbb.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Couplet");
        fbb.putString(FirebaseAnalytics.Param.ITEM_ID, couplet.get_id() + "");
        fbb.putString(FirebaseAnalytics.Param.ITEM_NAME, couplet.getShortDesc());
        fbb.putString(FirebaseAnalytics.Param.METHOD, "Android");
        this.mFireBaseAnalytics.logEvent(FirebaseAnalytics.Event.SHARE, fbb);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class CoupletPageFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String COUPLET_ID = "couplet_id";

        public CoupletPageFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static CoupletPageFragment newInstance(int coupletID) {
            CoupletPageFragment fragment = new CoupletPageFragment();
            Bundle args = new Bundle();
            args.putInt(COUPLET_ID, coupletID);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_couplet_swipe, container, false);

            assert getArguments() != null;
            int index = getArguments().getInt(COUPLET_ID) - 1;
            Couplet couplet = ContentHlpr.COUPLETS.get(index);

            //Bind data to view
            TextView coupletTextView = view.findViewById(R.id.couplet_text);
            TextView allCommentaries = view.findViewById(R.id.all_commentariesTextView);

            boolean showCom1 = PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean(SettingsActivity.KEY_COMM_1, true);
            boolean showCom2 = PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean(SettingsActivity.KEY_COMM_2, true);
            boolean showCom3 = PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean(SettingsActivity.KEY_COMM_3, true);
            boolean showCom4 = PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean(SettingsActivity.KEY_COMM_4, true);
            boolean showCom5 = PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean(SettingsActivity.KEY_COMM_5, true);

            SpannableStringBuilder spanBuilder = new SpannableStringBuilder();
            SpannableString nl = new SpannableString(System.getProperty("line.separator") + System.getProperty("line.separator"));
            if (showCom1) {
                SpannableString s1 = new SpannableString(getResources().getString(R.string.commentaryBySolomonPappaiah));
                s1.setSpan(new TextAppearanceSpan(getContext(), R.style.Commentary_Title), 0, s1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s2 = new SpannableString(couplet.getExpln_pappaiah());

                spanBuilder.append(s1);
                spanBuilder.append(nl);
                spanBuilder.append(s2);
                spanBuilder.append(nl);
            }

            if (showCom2) {
                SpannableString s1 = new SpannableString(getResources().getString(R.string.commentaryByMuVaradarajan));
                s1.setSpan(new TextAppearanceSpan(getContext(), R.style.Commentary_Title), 0, s1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s2 = new SpannableString(couplet.getExpln_muva());

                spanBuilder.append(s1);
                spanBuilder.append(nl);
                spanBuilder.append(s2);
                spanBuilder.append(nl);
            }
            if (showCom3) {
                SpannableString s1 = new SpannableString(getResources().getString(R.string.commentaryByMuKarunanidhi));
                s1.setSpan(new TextAppearanceSpan(getContext(), R.style.Commentary_Title), 0, s1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s2 = new SpannableString(couplet.getExpln_karunanidhi());

                spanBuilder.append(s1);
                spanBuilder.append(nl);
                spanBuilder.append(s2);
                spanBuilder.append(nl);
            }
            if (showCom4) {
                if (couplet.getExpln_manakudavar() != null) {
                    SpannableString s1 = new SpannableString(getResources().getString(R.string.commentaryByManakudavar));
                    s1.setSpan(new TextAppearanceSpan(getContext(), R.style.Commentary_Title), 0, s1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    SpannableString s2 = new SpannableString(couplet.getExpln_manakudavar());

                    spanBuilder.append(s1);
                    spanBuilder.append(nl);
                    spanBuilder.append(s2);
                    spanBuilder.append(nl);
                }
            }
            if (showCom5) {
                SpannableString s1 = new SpannableString(getResources().getString(R.string.english));
                s1.setSpan(new TextAppearanceSpan(getContext(), R.style.Commentary_Title), 0, s1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s2 = new SpannableString(couplet.getCouplet_en());

                spanBuilder.append(s1);
                spanBuilder.append(nl);
                spanBuilder.append(s2);
                spanBuilder.append(nl);

                SpannableString s3 = new SpannableString(getResources().getString(R.string.englishCommentary));
                s3.setSpan(new TextAppearanceSpan(getContext(), R.style.Commentary_Title), 0, s3.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s4 = new SpannableString(couplet.getExpln_en());

                spanBuilder.append(s3);
                spanBuilder.append(nl);
                spanBuilder.append(s4);
            }
            spanBuilder.append(nl);
            spanBuilder.append(nl);
            spanBuilder.append(nl);
            coupletTextView.setText(couplet.getCouplet());
            if (couplet.getFav() == 1) {
                int color = ContextCompat.getColor(requireContext(), R.color.fav_couplete_color);
                coupletTextView.setTextColor(color);
            }

            allCommentaries.setText(spanBuilder);
            return view;
        }

    }

    public static class CoupletsPagerAdapter extends FragmentStateAdapter {

        public CoupletsPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return CoupletPageFragment.newInstance(position + 1);
        }

        @Override
        public int getItemCount() {
            return ContentHlpr.COUPLETS.size();
        }
    }
}
