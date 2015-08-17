package com.example.sulemanshakil.mymenu4;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
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

import com.example.sulemanshakil.mymenu4.Data.DBContract.FeedEntry;
import com.example.sulemanshakil.mymenu4.Data.DBHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by Suleman Shakil on 27.07.2015.
 */
public class CalculatorActivity extends ActionBarActivity {
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
    String Type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculator_activity);
        editTextNote = (EditText) findViewById(R.id.editText);
        textViewDateSpace=(TextView)findViewById(R.id.textViewDateSpace);
        HashMap<String, String> data = (HashMap<String, String>) getIntent().getSerializableExtra("message");
        Type = (String )getIntent().getExtras().get("Type");
        mDbHelper = new DBHelper(this);

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        String dateNow=dateConverterToString(mYear, mMonth, mDay);
        String dataString= convertToWeek(dateNow);
        textViewDateSpace.setText(dataString);
        GridViewAdapter gridViewAdapter;
        gridView = (GridView)findViewById(R.id.gridView);
        gridViewAdapter = new GridViewAdapter(this, data);

        gridView.setAdapter(gridViewAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView textView =(TextView) view.findViewById(R.id.textViewGridItem);
                final float amount = Float.parseFloat(scr.getText().toString());
                final String category= textView.getText().toString();
                String description= editTextNote.getText().toString();
                String date=dateConverterToString(mYear, mMonth, mDay);

                SQLiteDatabase db = mDbHelper.getWritableDatabase();

                // Create a new map of values, where column names are the keys
                ContentValues values = new ContentValues();
                values.put(FeedEntry.COLUMN_DATE, date);
                values.put(FeedEntry.COLUMN_ACCOUNT_TYPE, "Cash");
                values.put(FeedEntry.COLUMN_CATEGORY,category );
                values.put(FeedEntry.COLUMN_AMOUNT, amount);
                values.put(FeedEntry.COLUMN_TYPE,Type);
                values.put(FeedEntry.COLUMN_DESCRIPTION, description);
                Log.i("message", date);

                // Insert the new row, returning the primary key value of the new row
                final long newRowId;
                newRowId = db.insert(
                        FeedEntry.TABLE_NAME,
                        null,
                        values);

                CalculatorActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(CalculatorActivity.this, category + ": " + amount + " added " + newRowId, Toast.LENGTH_SHORT).show();
                    }
                });;

                TabbedFragment.refresh();
                CalculatorActivity.this.finish();

            }

        });

        gridView.setVisibility(View.INVISIBLE);

        scr = (TextView) findViewById(R.id.textViewCategory);
        scr.setText("0");
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
                    Toast.makeText(CalculatorActivity.this,"Enter some amount",Toast.LENGTH_SHORT).show();
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
        getMenuInflater().inflate(R.menu.cal_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_datePicker:
                // Single menu item is selected do something
                // Ex: launching new activity/screen or show alert message
                Toast.makeText(CalculatorActivity.this, "Date picker is Selected", Toast.LENGTH_SHORT).show();
                showDialog(DATE_DIALOG_ID);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
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
}
