package com.example.sulemanshakil.mymenu4.Data;

import android.provider.BaseColumns;

/**
 * Created by Suleman Shakil on 10.08.2015.
 */
public class CatDBContract {

    public CatDBContract() {}

    /* Inner class that defines the table contents */
    public static abstract class FeedEntry1 implements BaseColumns {
        public static final String TABLE_NAME = "categoryList";
        public static final String _ID = "ID";
        public static final String COLUMN_INCOME = "Income";
        public static final String COLUMN_EXPENSE = "Expense";
        public static final String COLUMN_PIC_ID= "pictureID";
    }
}
