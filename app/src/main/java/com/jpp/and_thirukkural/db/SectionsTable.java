package com.jpp.and_thirukkural.db;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by jperiapandi on 06-08-2016.
 */
public class SectionsTable {
    public static final String TBL_NAME = "sections";

    public static final String COL_ID = "_id";
    public static final String COL_TTILE = "title";

    public static final String[] ALL_COLUMNS = {
            SectionsTable.COL_ID, SectionsTable.COL_TTILE
    };

    public static void checkColumns(String[] projection) {

        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(
                    Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<String>(
                    Arrays.asList(SectionsTable.ALL_COLUMNS));
            // check if all columns which are requested are available
            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException(
                        "Unknown columns in projection");
            }
        }
    }
}
