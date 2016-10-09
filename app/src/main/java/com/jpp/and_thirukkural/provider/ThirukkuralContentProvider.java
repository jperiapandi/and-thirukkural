package com.jpp.and_thirukkural.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.jpp.and_thirukkural.db.ChaptersTable;
import com.jpp.and_thirukkural.db.CoupletsTable;
import com.jpp.and_thirukkural.db.DBHelper;
import com.jpp.and_thirukkural.db.PartsTable;
import com.jpp.and_thirukkural.db.SearchHistoryTable;
import com.jpp.and_thirukkural.db.SectionsTable;
import com.jpp.and_thirukkural.db.URITypes;

import java.io.IOException;

public class ThirukkuralContentProvider extends ContentProvider {



    public static String AUTHORITY = "com.jpp.and_thirukkural";
    public static final Uri COUPLETS_URI = Uri.parse("content://"+ThirukkuralContentProvider.AUTHORITY+"/"+ CoupletsTable.TBL_NAME);
    public static final Uri SECTIONS_URI = Uri.parse("content://"+ThirukkuralContentProvider.AUTHORITY+"/"+ SectionsTable.TBL_NAME);
    public static final Uri PARTS_URI = Uri.parse("content://"+ThirukkuralContentProvider.AUTHORITY+"/"+ PartsTable.TBL_NAME);
    public static final Uri CHAPTERS_URI = Uri.parse("content://"+ThirukkuralContentProvider.AUTHORITY+"/"+ ChaptersTable.TBL_NAME);
    public static final Uri SEARCH_HISTORY_URI = Uri.parse("content://"+ThirukkuralContentProvider.AUTHORITY+"/"+ SearchHistoryTable.TBL_NAME);

    private DBHelper dbHelper;
    public static UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        //Get all couplets
        sURIMatcher.addURI(ThirukkuralContentProvider.AUTHORITY, CoupletsTable.TBL_NAME, URITypes.COUPLETS);
        //Get couplets based on couplet id
        sURIMatcher.addURI(ThirukkuralContentProvider.AUTHORITY, CoupletsTable.TBL_NAME+"/#", URITypes.COUPLET_BY_ID);

        //Get all sections
        sURIMatcher.addURI(ThirukkuralContentProvider.AUTHORITY, SectionsTable.TBL_NAME, URITypes.SECTIONS);
        //Get sections based on section id
        sURIMatcher.addURI(ThirukkuralContentProvider.AUTHORITY, SectionsTable.TBL_NAME+"/#", URITypes.SECTION_BY_ID);

        //Get all parts
        sURIMatcher.addURI(ThirukkuralContentProvider.AUTHORITY, PartsTable.TBL_NAME, URITypes.PARTS);
        //Get part based on part id
        sURIMatcher.addURI(ThirukkuralContentProvider.AUTHORITY, PartsTable.TBL_NAME+"/#", URITypes.PART_BY_ID);

        //Get all chapters
        sURIMatcher.addURI(ThirukkuralContentProvider.AUTHORITY, ChaptersTable.TBL_NAME, URITypes.CHAPTERS);
        //Get chapter based on id
        sURIMatcher.addURI(ThirukkuralContentProvider.AUTHORITY, ChaptersTable.TBL_NAME+"/#", URITypes.CHAPTER_BY_ID);

        //Get all search history
        sURIMatcher.addURI(ThirukkuralContentProvider.AUTHORITY, SearchHistoryTable.TBL_NAME, URITypes.SEARCH_HISTORY);
        //Get all (non deleted) search history
    }

    public ThirukkuralContentProvider() {

    }

    public DBHelper getDbHelper(){
        return this.dbHelper;
    }
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {


        int uriType = sURIMatcher.match(uri);
        switch (uriType){
            case URITypes.SEARCH_HISTORY:
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                long rowId = db.insert(SearchHistoryTable.TBL_NAME, null, values);
                if(rowId != -1){
                    Uri _uri = ContentUris.withAppendedId(uri, rowId);
                    getContext().getContentResolver().notifyChange(_uri, null);
                    return _uri;
                }
                break;

        }

        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean onCreate() {
        this.dbHelper = new DBHelper(this.getContext());
        try {
            dbHelper.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        int uriType = sURIMatcher.match(uri);

        switch (uriType){
            case URITypes.COUPLETS:
            case URITypes.COUPLET_BY_ID:
                return queryCouplets(uri, projection, selection, selectionArgs, sortOrder);
            case URITypes.SECTIONS:
            case URITypes.SECTION_BY_ID:
                return querySections(uri, projection, selection, selectionArgs, sortOrder);
            case URITypes.PARTS:
            case URITypes.PART_BY_ID:
                return queryParts(uri, projection, selection, selectionArgs, sortOrder);
            case URITypes.CHAPTERS:
            case URITypes.CHAPTER_BY_ID:
                return queryChapters(uri, projection, selection, selectionArgs, sortOrder);
            case URITypes.SEARCH_HISTORY :
                return querySearchHistory(uri, projection, selection, selectionArgs, sortOrder);
        }

        return null;
    }


    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    //Private methods
    private Cursor queryCouplets(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder){
        try{
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
            queryBuilder.setTables(CoupletsTable.TBL_NAME);
            CoupletsTable.checkColumns(projection);
            int uriType = sURIMatcher.match(uri);
            switch (uriType){
                case URITypes.COUPLET_BY_ID :
                    queryBuilder.appendWhere(CoupletsTable.COL_ID+" = "+uri.getLastPathSegment());
                    break;
            }

            Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
            return cursor;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    private Cursor querySections(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        try{
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
            queryBuilder.setTables(SectionsTable.TBL_NAME);
            SectionsTable.checkColumns(projection);
            int uriType = sURIMatcher.match(uri);
            switch (uriType){
                case URITypes.SECTION_BY_ID :
                    queryBuilder.appendWhere(SectionsTable.COL_ID+" = "+uri.getLastPathSegment());
                    break;
            }

            Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
            return cursor;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    private Cursor queryParts(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        try{
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
            queryBuilder.setTables(PartsTable.TBL_NAME);
            PartsTable.checkColumns(projection);
            int uriType = sURIMatcher.match(uri);
            switch (uriType){
                case URITypes.PART_BY_ID :
                    queryBuilder.appendWhere(PartsTable.COL_ID+" = "+uri.getLastPathSegment());
                    break;
            }

            Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
            return cursor;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }
    private Cursor queryChapters(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        try{
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
            queryBuilder.setTables(ChaptersTable.TBL_NAME);
            ChaptersTable.checkColumns(projection);
            int uriType = sURIMatcher.match(uri);
            switch (uriType){
                case URITypes.CHAPTER_BY_ID :
                    queryBuilder.appendWhere(ChaptersTable.COL_ID+" = "+uri.getLastPathSegment());
                    break;
            }

            Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
            return cursor;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    private Cursor querySearchHistory(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        try{
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
            queryBuilder.setTables(SearchHistoryTable.TBL_NAME);
            SearchHistoryTable.checkColumns(projection);
            Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
            return cursor;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
