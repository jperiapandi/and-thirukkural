package com.jpp.and.thirukkural;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.widget.Toolbar;

import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.OnCompleteListener;
import com.google.android.play.core.tasks.Task;
import com.jpp.and.thirukkural.adapters.AllChaptersListItemAdapter;
import com.jpp.and.thirukkural.adapters.AllCoupletsListItemAdapter;
import com.jpp.and.thirukkural.adapters.ListItemAdapter;
import com.jpp.and.thirukkural.db.DataLoadHelper;
import com.jpp.and.thirukkural.model.Chapter;
import com.jpp.and.thirukkural.model.Couplet;
import com.jpp.and.thirukkural.model.ListItem;
import com.jpp.and.thirukkural.model.ListItemType;

import java.util.ArrayList;
import java.util.Objects;

public class AllCoupletsActivity extends ThirukkuralBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapters_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        ListView mList = findViewById(android.R.id.list);
        //
        DataLoadHelper dlh = DataLoadHelper.getInstance();
        ArrayList<Couplet> couplets = dlh.getAllCouplets(false);

        Couplet[] values = couplets.toArray(new Couplet[0]);

        ListItemAdapter adapter = new AllCoupletsListItemAdapter(getBaseContext(), values);
        mList.setAdapter(adapter);

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListItem clickedItem = (ListItem) parent.getItemAtPosition(position);
                if(clickedItem!=null && clickedItem.getListItemType() == ListItemType.COUPLET)
                {
                    //Open the clicked chapter in Main Activity
                    Couplet couplet = (Couplet) clickedItem;
                    Intent intent = new Intent(view.getContext(), CoupletSwipeActivity.class);
                    Bundle extras = new Bundle();
                    extras.putInt(Couplet.COUPLET_ID, couplet.get_id());
                    intent.putExtras(extras);
                    intent.setAction(Intent.ACTION_MAIN);
                    startActivity(intent);
                }
            }
        });

        //In-App Review
        ReviewManager manager = ReviewManagerFactory.create(this);
        Task<ReviewInfo> reviewFlow = manager.requestReviewFlow();
        Activity thisAct = this;
        reviewFlow.addOnCompleteListener(new OnCompleteListener<ReviewInfo>() {
            @Override
            public void onComplete(Task<ReviewInfo> task) {
                if(reviewFlow.isSuccessful()){
                    ReviewInfo reviewInfo = reviewFlow.getResult();
                    //
                    Task<Void> flow = manager.launchReviewFlow(thisAct, reviewInfo);
                    flow.addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(Task<Void> task) {
                            //Do nothing
                        }
                    });
                }
            }
        });

    }
}
