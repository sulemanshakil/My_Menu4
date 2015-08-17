package com.example.sulemanshakil.mymenu4.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Suleman Shakil on 04.08.2015.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Money.db";



    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String TEXT_TYPE = " TEXT";
        final String REAL_TYPE = " REAL";
        final String COMMA_SEP = ",";
         final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + DBContract.FeedEntry.TABLE_NAME + " (" +
                        DBContract.FeedEntry._ID + " INTEGER PRIMARY KEY," +
                        DBContract.FeedEntry.COLUMN_DATE + TEXT_TYPE + COMMA_SEP +
                        DBContract.FeedEntry.COLUMN_ACCOUNT_TYPE + TEXT_TYPE + COMMA_SEP +
                        DBContract.FeedEntry.COLUMN_CATEGORY + TEXT_TYPE + COMMA_SEP +
                        DBContract.FeedEntry.COLUMN_AMOUNT + REAL_TYPE + COMMA_SEP +
                        DBContract.FeedEntry.COLUMN_TYPE + REAL_TYPE + COMMA_SEP +
                        DBContract.FeedEntry.COLUMN_DESCRIPTION + TEXT_TYPE +
        " )";

        db.execSQL(SQL_CREATE_ENTRIES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + DBContract.FeedEntry.TABLE_NAME;

        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
