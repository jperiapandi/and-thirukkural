package com.jpp.and_thirukkural.db;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by jperiapandi on 07-08-2016.
 */
public class PartsTable {
    public static final String TBL_NAME = "parts";

    public static final String COL_ID = "_id";
    public static final String COL_TITLE = "title";
    public static final String COL_SECTION_ID = "section_id";

    public static void checkColumns(String[] projection) {
        String[] available = {PartsTable.COL_ID, PartsTable.COL_SECTION_ID,PartsTable.COL_TITLE};

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
