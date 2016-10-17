package com.jpp.and_thirukkural.db;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;

import com.jpp.and_thirukkural.model.Chapter;
import com.jpp.and_thirukkural.model.Couplet;
import com.jpp.and_thirukkural.model.Part;
import com.jpp.and_thirukkural.model.SearchHistory;
import com.jpp.and_thirukkural.model.SearchResult;
import com.jpp.and_thirukkural.model.Section;
import com.jpp.and_thirukkural.provider.ThirukkuralContentProvider;

import java.util.ArrayList;

/**
 * Created by jperiapandi on 05-07-2016.
 */
public class DataLoadHelper {


    private static DataLoadHelper instance;

    private static Context context;
    private final ContentResolver cr;

    private DataLoadHelper(Context context){
        this.context = context;
        this.cr = context.getContentResolver();
    }

    public static void setContext(Context c){
        if(DataLoadHelper.context == null){
            DataLoadHelper.context = c;
        }
    }

    public static DataLoadHelper getInstance() {
        if(instance == null)
        {
            instance = new DataLoadHelper(DataLoadHelper.context);
        }

        return instance;
    }

    public Couplet getCoupletById(int coupletID, boolean includeExplanation){
        Couplet couplet = null;

        String[] mProjection = CoupletsTable.BASIC_COLUMNS;
        if(includeExplanation)
        {
            mProjection = CoupletsTable.ALL_COLUMNS;
        }

        Cursor cursor = cr.query(ContentUris.withAppendedId(ThirukkuralContentProvider.COUPLETS_URI, coupletID), mProjection, null, null, null);

        if(cursor == null){
            //Error while retrieving data

        }else if(cursor.getCount()==0){
            //No data found

        }else{
            cursor.moveToFirst();
            couplet = getCoupletFromCursor(cursor, includeExplanation);
            cursor.close();
        }

        return couplet;
    }

    public ArrayList<Couplet> getCoupletsByChapter(int chapterID, boolean includeExplanation){
        ArrayList<Couplet> result = null;

        ContentResolver cr = context.getContentResolver();
        //Load data from couplet table
        ArrayList<String> coupletIds = new ArrayList<String>();
        String[] mSelectionArgs = new String[10];
        int i =0;
        int ei = chapterID * 10;
        for(int si = ((chapterID-1)*10)+1; si<=ei; si++){
            coupletIds.add(si+"");
            mSelectionArgs[i] = si+"";
            i++;
        };
        String idsToMatch = "(" + TextUtils.join(",", coupletIds) + ")";


        String[] mProjection = CoupletsTable.BASIC_COLUMNS;
        if(includeExplanation)
        {
            mProjection = CoupletsTable.ALL_COLUMNS;
        }

        String mSelectionClause = CoupletsTable.COL_ID+" in (?,?,?,?,?,?,?,?,?,?)";
        Cursor cursor = cr.query(ThirukkuralContentProvider.COUPLETS_URI, mProjection, mSelectionClause, mSelectionArgs, null);

        if(cursor == null){
            //Error while retrieving data

        }else if(cursor.getCount()==0){
            //No data found

        }else{
            cursor.moveToFirst();
            result = new ArrayList<Couplet>();
            do{
                Couplet couplet = getCoupletFromCursor(cursor, includeExplanation);
                result.add(couplet);
            }while(cursor.moveToNext());

            cursor.close();
        }

        return result;
    }

    public ArrayList<Couplet> getAllCouplets(boolean includeExplanation, String mSelectionClause, String[] mSelectionArgs){
        ArrayList<Couplet> result =null;
        ContentResolver cr = context.getContentResolver();
        //Load data from couplets table

        String[] mProjection = CoupletsTable.BASIC_COLUMNS;
        if(includeExplanation)
        {
            mProjection = CoupletsTable.ALL_COLUMNS;
        }

        String sortOrder = null;

        Cursor cursor = cr.query(ThirukkuralContentProvider.COUPLETS_URI, mProjection, mSelectionClause, mSelectionArgs, sortOrder);

        if(cursor == null){
            //Error while retrieving data

        }else if(cursor.getCount()==0){
            //No data found

        }else{
            cursor.moveToFirst();
            result = new ArrayList<Couplet>();
            do{
                Couplet couplet =getCoupletFromCursor(cursor, includeExplanation);
                result.add(couplet);
            }while(cursor.moveToNext());

            cursor.close();
        }

        return result;
    }

    public ArrayList<Couplet> getAllCouplets(boolean includeExplanation){ return getAllCouplets(includeExplanation, null, null);};

