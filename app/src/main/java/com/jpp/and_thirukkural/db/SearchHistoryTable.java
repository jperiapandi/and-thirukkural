package com.jpp.and_thirukkural.db;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by jperiapandi on 06-08-2016.
 */
public class SearchHistoryTable {
    public static final String TBL_NAME = "search_history";

    public static final String COL_ID = "_id";
    public static final String COL_QUERY = "query";
    public static final String COL_SOFT_DELETE = "soft_delete";

    public static final String[] ALL_COLUMNS = {
            SearchHistoryTable.COL_ID, SearchHistoryTable.COL_QUERY, SearchHistoryTable.COL_SOFT_DELETE
    };

    public static void checkColumns(String[] projection) {

        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(
                    Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<String>(
                    Arrays.asList(SearchHistoryTable.ALL_COLUMNS));
            // check if all columns which are requested are available
            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException(
                        "Unknown columns in projection");
            }
        }
    }
}
