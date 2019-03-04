package com.jpp.and.thirukkural.db;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by jperiapandi on 04-07-2016.
 */

public class CoupletsTable {
    public static final String TBL_NAME = "couplets";
    public static final String COL_ID = "_id";
    public static final String COL_FAV = "fav";
    public static final String COL_COUPLET = "couplet";

    public static final String COL_COUPLET_EN = "couplet_en";
    public static final String COL_EXPLN_MUVA = "expln_muva";
    public static final String COL_EXPLN_PAPPAIAH = "expln_pappaiah";
    public static final String COL_EXPLN_MANAKUDAVAR = "expln_manakudavar";
    public static final String COL_EXPLN_KARUNANIDHI = "expln_karunanidhi";
    public static final String COL_EXPLN_EN = "expln_en";

    public static final String[] BASIC_COLUMNS = {
            CoupletsTable.COL_ID, CoupletsTable.COL_FAV, CoupletsTable.COL_COUPLET
            };

    public static final String[] ALL_COLUMNS = {
            CoupletsTable.COL_ID, CoupletsTable.COL_FAV, CoupletsTable.COL_COUPLET, CoupletsTable.COL_COUPLET_EN,
            CoupletsTable.COL_EXPLN_MUVA, CoupletsTable.COL_EXPLN_PAPPAIAH, CoupletsTable.COL_EXPLN_MANAKUDAVAR,
            CoupletsTable.COL_EXPLN_KARUNANIDHI, CoupletsTable.COL_EXPLN_EN
            };

    public static void checkColumns(String[] projection) {

        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(
                    Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<String>(
                    Arrays.asList(CoupletsTable.ALL_COLUMNS));
            // check if all columns which are requested are available
            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException(
                        "Unknown columns in projection");
            }
        }
    }


}