    private Couplet getCoupletFromCursor(Cursor cursor, boolean includeExplanation){
        Couplet couplet = new Couplet();
        couplet.set_id(cursor.getInt(cursor.getColumnIndex(CoupletsTable.COL_ID)));
        couplet.setFav(cursor.getInt(cursor.getColumnIndex(CoupletsTable.COL_FAV)));
        couplet.setCouplet(cursor.getString(cursor.getColumnIndex(CoupletsTable.COL_COUPLET)));

        if(includeExplanation){
            couplet.setCouplet_en(cursor.getString(cursor.getColumnIndex(CoupletsTable.COL_COUPLET_EN)));
            couplet.setExpln_en(cursor.getString(cursor.getColumnIndex(CoupletsTable.COL_EXPLN_EN)));
            couplet.setExpln_muva(cursor.getString(cursor.getColumnIndex(CoupletsTable.COL_EXPLN_MUVA)));
            couplet.setExpln_pappaiah(cursor.getString(cursor.getColumnIndex(CoupletsTable.COL_EXPLN_PAPPAIAH)));
            couplet.setExpln_manakudavar(cursor.getString(cursor.getColumnIndex(CoupletsTable.COL_EXPLN_MANAKUDAVAR)));
            couplet.setExpln_karunanidhi(cursor.getString(cursor.getColumnIndex(CoupletsTable.COL_EXPLN_KARUNANIDHI)));
        }
        return couplet;
    }

