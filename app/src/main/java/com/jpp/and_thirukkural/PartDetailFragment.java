package com.jpp.and_thirukkural;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import com.jpp.and_thirukkural.adapters.ListItemAdapter;
import com.jpp.and_thirukkural.content.ContentHlpr;
import com.jpp.and_thirukkural.model.Chapter;
import com.jpp.and_thirukkural.model.Part;

import java.util.ArrayList;

/**
 * A fragment representing a single Part detail screen.
 * This fragment is either contained in a {@link PartListActivity}
 * in two-pane mode (on tablets) or a {@link PartDetailActivity}
 * on handsets.
 */
public class PartDetailFragment extends ListFragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_PART_ID = "partId";

    /**
     * The dummy content this fragment is presenting.
     */
    private Part mPart;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PartDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_PART_ID)) {
            // Load data
            int partId = getArguments().getInt(ARG_PART_ID);
            mPart = ContentHlpr.PARTS.get(partId-1);

            Activity activity = this.getActivity();
//            activity.setTitle(mPart.get_id()+". "+mPart.getTitle()+" - "+ContentHlpr.getChaptersByPart(mPart.get_id()).size());
            activity.setTitle(mPart.get_id()+". "+mPart.getTitle()+" "+getResources().getString(R.string.chapters));

            ArrayList<Chapter> chapters = ContentHlpr.getChaptersByPart(mPart.get_id());
            Chapter[] items = chapters.toArray(new Chapter[chapters.size()]);
            ListItemAdapter adapter = new ListItemAdapter(getContext(), items);
            setListAdapter(adapter);
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        super.onListItemClick(l, v, position, id);
        Chapter chapter = (Chapter) l.getItemAtPosition(position);
        Intent intent = new Intent(getContext(), ChapterActivity.class);
        Bundle extras = new Bundle();
        extras.putInt(Chapter.CHAPTER_ID, chapter.get_id());
        intent.putExtras(extras);
        intent.setAction(Intent.ACTION_MAIN);
        startActivity(intent);
    }
}
