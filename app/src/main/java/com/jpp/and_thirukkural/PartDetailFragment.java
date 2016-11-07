package com.jpp.and_thirukkural;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

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
    private TextView mTitle;
    private TextView mChaptersCount;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.part_detail,
                container, false);
        mTitle = (TextView) rootView.findViewById(R.id.title);
        mChaptersCount = (TextView) rootView.findViewById(R.id.chaptersCount);
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_PART_ID)) {
            // Load data
            int partId = getArguments().getInt(ARG_PART_ID);
            mPart = ContentHlpr.PARTS.get(partId-1);

            Activity activity = this.getActivity();


            ArrayList<Chapter> chapters = ContentHlpr.getChaptersByPart(mPart.get_id());
            Chapter[] items = chapters.toArray(new Chapter[chapters.size()]);
            ListItemAdapter adapter = new ListItemAdapter(getContext(), items);
            setListAdapter(adapter);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String titleStr = mPart.get_id()+". "+mPart.getTitle()+" "+getResources().getString(R.string.chapters);
        //set title
        if(mTitle != null){
            mTitle.setText(titleStr);
        }
        if(mChaptersCount != null){
            int n = ContentHlpr.getChaptersByPart(mPart.get_id()).size();
//            String nChapters = n > 1 ? n+" "+getResources().getString(R.string.chapters) : n+" "+getResources().getString(R.string.chapter);
            String nChapters = getResources().getString(R.string.total)+" "+n;

            mChaptersCount.setText(nChapters);
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
