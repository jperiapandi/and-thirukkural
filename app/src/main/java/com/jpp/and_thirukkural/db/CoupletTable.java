package com.jpp.and_thirukkural.db;

import android.database.sqlite.SQLiteDatabase;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by jperiapandi on 04-07-2016.
 */

public class CoupletTable {
    public static final String TBL_NAME = "couplets";
    public static final String COL_ID = "_id";
    public static final String COL_CHAPTER_ID = "chapter_id";
    public static final String COL_COUPLET = "couplet";

    public static void checkColumns(String[] projection) {
        String[] available = { CoupletTable.COL_ID,
                CoupletTable.COL_CHAPTER_ID, CoupletTable.COL_COUPLET
        };
        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(
                    Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<String>(
                    Arrays.asList(available));
            // check if all columns which are requested are available
            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException(
                        "Unknown columns in projection");
            }
        }
    }
}
