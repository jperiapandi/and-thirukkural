package com.jpp.and.thirukkural.db;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by jperiapandi on 04-07-2016.
 */

public class ChaptersTable {

    public static final String TBL_NAME = "chapters";

    public static final String COL_ID = "_id";
    public static final String COL_SECTION_ID = "section_id";
    public static final String COL_PART_ID = "part_id";
    public static final String COL_TITLE = "title";

    public static final String[] ALL_COLUMNS =  {
      ChaptersTable.COL_ID, ChaptersTable.COL_SECTION_ID, ChaptersTable.COL_PART_ID, ChaptersTable.COL_TITLE
    };

    public static void checkColumns(String[] projection) {

        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(
                    Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<String>(
                    Arrays.asList(ChaptersTable.ALL_COLUMNS));
            // check if all columns which are requested are available
            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException(
                        "Unknown columns in projection");
            }
        }
    }


}
