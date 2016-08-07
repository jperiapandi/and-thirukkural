package com.jpp.and_thirukkural.db;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;

import com.jpp.and_thirukkural.model.Chapter;
import com.jpp.and_thirukkural.model.Couplet;
import com.jpp.and_thirukkural.model.Part;
import com.jpp.and_thirukkural.model.Section;
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

        String[] mProjection = {CoupletsTable.COL_ID, CoupletsTable.COL_CHAPTER_ID, CoupletsTable.COL_COUPLET};
        Cursor cursor = cr.query(ContentUris.withAppendedId(ThirukkuralContentProvider.COUPLETS_URI, coupletID), mProjection, null, null, null);

        if(cursor == null){
            //Error while retrieving data

        }else if(cursor.getCount()==0){
            //No data found

        }else{
            cursor.moveToFirst();

            couplet = new Couplet();
            couplet.set_id(cursor.getInt(cursor.getColumnIndex(CoupletsTable.COL_ID)));
            couplet.setCouplet(cursor.getString(cursor.getColumnIndex(CoupletsTable.COL_COUPLET)));

            cursor.close();
        }

        return couplet;
    }
    public ArrayList<Couplet> getCoupletsByChapter(int chapterID){
        ArrayList<Couplet> result = null;

        ContentResolver cr = context.getContentResolver();
        //Load data from couplet table

        String[] mProjection = {CoupletsTable.COL_ID, CoupletsTable.COL_CHAPTER_ID, CoupletsTable.COL_COUPLET};
        String mSelectionClause = CoupletsTable.COL_CHAPTER_ID+"=?";
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
                c.set_id(cursor.getInt(cursor.getColumnIndex(CoupletsTable.COL_ID)));
                c.setCouplet(cursor.getString(cursor.getColumnIndex(CoupletsTable.COL_COUPLET)));
                result.add(c);
            }while(cursor.moveToNext());

            cursor.close();
        }

        return result;
    }

    public ArrayList<Section> getAllSections(){
        ArrayList<Section> result =null;
        ContentResolver cr = context.getContentResolver();
        //Load data from sections table

        String[] mProjection = {SectionsTable.COL_ID, SectionsTable.COL_TTILE};
        String mSelectionClause = null;
        String[] mSelectionArgs = null;
        String sortOrder = null;

        Cursor cursor = cr.query(ThirukkuralContentProvider.SECTIONS_URI, mProjection, mSelectionClause, mSelectionArgs, sortOrder);

        if(cursor == null){
            //Error while retrieving data

        }else if(cursor.getCount()==0){
            //No data found

        }else{
            cursor.moveToFirst();
            result = new ArrayList<Section>();
            do{
                Section c = new Section();
                c.set_id(cursor.getInt(cursor.getColumnIndex(SectionsTable.COL_ID)));
                c.setTitle(cursor.getString(cursor.getColumnIndex(SectionsTable.COL_TTILE)));
                result.add(c);
            }while(cursor.moveToNext());

            cursor.close();
        }

        return result;
    }

    public Section getSectionById(int sectionID){
        Section result = null;

        String[] mProjection = {SectionsTable.COL_ID,SectionsTable.COL_TTILE};
        Cursor cursor = cr.query(ContentUris.withAppendedId(ThirukkuralContentProvider.SECTIONS_URI, sectionID), mProjection, null, null, null);

        if(cursor == null){
            //Error while retrieving data

        }else if(cursor.getCount()==0){
            //No data found

        }else{
            cursor.moveToFirst();

            result = new Section();
            result.set_id(cursor.getInt(cursor.getColumnIndex(SectionsTable.COL_ID)));
            result.setTitle(cursor.getString(cursor.getColumnIndex(SectionsTable.COL_TTILE)));

            cursor.close();
        }

        return result;
    }

    public ArrayList<Chapter> getAllChapters(){
        ArrayList<Chapter> result =null;
        ContentResolver cr = context.getContentResolver();
        //Load data from chapters table

        String[] mProjection = {ChaptersTable.COL_ID, ChaptersTable.COL_PART_ID, ChaptersTable.COL_SECTION_ID, ChaptersTable.COL_TITLE};
        String mSelectionClause = null;
        String[] mSelectionArgs = null;
        String sortOrder = null;

        Cursor cursor = cr.query(ThirukkuralContentProvider.CHAPTERS_URI, mProjection, mSelectionClause, mSelectionArgs, sortOrder);

        if(cursor == null){
            //Error while retrieving data

        }else if(cursor.getCount()==0){
            //No data found

        }else{
            cursor.moveToFirst();
            result = new ArrayList<Chapter>();
            do{
                Chapter c = new Chapter();
                c.set_id(cursor.getInt(cursor.getColumnIndex(ChaptersTable.COL_ID)));
                c.setTitle(cursor.getString(cursor.getColumnIndex(ChaptersTable.COL_TITLE)));
                c.setPartId(cursor.getInt(cursor.getColumnIndex(ChaptersTable.COL_PART_ID)));
                c.setSectionId(cursor.getInt(cursor.getColumnIndex(ChaptersTable.COL_SECTION_ID)));
                result.add(c);
            }while(cursor.moveToNext());

            cursor.close();
        }

        return result;
    }
    public ArrayList<Chapter> getChaptersBySectionId(int sectionId){
        ArrayList<Chapter> result =null;
        ContentResolver cr = context.getContentResolver();
        //Load data from chapters table

        String[] mProjection = {ChaptersTable.COL_ID, ChaptersTable.COL_PART_ID, ChaptersTable.COL_SECTION_ID, ChaptersTable.COL_TITLE};
        String mSelectionClause = ChaptersTable.COL_SECTION_ID+"=?";
        String[] mSelectionArgs = { sectionId+"" };
        String sortOrder = null;

        Cursor cursor = cr.query(ThirukkuralContentProvider.CHAPTERS_URI, mProjection, mSelectionClause, mSelectionArgs, sortOrder);
        if (cursor != null ) {
            if  (cursor.moveToFirst()) {
                result = new ArrayList<Chapter>();
                do {
                    Chapter c = new Chapter();
                    c.set_id(cursor.getInt(cursor.getColumnIndex(ChaptersTable.COL_ID)));
                    c.setTitle(cursor.getString(cursor.getColumnIndex(ChaptersTable.COL_TITLE)));
                    c.setPartId(cursor.getInt(cursor.getColumnIndex(ChaptersTable.COL_PART_ID)));
                    c.setSectionId(cursor.getInt(cursor.getColumnIndex(ChaptersTable.COL_SECTION_ID)));
                    result.add(c);
                }while (cursor.moveToNext());
            }
        }
        cursor.close();

        return result;
    }

    public ArrayList<Part> getAllParts(){
        ArrayList<Part> result =null;
        ContentResolver cr = context.getContentResolver();
        //Load data from sections table

        String[] mProjection = {PartsTable.COL_ID, PartsTable.COL_TITLE, PartsTable.COL_SECTION_ID};
        String mSelectionClause = null;
        String[] mSelectionArgs = null;
        String sortOrder = null;

        Cursor cursor = cr.query(ThirukkuralContentProvider.PARTS_URI, mProjection, mSelectionClause, mSelectionArgs, sortOrder);
        if (cursor != null ) {
            if  (cursor.moveToFirst()) {
                result = new ArrayList<Part>();
                do {
                    Part c = new Part();
                    c.set_id(cursor.getInt(cursor.getColumnIndex(PartsTable.COL_ID)));
                    c.setTitle(cursor.getString(cursor.getColumnIndex(PartsTable.COL_TITLE)));
                    c.setSectionId(cursor.getInt(cursor.getColumnIndex(PartsTable.COL_SECTION_ID)));
                    result.add(c);
                }while (cursor.moveToNext());
            }
        }
        cursor.close();

        return result;
    }

    public Part getPartById(int partID){
        Part result = null;

        String[] mProjection = {PartsTable.COL_ID, PartsTable.COL_TITLE, PartsTable.COL_SECTION_ID};
        Cursor cursor = cr.query(ContentUris.withAppendedId(ThirukkuralContentProvider.PARTS_URI, partID), mProjection, null, null, null);

        if(cursor == null){
            //Error while retrieving data

        }else if(cursor.getCount()==0){
            //No data found

        }else{
            cursor.moveToFirst();

            result = new Part();
            result.set_id(cursor.getInt(cursor.getColumnIndex(PartsTable.COL_ID)));
            result.setTitle(cursor.getString(cursor.getColumnIndex(PartsTable.COL_TITLE)));
            result.setSectionId(cursor.getInt(cursor.getColumnIndex(PartsTable.COL_SECTION_ID)));

            cursor.close();
        }

        return result;
    }
}
