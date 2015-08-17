package com.example.sulemanshakil.mymenu4;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sulemanshakil.mymenu4.Data.CatDBContract.FeedEntry1;
import com.example.sulemanshakil.mymenu4.Data.CategoriesDBHelper;
import com.example.sulemanshakil.mymenu4.Data.DBContract.FeedEntry;
import com.example.sulemanshakil.mymenu4.Data.DBHelper;

/**
 * Created by Suleman Shakil on 11.08.2015.
 */
public class CategoryAddDel extends ActionBarActivity {
    MenuItem item;
    String message;
    EditText editText;
    GridView gridView;
    String pictureID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_add_del);
        message = getIntent().getExtras().getString("message");
        editText = (EditText) findViewById(R.id.editTextAddDel);
        gridView =(GridView)findViewById(R.id.gridView2);
        String data[]= getResources().getStringArray(R.array.Picture_name_array);
        final GridViewAdapterAddDel gridViewAdapterAddDel= new GridViewAdapterAddDel(this,data);
        gridView.setAdapter(gridViewAdapterAddDel);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final int numVisibleChildren = gridView.getChildCount();
                for ( int i = 0; i < numVisibleChildren; i++ ) {
                    View mview = gridView.getChildAt(i);
                    mview.setBackgroundColor(android.R.color.transparent);
                }
                view.setBackgroundColor(R.color.black);
                pictureID=(String)view.getTag();
                gridViewAdapterAddDel.setPictureID(pictureID);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_del_menu, menu);
        item = menu.findItem(R.id.action_del);
        switch (message) {
            case "EXPENSES":
                editText.setText("");
                item.setVisible(false);
                break;
            case "INCOMES":
                editText.setText("");
                item.setVisible(false);
                break;
            default:
                editText.setText(message);
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_save:
                save(message, editText.getText().toString());
                // Single menu item is selected do something
                // Ex: launching new activity/screen or show alert message
                return true;
            case R.id.action_del:
                del(editText.getText().toString());
                // Single menu item is selected do something
                // Ex: launching new activity/screen or show alert message
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
// Del this category from both table
    private void del(String s) {
        CategoriesDBHelper mCategoriesDBHelper = new CategoriesDBHelper(this);
        SQLiteDatabase db = mCategoriesDBHelper.getWritableDatabase();

        // Define 'where' part of query.
        String selection = FeedEntry1.COLUMN_EXPENSE + "=?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = {s};
        // Issue SQL statement.
        db.delete(FeedEntry1.TABLE_NAME, selection, selectionArgs);

        String selection1 = FeedEntry1.COLUMN_INCOME + "=?";
        db.delete(FeedEntry1.TABLE_NAME, selection1, selectionArgs);
        db.close();


        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase dbh = dbHelper.getWritableDatabase();

        String selection2 = FeedEntry.COLUMN_CATEGORY + "=?";
        String[] selectionArgs2 = {s};
        dbh.delete(FeedEntry.TABLE_NAME, selection2, selectionArgs2);
        dbh.close();

        TabbedFragment.refresh();
        finish();
    }

    private void save(String message, String s) {
        switch (message) {
            case "EXPENSES":
                addExpenses(s);
                break;
            case "INCOMES":
                addIncomes(s);
                break;
            default:
                editString(message,s);
                break;
        }
    }

    // add new category to category table
    private void addIncomes(String s) {
        CategoriesDBHelper mCategoriesDBHelper = new CategoriesDBHelper(this);
        SQLiteDatabase db = mCategoriesDBHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(FeedEntry1.COLUMN_INCOME, s);
        values.put(FeedEntry1.COLUMN_PIC_ID,pictureID);
        db.insert(FeedEntry1.TABLE_NAME,null,values);

        db.close();
        finish();
    }
    // add new category to category table
    private void addExpenses(String s) {
        CategoriesDBHelper mCategoriesDBHelper = new CategoriesDBHelper(this);
        SQLiteDatabase db = mCategoriesDBHelper.getWritableDatabase();
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(FeedEntry1.COLUMN_EXPENSE, s);
        values.put(FeedEntry1.COLUMN_PIC_ID,pictureID);
        db.insert(FeedEntry1.TABLE_NAME,null,values);
        db.close();
        finish();
    }

    // edit the category in both tables
    private void editString(String preString,String newString) {

        CategoriesDBHelper mCategoriesDBHelper = new CategoriesDBHelper(this);
        SQLiteDatabase db = mCategoriesDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FeedEntry1.COLUMN_EXPENSE, newString);
        values.put(FeedEntry1.COLUMN_PIC_ID,pictureID);

        // Which row to update, based on the ID
        String selection = FeedEntry1.COLUMN_EXPENSE + "=?";
        String[] selectionArgs = { preString };
        db.update(FeedEntry1.TABLE_NAME, values, selection, selectionArgs);
        // Which row to update, based on the ID

        ContentValues values1 = new ContentValues();
        values1.put(FeedEntry1.COLUMN_INCOME, newString);
        values1.put(FeedEntry1.COLUMN_PIC_ID,pictureID);
        String selection1 = FeedEntry1.COLUMN_INCOME + "=?";
        String[] selectionArgs1 = { preString };
        db.update(FeedEntry1.TABLE_NAME, values1, selection1, selectionArgs1);
        db.close();


        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase dbh = dbHelper.getWritableDatabase();

        ContentValues values2 = new ContentValues();
        values2.put(FeedEntry.COLUMN_CATEGORY, newString);

        String selection2 = FeedEntry.COLUMN_CATEGORY + "=?";
        String[] selectionArgs2 = {preString};
        dbh.update(FeedEntry.TABLE_NAME, values2, selection2, selectionArgs2);
        dbh.close();

        TabbedFragment.refresh();
        finish();
    }


}

