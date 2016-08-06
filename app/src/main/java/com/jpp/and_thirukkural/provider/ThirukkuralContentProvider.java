package com.jpp.and_thirukkural.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.jpp.and_thirukkural.db.CoupletTable;
import com.jpp.and_thirukkural.db.DBHelper;
import com.jpp.and_thirukkural.db.SectionsTable;
import com.jpp.and_thirukkural.db.URITypes;

import java.io.IOException;

public class ThirukkuralContentProvider extends ContentProvider {

    public static String AUTHORITY = "com.jpp.and_thirukkural";
    public static final Uri COUPLETS_URI = Uri.parse("content://"+ThirukkuralContentProvider.AUTHORITY+"/"+CoupletTable.TBL_NAME);
    public static final Uri SECTIONS_URI = Uri.parse("content://"+ThirukkuralContentProvider.AUTHORITY+"/"+ SectionsTable.TBL_NAME);

    private DBHelper dbHelper;
    public static UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        //Get multiple couplets
        sURIMatcher.addURI(ThirukkuralContentProvider.AUTHORITY, CoupletTable.TBL_NAME, URITypes.COUPLETS);
        //Get couplet based on couplet id
        sURIMatcher.addURI(ThirukkuralContentProvider.AUTHORITY, CoupletTable.TBL_NAME+"/#", URITypes.COUPLET_BY_ID);

        //Get multiple sections
        sURIMatcher.addURI(ThirukkuralContentProvider.AUTHORITY, SectionsTable.TBL_NAME, URITypes.SECTIONS);
        //Get sections based on section id
        sURIMatcher.addURI(ThirukkuralContentProvider.AUTHORITY, SectionsTable.TBL_NAME+"/#", URITypes.SECTION_BY_ID);
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
        // TODO: Implement this to handle requests to insert a new row.
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
            queryBuilder.setTables(CoupletTable.TBL_NAME);
            CoupletTable.checkColumns(projection);
            int uriType = sURIMatcher.match(uri);
            switch (uriType){
                case URITypes.COUPLET_BY_ID :
                    queryBuilder.appendWhere(CoupletTable.COL_ID+" = "+uri.getLastPathSegment());
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
}
