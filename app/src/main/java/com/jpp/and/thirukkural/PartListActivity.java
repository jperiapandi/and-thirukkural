package com.jpp.and.thirukkural;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.jpp.and.thirukkural.content.ContentHlpr;
import com.jpp.and.thirukkural.model.Part;

import java.util.ArrayList;

/**
 * An activity representing a list of Parts. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link PartDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class PartListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        View recyclerView = findViewById(R.id.part_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.part_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(ContentHlpr.PARTS));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final ArrayList<Part> mValues;

        public SimpleItemRecyclerViewAdapter(ArrayList<Part> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_part_partlist, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {

            holder.mItem = mValues.get(position);
            String partName = mValues.get(position).get_id()+"  "+mValues.get(position).getTitle();
            int n = ContentHlpr.getChaptersByPart(mValues.get(position).get_id()).size();
            String nChapters = n > 1 ? n+" "+getResources().getString(R.string.chapters) : n+" "+getResources().getString(R.string.chapter);
            holder.mPartName.setText(partName);
            holder.mChaptersCount.setText(nChapters);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putInt(PartDetailFragment.ARG_PART_ID, holder.mItem.get_id());
                        PartDetailFragment fragment = new PartDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.part_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, PartDetailActivity.class);
                        intent.putExtra(PartDetailFragment.ARG_PART_ID, holder.mItem.get_id());

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mChaptersCount;
            public final TextView mPartName;
            public Part mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mChaptersCount = (TextView) view.findViewById(R.id.chaptersCount);
                mPartName = (TextView) view.findViewById(R.id.partName);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mPartName.getText() + "'";
            }
        }
    }
}
