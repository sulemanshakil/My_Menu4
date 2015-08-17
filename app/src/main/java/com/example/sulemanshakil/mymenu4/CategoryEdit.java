package com.example.sulemanshakil.mymenu4;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sulemanshakil.mymenu4.Data.CatDBContract;
import com.example.sulemanshakil.mymenu4.Data.CategoriesDBHelper;
import com.example.sulemanshakil.mymenu4.Data.DBContract;
import com.example.sulemanshakil.mymenu4.Data.DBHelper;
import com.example.sulemanshakil.mymenu4.Model.Category;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Suleman Shakil on 14.08.2015.
 */
public class CategoryEdit extends ActionBarActivity{
    private TextView scr;
    private ButtonClickListener btnClick;
    String Operation="";
    private TextView textViewDateSpace;
    EditText editTextNote;
    GridView gridView;
    float NumberBf;  // save screen before pressing operation button
    boolean isLastMathClicked=false;
    DBHelper mDbHelper;
    private int mYear;
    private int mMonth;
    private int mDay;
    static final int DATE_DIALOG_ID = 0;
    Category category;
    int childPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculator_activity);
        editTextNote = (EditText) findViewById(R.id.editText);
        textViewDateSpace=(TextView)findViewById(R.id.textViewDateSpace);
        category = (Category) getIntent().getSerializableExtra("message");
        childPosition =  getIntent().getIntExtra("Position",0);

        HashMap<String, String> data = getCatDB();

        mDbHelper = new DBHelper(this);

        final Calendar c = Calendar.getInstance();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = format.parse(category.date.get(childPosition));
            c.setTime(date);
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String dateNow=category.date.get(childPosition);
        String dataString= convertToWeek(dateNow);
        textViewDateSpace.setText(dataString);
        GridViewAdapter gridViewAdapter;
        gridView = (GridView)findViewById(R.id.gridView);
        gridViewAdapter = new GridViewAdapter(this, data);

        gridView.setAdapter(gridViewAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView textView = (TextView) view.findViewById(R.id.textViewGridItem);
                final float amount = Float.parseFloat(scr.getText().toString());
                final String categoryName = textView.getText().toString();
                String description = editTextNote.getText().toString();
                String date = dateConverterToString(mYear, mMonth, mDay);

                SQLiteDatabase db = mDbHelper.getWritableDatabase();

                // Create a new map of values, where column names are the keys
                ContentValues values = new ContentValues();
                values.put(DBContract.FeedEntry.COLUMN_DATE, date);
                values.put(DBContract.FeedEntry.COLUMN_ACCOUNT_TYPE, "Cash");
                values.put(DBContract.FeedEntry.COLUMN_CATEGORY, categoryName);
                values.put(DBContract.FeedEntry.COLUMN_AMOUNT, amount);
                values.put(DBContract.FeedEntry.COLUMN_DESCRIPTION, description);



                String selection = DBContract.FeedEntry._ID + "=?";
                String[] selectionArgs = {category.ID.get(childPosition).toString()};
                db.update(DBContract.FeedEntry.TABLE_NAME, values, selection, selectionArgs);
                db.close();

                TabbedFragment.refresh();
                CategoryEdit.this.finish();

            }

        });

        gridView.setVisibility(View.INVISIBLE);

        scr = (TextView) findViewById(R.id.textViewCategory);
      //  scr.setText("0");
        scr.setText(category.money.get(childPosition).toString());
        btnClick = new ButtonClickListener();
        int idList[]= {R.id.button0,R.id.button1,R.id.button2,R.id.button3,R.id.button4,R.id.button5,R.id.button6,
                R.id.button7,R.id.button8,R.id.button9,R.id.buttonEql,R.id.buttonDot,R.id.buttonDiv,
                R.id.buttonMul,R.id.button_Sub,R.id.buttonPlus,R.id.buttonC};

        for (int id:idList){
            findViewById(id).setOnClickListener(btnClick);
        }

        findViewById(R.id.button_Choose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (scr.getText().toString()=="0"){
                    Toast.makeText(CategoryEdit.this,"Enter some amount",Toast.LENGTH_SHORT).show();
                }else {
                    View view = findViewById(R.id.relative_layout);
                    view.animate().translationY(view.getHeight()).alpha(0).setDuration(500);
                    gridView.setVisibility(View.VISIBLE);
                    editTextNote.setVisibility(View.INVISIBLE);
                    String Note = editTextNote.getText().toString();
                }
            }
        });
    }



    @Override
    public void onBackPressed() {
        if (gridView.isShown()) {
            findViewById(R.id.relative_layout).animate().translationY(0).alpha(1).setDuration(500);
            gridView.setVisibility(View.INVISIBLE);
            editTextNote.setVisibility(View.VISIBLE);
        }
        else {
            super.onBackPressed();
        }
    }

    public void math(String str){
        NumberBf = Float.parseFloat(scr.getText().toString());
        Operation = str;
        isLastMathClicked=true;
    }

    public void getKeyboard(String str){
        if(isLastMathClicked){
            scr.setText("");
            isLastMathClicked=false;
        }
        String currentStr = scr.getText().toString();
        if(currentStr == "0")
            currentStr="";
        currentStr += str;
        scr.setText(currentStr);
    }


    public void mResults(){
        float NumAf = Float.parseFloat(scr.getText().toString());
        float results = 0;
        if(Operation.equals("+")){
            results=NumberBf+NumAf;
        }
        if(Operation.equals("-")){
            results=NumberBf-NumAf;
        }
        if(Operation.equals("*")){
            results=NumberBf*NumAf;
        }
        if(Operation.equals("/")){
            results=NumberBf/NumAf;
        }
        if(Operation!="") {
            scr.setText(String.valueOf(results));
        }
    }

    private class ButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case (R.id.buttonC):
                    scr.setText("0");
                    NumberBf = 0;
                    Operation ="";
                    break;
                case (R.id.buttonPlus):
                    math("+");
                    break;
                case (R.id.button_Sub):
                    math("-");
                    break;
                case (R.id.buttonMul):
                    math("*");
                    break;
                case (R.id.buttonDiv):
                    math("/");
                    break;
                case (R.id.buttonEql):
                    mResults();
                    break;
                default:
                    String number =((Button) v).getText().toString();
                    getKeyboard(number);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cat_edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_datePicker:
                // Single menu item is selected do something
                // Ex: launching new activity/screen or show alert message
             //   Toast.makeText(Category.this, "Date picker is Selected", Toast.LENGTH_SHORT).show();
                showDialog(DATE_DIALOG_ID);
                return true;

            case R.id.action_del:
                del();
                return true;

            case R.id.action_save:
                save();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Del this category from both table
    private void del() {
        DBHelper DBHelper = new DBHelper(this);
        SQLiteDatabase db = DBHelper.getWritableDatabase();

        // Define 'where' part of query.
        String selection = DBContract.FeedEntry._ID + "=?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = {category.ID.get(childPosition).toString()};
        // Issue SQL statement.
        db.delete(DBContract.FeedEntry.TABLE_NAME, selection, selectionArgs);
        db.close();

        TabbedFragment.refresh();
        finish();
    }

    // edit the category in both tables
    private void save() {

        String date = dateConverterToString(mYear, mMonth, mDay);

        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBContract.FeedEntry.COLUMN_AMOUNT, scr.getText().toString());
        values.put(DBContract.FeedEntry.COLUMN_DESCRIPTION, editTextNote.getText().toString());
        values.put(DBContract.FeedEntry.COLUMN_DATE,date );

        String selection = DBContract.FeedEntry._ID + "=?";
        String[] selectionArgs = {category.ID.get(childPosition).toString()};
        db.update(DBContract.FeedEntry.TABLE_NAME, values, selection, selectionArgs);
        db.close();

        TabbedFragment.refresh();
        finish();
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    String dateNow=dateConverterToString(mYear, mMonth, mDay);
                    String dataString= convertToWeek(dateNow);
                    textViewDateSpace.setText(dataString);
                    Log.i("message", "year:" + mYear + "  month:" + mMonth + "  day:" + mDay);
                }
            };

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this,
                        mDateSetListener,
                        mYear, mMonth, mDay);
        }
        return null;
    }

    private String dateConverterToString(int mYear, int mMonth, int mDay) {

        StringBuilder sb = new StringBuilder();
        sb.append(Integer.toString(mYear)).append("-");
        mMonth=mMonth+1;
        if(mMonth<10){
            sb.append("0");
            sb.append(Integer.toString(mMonth)).append("-");
        }else {
            sb.append(Integer.toString(mMonth)).append("-");
        }

        if(mDay<10){
            sb.append("0");
            sb.append(Integer.toString(mDay));
        }else {
            sb.append(Integer.toString(mDay));
        }
        String date =sb.toString();

        return date;
    }

    private String convertToWeek(String date) {

        SimpleDateFormat read = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat write = new SimpleDateFormat("EEEE, d MMMM");
        String str = null;
        try {
            str = write.format(read.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public HashMap<String, String> getCatDB(){
        CategoriesDBHelper mCategoriesDBHelper = new CategoriesDBHelper(this);
        SQLiteDatabase db = mCategoriesDBHelper.getReadableDatabase();

        String[] projection = {
                CatDBContract.FeedEntry1._ID,
                CatDBContract.FeedEntry1.COLUMN_INCOME,
                CatDBContract.FeedEntry1.COLUMN_EXPENSE,
                CatDBContract.FeedEntry1.COLUMN_PIC_ID,
        };

        Cursor c = db.query(
                CatDBContract.FeedEntry1.TABLE_NAME,                          // The table to query
                projection,                                     // The columns to return
                null,                                           // The columns for the WHERE clause
                null,                                           // The values for the WHERE clause
                null,                                           // don't group the rows
                null,                                           // don't filter by row groups
                null                                            // The sort order

        );

        HashMap<String, String> Income  = new HashMap<String, String>();
        HashMap<String, String> Expense = new HashMap<String, String>();

        while(c.moveToNext()){
            if(c.getString(1)!=null) {
                Income.put(c.getString(1), c.getString(3));
            }
            if(c.getString(2)!=null) {
                Expense.put(c.getString(2), c.getString(3));

            }
        }
        db.close();
        String Type = category.CategoryType;
        if(Type.equals(getResources().getString(R.string.Expense))){
            Log.i("Type",Type);
            return Expense;

        }else {
            return Income;
        }
    }
}

