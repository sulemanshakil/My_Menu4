package com.example.sulemanshakil.mymenu4.Data;

import android.provider.BaseColumns;

/**
 * Created by Suleman Shakil on 04.08.2015.
 */
public class DBContract  {

    public DBContract() {}


    /* Inner class that defines the table contents */
    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "moneytable";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_ACCOUNT_TYPE = "accounttype";
        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_AMOUNT = "amount";
        public static final String COLUMN_DESCRIPTION="description";
        public static final String COLUMN_TYPE="type";
    }
}
