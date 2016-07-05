package com.jpp.and_thirukkural.db;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.util.Log;

import com.jpp.and_thirukkural.model.Couplet;
import com.jpp.and_thirukkural.provider.ThirukkuralContentProvider;

import java.util.ArrayList;

/**
 * Created by jperiapandi on 05-07-2016.
 */
public class DataLoadHelper {

    private final Context context;
    private final ContentResolver cr;

    public DataLoadHelper(Context context){
        this.context = context;
        this.cr = context.getContentResolver();
    }

    public Couplet getCoupletById(int coupletID){
        Couplet couplet = null;

        String[] mProjection = {CoupletTable.COL_ID, CoupletTable.COL_CHAPTER_ID, CoupletTable.COL_COUPLET};
        Cursor cursor = cr.query(ContentUris.withAppendedId(ThirukkuralContentProvider.COUPLETS_URI, coupletID), mProjection, null, null, null);

        if(cursor == null){
            //Error while retrieving data

        }else if(cursor.getCount()==0){
            //No data found

        }else{
            cursor.moveToFirst();

            couplet = new Couplet();
            couplet.set_id(cursor.getInt(cursor.getColumnIndex(CoupletTable.COL_ID)));
            couplet.setCouplet(cursor.getString(cursor.getColumnIndex(CoupletTable.COL_COUPLET)));

            cursor.close();
        }

        return couplet;
    }
    public ArrayList<Couplet> getCoupletsByChapter(int chapterID){
        ArrayList<Couplet> result = null;

        ContentResolver cr = context.getContentResolver();
        //Load data from couplet table

        String[] mProjection = {CoupletTable.COL_ID, CoupletTable.COL_CHAPTER_ID, CoupletTable.COL_COUPLET};
        String mSelectionClause = CoupletTable.COL_CHAPTER_ID+"=?";
        String[] mSelectionArgs = { chapterID+"" };

        Cursor cursor = cr.query(ThirukkuralContentProvider.COUPLETS_URI, mProjection, mSelectionClause, mSelectionArgs, null);

        if(cursor == null){
            //Error while retrieving data

        }else if(cursor.getCount()==0){
            //No data found

        }else{
            cursor.moveToFirst();
            result = new ArrayList<Couplet>();
            do{
                Couplet c = new Couplet();
                c.set_id(cursor.getInt(cursor.getColumnIndex(CoupletTable.COL_ID)));
                c.setCouplet(cursor.getString(cursor.getColumnIndex(CoupletTable.COL_COUPLET)));
                result.add(c);
            }while(cursor.moveToNext());

            cursor.close();
        }

        return result;
    }
}
