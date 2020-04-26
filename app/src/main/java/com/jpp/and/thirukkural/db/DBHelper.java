package com.jpp.and.thirukkural.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by jperiapandi on 04-07-2016.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "thirukkural";

    private final Context context;
    private final String DB_PATH;
    private SQLiteDatabase myDataBase;

    private static final int DB_VERSION = 6;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        this.context = context;
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     */
    public void createDataBase() throws IOException {

        boolean dbExist = false;
        int version = 0;
        SQLiteDatabase checkDB = null;
        String myPath = DB_PATH + DB_NAME;
        //Read installed DB version
        try {
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        } catch (SQLiteException e) {
            //database does’t exist yet.
            e.printStackTrace();
        }
        if (checkDB != null) {
            dbExist=true;
            version = checkDB.getVersion();
            checkDB.close();
        }

        if (dbExist && version >= DB_VERSION) {
            //do nothing – database already exist
            Log.d("DBHelper", "Data base file already exists");
        } else {
            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            checkDB = this.getReadableDatabase();
            this.close();
            try {
                copyDataBase();
                if(checkDB!=null){
                    checkDB.close();
                }
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }

        //Set the installed DB version
        try {
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        } catch (SQLiteException e) {
            //database does’t exist yet.
            e.printStackTrace();
        }
        if (checkDB != null) {
            checkDB.setVersion(DB_VERSION);
            checkDB.close();
        }
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     */
    private void copyDataBase() throws IOException {

        //Open your local db as an input stream
        InputStream myInput = context.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDataBase() throws SQLException {
        //Open the database
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    }

    @Override
    public synchronized void close() {
        if (myDataBase != null)
            myDataBase.close();

        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
