package com.jpp.and.thirukkural;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class SettingsActivity extends ThirukkuralBaseActivity {
    public static final String TAG = "ThirukkuralAPP";
    public static final String KEY_COMM_1 = "commentary1";
    public static final String KEY_COMM_2 = "commentary2";
    public static final String KEY_COMM_3 = "commentary3";
    public static final String KEY_COMM_4 = "commentary4";
    public static final String KEY_COMM_5 = "commentary5";
    public static final String KEY_MORNING_COUPLET = "morningCouplet";
    public static final String KEY_EVENING_COUPLET = "eveningCouplet";

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        getFragmentManager().
                beginTransaction().
                replace(R.id.frameLayout, new ThirukkuralPreferenceFragment()).
                commit();
        //
        this.db.collection("preference")
                .get()
                .addOnCompleteListener(
                        new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    for(QueryDocumentSnapshot doc:task.getResult()){
                                        Log.d(TAG, doc.getId()+" => "+doc.getData());
                                    }
                                }else{
                                    Log.w(TAG, "Error getting preference", task.getException());
                                }
                            }
                        }
                );
    }

    public static class ThirukkuralPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.app_preference);
        }
    }
}