    public ArrayList<Section> getAllSections(String mSelectionClause, String[] mSelectionArgs){
        ArrayList<Section> result =null;
        ContentResolver cr = context.getContentResolver();
        //Load data from sections table

        String[] mProjection = {SectionsTable.COL_ID, SectionsTable.COL_TTILE};
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

    public ArrayList<Section> getAllSections(){return getAllSections(null, null);}

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

    public ArrayList<Chapter> getAllChapters(String mSelectionClause, String[] mSelectionArgs){
        ArrayList<Chapter> result =null;
        ContentResolver cr = context.getContentResolver();
        //Load data from chapters table

        String[] mProjection = {ChaptersTable.COL_ID, ChaptersTable.COL_PART_ID, ChaptersTable.COL_SECTION_ID, ChaptersTable.COL_TITLE};
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
    public ArrayList<Chapter> getAllChapters(){return getAllChapters(null, null);}

    public Chapter getChapterById(int chapterID){
        Chapter result = null;

        String[] mProjection = {ChaptersTable.COL_ID, ChaptersTable.COL_TITLE, ChaptersTable.COL_SECTION_ID, ChaptersTable.COL_PART_ID};
        Cursor cursor = cr.query(ContentUris.withAppendedId(ThirukkuralContentProvider.CHAPTERS_URI, chapterID), mProjection, null, null, null);
        if(cursor == null){
            //Error while retrieving data

        }else if(cursor.getCount()==0){
            //No data found

        }else{
            cursor.moveToFirst();
            result = new Chapter();
            result.set_id(cursor.getInt(cursor.getColumnIndex(ChaptersTable.COL_ID)));
            result.setTitle(cursor.getString(cursor.getColumnIndex(ChaptersTable.COL_TITLE)));
            result.setPartId(cursor.getInt(cursor.getColumnIndex(ChaptersTable.COL_PART_ID)));
            result.setSectionId(cursor.getInt(cursor.getColumnIndex(ChaptersTable.COL_SECTION_ID)));

        }
        cursor.close();

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

    public ArrayList<Chapter> getChaptersByPartId(int partID){
        ArrayList<Chapter> result =null;
        ContentResolver cr = context.getContentResolver();
        //Load data from chapters table

        String[] mProjection = {ChaptersTable.COL_ID, ChaptersTable.COL_PART_ID, ChaptersTable.COL_SECTION_ID, ChaptersTable.COL_TITLE};
        String mSelectionClause = ChaptersTable.COL_PART_ID+"=?";
        String[] mSelectionArgs = { partID+"" };
        String sortOrder = ChaptersTable.COL_ID;

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

    public ArrayList<Part> getAllParts(String mSelectionClause, String[] mSelectionArgs){
        ArrayList<Part> result =null;
        ContentResolver cr = context.getContentResolver();
        //Load data from sections table

        String[] mProjection = {PartsTable.COL_ID, PartsTable.COL_TITLE, PartsTable.COL_SECTION_ID};
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

    public ArrayList<Part> getAllParts(){ return getAllParts(null, null);}

    public ArrayList<Part> getPartsBySectionId(int sectionID){
        ArrayList<Part> result =null;
        ContentResolver cr = context.getContentResolver();
        //Load data from sections table

        String[] mProjection = {PartsTable.COL_ID, PartsTable.COL_TITLE, PartsTable.COL_SECTION_ID};
        String mSelectionClause = PartsTable.COL_SECTION_ID+"=?";
        String[] mSelectionArgs = {sectionID+""};
        String sortOrder = PartsTable.COL_ID;

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

    public SearchResult search(String q){

        Log.i("Search ", q);
        int qID = -1;
        try
        {
            qID = Integer.parseInt(q);
        }
        catch(NumberFormatException e){
            qID = -1;
        }

        SearchResult result = new SearchResult();
        result.setQ(q);
        ArrayList<Section> sections = null;
        ArrayList<Part> parts = null;
        ArrayList<Chapter> chapters = null;
        ArrayList<Couplet> couplets = null;

        if(qID != -1){
            //Search by ID

            Section section = getSectionById(qID);
            Part part = getPartById(qID);
            Chapter chapter = getChapterById(qID);
            Couplet couplet = getCoupletById(qID, false);

            if(section!=null){
                sections = new ArrayList<Section>();
                sections.add(section);
            }

            if(part!=null){
                parts = new ArrayList<Part>();
                parts.add(part);
            }

            if(chapter != null){
                chapters = new ArrayList<Chapter>();
                chapters.add(chapter);
            }

            if(couplet != null){
                couplets = new ArrayList<Couplet>();
                couplets.add(couplet);
            }

        }
        else
        {
            //Search by word
            String mSelectionClause1 = "title like ?";
            String mSelectionClause2 = "couplet like ?";
            String[] mSelectionArgs = new String[1];
            mSelectionArgs[0] = "%"+q+"%";

            sections = getAllSections(mSelectionClause1, mSelectionArgs);
            parts = getAllParts( mSelectionClause1, mSelectionArgs);
            chapters = getAllChapters( mSelectionClause1, mSelectionArgs);
            couplets = getAllCouplets(false, mSelectionClause2, mSelectionArgs);
        }

        result.setSections(sections);
        result.setParts(parts);
        result.setChapters(chapters);
        result.setCouplets(couplets);

        return result;
    }


    public ArrayList<SearchHistory> getAllSearchHistory(){
        ArrayList<SearchHistory> result =null;
        ContentResolver cr = context.getContentResolver();
        //Load data from search history table

        String[] mProjection = SearchHistoryTable.ALL_COLUMNS;
        String mSelectionClause = null;
        String[] mSelectionArgs = null;
        String sortOrder = SearchHistoryTable.COL_ID;
        Cursor cursor = cr.query(ThirukkuralContentProvider.SEARCH_HISTORY_URI, mProjection, mSelectionClause, mSelectionArgs, sortOrder);
        result = getSearchHistoryFromCursor(cursor);

        return result;
    }

    public ArrayList<SearchHistory> getActiveSearchHistory(){
        ArrayList<SearchHistory> result =null;
        ContentResolver cr = context.getContentResolver();
        //Load data from search history table

        String[] mProjection = SearchHistoryTable.ALL_COLUMNS;
        String mSelectionClause = SearchHistoryTable.COL_SOFT_DELETE+"=?";
        String[] mSelectionArgs = {"0"};
        String sortOrder = SearchHistoryTable.COL_ID+" DESC";
        Cursor cursor = cr.query(ThirukkuralContentProvider.SEARCH_HISTORY_URI, mProjection, mSelectionClause, mSelectionArgs, sortOrder);
        result = getSearchHistoryFromCursor(cursor);
        return result;
    }

    private ArrayList<SearchHistory> getSearchHistoryFromCursor(Cursor cursor){
        ArrayList<SearchHistory> result =null;
        if (cursor != null ) {
            if  (cursor.moveToFirst()) {
                result = new ArrayList<SearchHistory>();
                do {
                    SearchHistory searchHistory = new SearchHistory();
                    searchHistory.set_id(cursor.getInt(cursor.getColumnIndex(SearchHistoryTable.COL_ID)));
                    searchHistory.setQuery(cursor.getString(cursor.getColumnIndex(SearchHistoryTable.COL_QUERY)));
                    searchHistory.setSoftDelete(cursor.getInt(cursor.getColumnIndex(SearchHistoryTable.COL_SOFT_DELETE)));
                    result.add(searchHistory);
                }while (cursor.moveToNext());
            }
        }
        cursor.close();

        return result;
    }


    public void insertSearchHistory(String query){
        //Remove existing search histories from db
        cr.delete(ThirukkuralContentProvider.SEARCH_HISTORY_URI, SearchHistoryTable.COL_QUERY+" = ?", new String[]{query});

        //insert the query string into search history table
        ContentValues values = new ContentValues();
        values.put(SearchHistoryTable.COL_QUERY, query);
        values.put(SearchHistoryTable.COL_SOFT_DELETE, false);
        cr.insert(ThirukkuralContentProvider.SEARCH_HISTORY_URI, values);
    }

    public void markFavoriteCouplet(int coupletID){
        String where = CoupletsTable.COL_ID+"=?";
        String[] selectionArgs = new String[]{coupletID+""};

        ContentValues values = new ContentValues();
        values.put(CoupletsTable.COL_FAV, 1);
        cr.update(ThirukkuralContentProvider.COUPLETS_URI, values, where, selectionArgs);
    }

    public void unmarkFavoriteCouplet(int coupletID){
        String where = CoupletsTable.COL_ID+"=?";
        String[] selectionArgs = new String[]{coupletID+""};

        ContentValues values = new ContentValues();
        values.put(CoupletsTable.COL_FAV, 0);
        cr.update(ThirukkuralContentProvider.COUPLETS_URI, values, where, selectionArgs);
    }
}
