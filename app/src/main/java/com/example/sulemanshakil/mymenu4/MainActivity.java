package com.example.sulemanshakil.mymenu4;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.LightingColorFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuItem;
import android.content.res.Configuration;

import com.example.sulemanshakil.mymenu4.Data.CategoriesDBHelper;
import com.example.sulemanshakil.mymenu4.Data.CatDBContract.FeedEntry1;
import com.example.sulemanshakil.mymenu4.Data.DBContract;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private View mRightDrawerView,mLeftDrawerList;
    ImageButton imageButtonDay,imageButtonWeek,imageButtonMonth,imageButtonYear;
    ImageButton imageButton8,imageButton5,imageButton6,imageButton7;
    Button add,remove;
    ListView listview5,listview6;
    HashMap<String, String> Income;
    HashMap<String, String> Expense;



    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mLeftDrawerList =  findViewById(R.id.left_drawer);
        mRightDrawerView = findViewById(R.id.right_drawer);
        add = (Button) findViewById(R.id.button_add);
        remove = (Button) findViewById(R.id.button_remove);
        add.getBackground().setColorFilter(new LightingColorFilter(0xFF000000, 0xB9F6CA));
        remove.getBackground().setColorFilter(new LightingColorFilter(0xFF000000, 0xFF8A80));

        listview5=(ListView) findViewById(R.id.listview5);
        listview6=(ListView) findViewById(R.id.listview6);
        listview5.setZ(-100);
        listview6.setZ(-99);

        Income = new HashMap<String, String>();
        Expense= new HashMap<String, String>();

        imageButtonDay=(ImageButton) findViewById(R.id.imageButton_day);
        imageButtonWeek=(ImageButton) findViewById(R.id.imageButton_week);
        imageButtonMonth=(ImageButton) findViewById(R.id.imageButton_month);
        imageButtonYear=(ImageButton) findViewById(R.id.imageButton_year);
        imageButton5=(ImageButton) findViewById(R.id.imageButton5);
        imageButton6=(ImageButton) findViewById(R.id.imageButton6);
        imageButton7=(ImageButton) findViewById(R.id.imageButton7);
        imageButton8=(ImageButton) findViewById(R.id.imageButton8);

        setupListView5();
        setupListView6();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        if(savedInstanceState ==null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame, TabbedFragment.newInstance(), TabbedFragment.TAG).commit();
        }

        imageButtonDay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                 /* Perform action on click */
                mDrawerLayout.closeDrawer(mLeftDrawerList);
                Toast.makeText(getApplicationContext(),"Functionality Not Implemented Yet",Toast.LENGTH_LONG).show();
            }
        });

        imageButtonWeek.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                 /* Perform action on click */
                mDrawerLayout.closeDrawer(mLeftDrawerList);
                Toast.makeText(getApplicationContext(),"Functionality Not Implemented Yet",Toast.LENGTH_LONG).show();
            }
        });
        imageButtonMonth.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                 /* Perform action on click */
                mDrawerLayout.closeDrawer(mLeftDrawerList);
                Toast.makeText(getApplicationContext(), "Functionality Not Implemented Yet", Toast.LENGTH_LONG).show();
            }
        });
        imageButtonYear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                 /* Perform action on click */
                mDrawerLayout.closeDrawer(mLeftDrawerList);
                Toast.makeText(getApplicationContext(), "Functionality Not Implemented Yet", Toast.LENGTH_LONG).show();
            }
        });

        imageButton5.setOnClickListener(new View.OnClickListener() {
            int n=0;
            public void onClick(View v) {
                 /* Perform action on click */
                Toast.makeText(MainActivity.this, "click", Toast.LENGTH_SHORT).show();
                if (n==0){
                    imageButton6.animate().translationY(mRightDrawerView.getHeight()).alpha(0).setDuration(500);
                    listview6.animate().translationY(mRightDrawerView.getHeight()).alpha(0).setDuration(500);
                    imageButton7.animate().translationY(mRightDrawerView.getHeight()).alpha(0).setDuration(500);
                    imageButton8.animate().translationY(mRightDrawerView.getHeight()).alpha(0).setDuration(500);
                    n++;
                }
                else{
                    imageButton6.animate().translationY(0).alpha(1).setDuration(500);
                    listview6.animate().translationY(0).alpha(1).setDuration(500);
                    imageButton7.animate().translationY(0).alpha(1).setDuration(500);
                    imageButton8.animate().translationY(0).alpha(1).setDuration(500);
                    n--;
                }
            }
        });

        imageButton6.setOnClickListener(new View.OnClickListener() {
            int n=0;
            public void onClick(View v) {
                 /* Perform action on click */
                Toast.makeText(MainActivity.this, "click", Toast.LENGTH_SHORT).show();
                if (n==0){
                    imageButton6.animate().translationY(-v.getHeight()+35).setDuration(500);
                    listview6.animate().translationY(-v.getHeight()+50).setDuration(500);
                    listview5.animate().translationY(-mRightDrawerView.getHeight()).alpha(0).setDuration(1);
                    imageButton7.animate().translationY(mRightDrawerView.getHeight()).alpha(0).setDuration(500);
                    imageButton8.animate().translationY(mRightDrawerView.getHeight()).alpha(0).setDuration(500);
                    n++;
                }
                else{
                    imageButton6.animate().translationY(0).alpha(1).setDuration(500);
                    listview6.animate().translationY(0).alpha(1).setDuration(500);
                    listview5.animate().translationY(0).alpha(1).setDuration(500);
                    imageButton7.animate().translationY(0).alpha(1).setDuration(500);
                    imageButton8.animate().translationY(0).alpha(1).setDuration(500);
                    n--;
                }

            }
        });
    }

    public void add(View view){
        Intent intent = new Intent(this,CalculatorActivity.class);
        intent.putExtra("message", (Serializable) Income);
        intent.putExtra("Type",getResources().getString(R.string.Income));
        MainActivity.this.startActivity(intent);
    }

    public void remove(View view){
        Intent intent = new Intent(this,CalculatorActivity.class);
        intent.putExtra("message", (Serializable) Expense);
        intent.putExtra("Type",getResources().getString(R.string.Expense) );
        MainActivity.this.startActivity(intent);
    }

    public void getCatDB(){
        CategoriesDBHelper mCategoriesDBHelper = new CategoriesDBHelper(this);
        SQLiteDatabase db = mCategoriesDBHelper.getReadableDatabase();

        String[] projection = {
                FeedEntry1._ID,
                FeedEntry1.COLUMN_INCOME,
                FeedEntry1.COLUMN_EXPENSE,
                FeedEntry1.COLUMN_PIC_ID,
        };

        Cursor c = db.query(
                FeedEntry1.TABLE_NAME,                          // The table to query
                projection,                                     // The columns to return
                null,                                           // The columns for the WHERE clause
                null,                                           // The values for the WHERE clause
                null,                                           // don't group the rows
                null,                                           // don't filter by row groups
                null                                            // The sort order

        );
        Income=  new HashMap<String, String>();
        Expense= new HashMap<String, String>();
        while(c.moveToNext()){
            if(c.getString(1)!=null) {
                Income.put(c.getString(1), c.getString(3));
            }
            if(c.getString(2)!=null) {
                Expense.put(c.getString(2), c.getString(3));

            }
        }
        db.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupListView5();
    }

    private void setupListView5(){
        getCatDB();
        List<String> values= new ArrayList<String>();
        values.clear();
        values.add("EXPENSES");
        for ( String key : Expense.keySet() ) {
            values.add(key);
        }
        values.add("INCOMES");
        for ( String key : Income.keySet() ) {
            values.add(key);
        }

        ManageCategoriesAdapter manageCategoriesAdapter= new ManageCategoriesAdapter(this,values);
        listview5.setAdapter(manageCategoriesAdapter);

        listview5.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textViewCat=(TextView)view.findViewById(R.id.textViewCat);
                // Launch new activity
                Intent intent = new Intent(getApplicationContext(), CategoryAddDel.class);
                String message = textViewCat.getText().toString();
                intent.putExtra("message", message);
                startActivity(intent);
            }
        });
    }

    private void setupListView6(){
        String[] values = new String[] {  "Cash", "Payment Card"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, values);
        listview6.setAdapter(adapter);
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //   getSupportActionBar().setTitle("Navigation!");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                //  getSupportActionBar().setTitle("Navigation!");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            boolean drawerOpen = mDrawerLayout.isDrawerOpen(mRightDrawerView);
           if(drawerOpen == true){
               mDrawerLayout.closeDrawer(mRightDrawerView);
               imageButton5.animate().translationY(0).alpha(1).setDuration(500);
               imageButton6.animate().translationY(0).alpha(1).setDuration(500);
               imageButton7.animate().translationY(0).alpha(1).setDuration(500);
               imageButton8.animate().translationY(0).alpha(1).setDuration(500);
               listview6.animate().translationY(0).alpha(1).setDuration(500);
               listview5.animate().translationY(0).alpha(1).setDuration(500);
           }
            else{
               mDrawerLayout.openDrawer(mRightDrawerView);
           }
            return true;
        }

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}