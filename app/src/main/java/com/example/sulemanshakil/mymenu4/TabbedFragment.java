package com.example.sulemanshakil.mymenu4;


import java.lang.reflect.Array;
import java.security.acl.Group;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.LightingColorFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sulemanshakil.mymenu4.Data.CatDBContract;
import com.example.sulemanshakil.mymenu4.Data.CategoriesDBHelper;
import com.example.sulemanshakil.mymenu4.Data.DBContract;
import com.example.sulemanshakil.mymenu4.Data.DBHelper;
import com.example.sulemanshakil.mymenu4.Model.Category;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

public class TabbedFragment extends Fragment {


    public static final String TAG = TabbedFragment.class.getSimpleName();
    public static SectionsPagerAdapter mSectionsPagerAdapter;
    public static ViewPager mViewPager;
    public static boolean isOpen=false;


    public static TabbedFragment newInstance() {
        return new TabbedFragment();
    }

    public static void refresh(){
        mSectionsPagerAdapter.getDatesList();
        mSectionsPagerAdapter.notifyDataSetChanged();

        int size= mSectionsPagerAdapter.getCount();
        for(int i = 0; i < size; i++) {
            Log.i("message", "refresh called"+size);
            TabbedContentFragment fragment = (TabbedContentFragment) mSectionsPagerAdapter.getRegisteredFragment(i);
            if(fragment!=null) {
                fragment.upDate();
                fragment.setupChart();
            }
        }
      //  mViewPager.setCurrentItem(2);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tabbed, container, false);
        mSectionsPagerAdapter = new SectionsPagerAdapter(
                getChildFragmentManager());

        mViewPager = (ViewPager) v.findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(mSectionsPagerAdapter.getCount());
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (isOpen) {

                    TabbedContentFragment fragment = (TabbedContentFragment) mSectionsPagerAdapter.getRegisteredFragment(position + 1);
                    if (fragment != null) {
                        if (!fragment.drawer.isOpened()) {
                            fragment.drawOpen();
                        }
                    }
                    TabbedContentFragment fragment1 = (TabbedContentFragment) mSectionsPagerAdapter.getRegisteredFragment(position);
                    if (fragment1 != null) {
                        if (!fragment1.drawer.isOpened()) {
                            fragment1.drawOpen();
                        }
                    }
                    TabbedContentFragment fragment2 = (TabbedContentFragment) mSectionsPagerAdapter.getRegisteredFragment(position - 1);
                    if (fragment2 != null) {
                        if (!fragment2.drawer.isOpened()) {
                            fragment2.drawOpen();
                        }
                    }

                }
            }

            @Override
            public void onPageSelected(int position) {
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    return v;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public int count;
        List<String> dateString;
        Date endDate;
        Date startDate;
        String stringStartDate;

        SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            getDatesList();
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }

        public Fragment getRegisteredFragment(int position) {
            return registeredFragments.get(position);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new TabbedContentFragment();
            Bundle args = new Bundle();

            args.putString(TabbedContentFragment.ARG_START_DATE, stringStartDate);
            args.putInt(TabbedContentFragment.ARG_SECTION_NUMBER, position);
            fragment.setArguments(args);

            return fragment;
        }

        @Override
        public int getCount() {
            return count;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);

            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            stringStartDate=sdf1.format(startDate);

            cal.add(Calendar.MONTH, +position);
            Date date = cal.getTime();


            SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy");
            return sdf.format(date);
        }

        public void getDatesList()  {

            DBHelper mDbHelper = new DBHelper(getActivity());
            SQLiteDatabase db = mDbHelper.getReadableDatabase();

            String[] projection = {
                    DBContract.FeedEntry.COLUMN_DATE,
            };

            Cursor c = db.query(
                    DBContract.FeedEntry.TABLE_NAME, projection, null, null, null, null, DBContract.FeedEntry.COLUMN_DATE + " ASC");

            dateString = new ArrayList<String>();

            while (c.moveToNext()) {
                Log.i("read databse Date", c.getString(0));
                dateString.add(c.getString(0));
            }
            if (dateString.size()==0) {
                Log.i("message", dateString.size() + "");
                return;
            }

            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            try {
                startDate = format.parse(dateString.get(0));
                endDate = format.parse(dateString.get(dateString.size()-1));

            } catch (ParseException e) {
                e.printStackTrace();
            }

            Calendar startCalendar = new GregorianCalendar();
            startCalendar.setTime(startDate);
            Calendar endCalendar = new GregorianCalendar();
            endCalendar.setTime(endDate);

            int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
            int diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
            count=diffMonth+1;

        }
    }


    public static class TabbedContentFragment extends Fragment {

        private PieChart mchart;
        private SlidingDrawer drawer;
        private Button handle;
        private Context context;
        private static int count=0;
        public ListView listView;
        private int position;
        private String startDate;
        public static final String ARG_SECTION_NUMBER = "section_number";
        public static final String ARG_START_DATE = "start_date";
        SparseArray<Category> categorySparseArray = new SparseArray<Category>();
        String dateEndOfMonth,dateStartOfMonth;
        MyExpandableListAdapter adapter;

        public TabbedContentFragment() {
        }

        public  ArrayList<String> getCatDB(){
            CategoriesDBHelper mCategoriesDBHelper = new CategoriesDBHelper(getActivity());
            SQLiteDatabase db = mCategoriesDBHelper.getReadableDatabase();
            ArrayList<String> Income = new ArrayList<>();

            String[] projection = {
                    CatDBContract.FeedEntry1._ID,
                    CatDBContract.FeedEntry1.COLUMN_INCOME,
                    CatDBContract.FeedEntry1.COLUMN_EXPENSE,
            };

            Cursor c = db.query(
                    CatDBContract.FeedEntry1.TABLE_NAME, projection, null, null, null, null, null);
            while(c.moveToNext()){
                if(c.getString(1)!=null) {
                    Income.add(c.getString(1));
                }
            }
            db.close();
            return Income;
        }

        public void setupChart(){
            ArrayList<String> labels = new ArrayList<>();
            ArrayList<Entry> entries = new ArrayList<>();
            ArrayList<String> Income ;

            Income=getCatDB();

            for(int i=0;i<categorySparseArray.size();i++){
               Category cat = categorySparseArray.get(i);
                if(!Income.contains(cat.name)) {
                    labels.add(cat.name);
                    entries.add(new Entry(cat.getTotalAmount(), 0));
                }
            }

            PieDataSet dataSet = new PieDataSet(entries,"");
            PieData data = new PieData(labels, dataSet);
            mchart.setData(data);
            dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
            dataSet.setSliceSpace(2);
            dataSet.setSelectionShift(5);
            mchart.setHoleRadius(50);
            float[] balance=calculateBalance();
            if(categorySparseArray.size()==0){
                mchart.setCenterText("No Data Available");
            }else {
                mchart.setCenterText("Balance \n " + Float.toString(balance[0]) + "\n" + Float.toString(balance[1]));
            }
            handle.setText("Balance  " + Float.toString(balance[2]));
            mchart.setRotationEnabled(false);
        }

        public void drawOpen(){
            Log.i("message",  "count"+count++);
            drawer.open();
        }
        public void drawClose(){
            drawer.close();
        }

        public void upDate() {
            fetchData();
            adapter.notifyDataSetChanged();
            Log.i("message", "update called");
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            position=getArguments().getInt(ARG_SECTION_NUMBER);
            startDate=getArguments().getString(ARG_START_DATE);

            dateEndOfMonth=getEndOfMonth(startDate, position);
            dateStartOfMonth= getStartOfMonth(startDate, position);

            fetchData();
            float[] balance=calculateBalance();
            View rootView = inflater.inflate(R.layout.fragment_tabbed_content, container, false);
            context = this.getActivity();

            handle = (Button) rootView.findViewById(R.id.handle);
            handle.getBackground().setColorFilter(new LightingColorFilter(0xFF000000, 0xB9F6CA));

            mchart = (PieChart) rootView.findViewById(R.id.chart);
            setupChart();


            drawer=(SlidingDrawer)rootView.findViewById(R.id.slidingDrawer);

            ExpandableListView listView = (ExpandableListView) rootView.findViewById(R.id.ListView);
            adapter = new MyExpandableListAdapter(getActivity(), categorySparseArray);
            listView.setAdapter(adapter);

            drawer.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {
                @Override
                public void onDrawerOpened() {
                    isOpen = true;

                    if (mViewPager.getCurrentItem() < mSectionsPagerAdapter.getCount() - 1) {
                        TabbedContentFragment fragment = (TabbedContentFragment) mSectionsPagerAdapter.getRegisteredFragment(mViewPager.getCurrentItem() + 1);
                        if (fragment != null) {
                            if (!fragment.drawer.isOpened()) {
                                fragment.drawOpen();
                            }
                        }
                    }
                    if (mViewPager.getCurrentItem() > 0) {
                        TabbedContentFragment fragment1 = (TabbedContentFragment) mSectionsPagerAdapter.getRegisteredFragment(mViewPager.getCurrentItem() - 1);
                        if (fragment1 != null) {
                            if (!fragment1.drawer.isOpened()) {
                                fragment1.drawOpen();
                            }
                        }
                    }
                }
            });

            drawer.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
                @Override
                public void onDrawerClosed() {
                    isOpen = false;
                    if (mViewPager.getCurrentItem() < mSectionsPagerAdapter.getCount() - 1) {
                        TabbedContentFragment fragment = (TabbedContentFragment) mSectionsPagerAdapter.getRegisteredFragment(mViewPager.getCurrentItem() + 1);
                        if (fragment != null) {
                            fragment.drawClose();
                        }
                    }

                    if (mViewPager.getCurrentItem() > 0) {
                        TabbedContentFragment fragment1 = (TabbedContentFragment) mSectionsPagerAdapter.getRegisteredFragment(mViewPager.getCurrentItem() - 1);
                        if (fragment1 != null) {
                            fragment1.drawClose();
                        }
                    }
                }
            });

            return rootView;
        }

        private float[] calculateBalance() {
            float[] nums = new float[3];

            Float Income= Float.valueOf(0);
            Float Expense= Float.valueOf(0);

            for(int i=0;i<categorySparseArray.size();i++){
                if (categorySparseArray.get(i).CategoryType.equals(getResources().getString(R.string.Income))){
                    Income=Income+categorySparseArray.get(i).getTotalAmount();
                }else {
                    Expense=Expense+categorySparseArray.get(i).getTotalAmount();
                }

            }
            nums[0]=Income;
            nums[1]=Expense;
            nums[2]=Income-Expense;
            return nums;
        }


        private void fetchData() {
            DBHelper mDbHelper = new DBHelper(getActivity());
            SQLiteDatabase db = mDbHelper.getReadableDatabase();
            categorySparseArray.clear();

            String[] projection = {
                    DBContract.FeedEntry._ID,
                    DBContract.FeedEntry.COLUMN_DATE,
                    DBContract.FeedEntry.COLUMN_ACCOUNT_TYPE,
                    DBContract.FeedEntry.COLUMN_CATEGORY,
                    DBContract.FeedEntry.COLUMN_AMOUNT,
                    DBContract.FeedEntry.COLUMN_DESCRIPTION,
                    DBContract.FeedEntry.COLUMN_TYPE,
            };

            Cursor c = db.query(
                    DBContract.FeedEntry.TABLE_NAME,                           // The table to query
                    projection,                                     // The columns to return
                    DBContract.FeedEntry.COLUMN_DATE + " BETWEEN ? AND ?",     // The columns for the WHERE clause
                    new String[] { dateStartOfMonth , dateEndOfMonth },              // The values for the WHERE clause
                    null,                                           // don't group the rows
                    null,                                           // don't filter by row groups
                    DBContract.FeedEntry.COLUMN_AMOUNT+" DESC"                                            // The sort order

            );
            Boolean categoryFound = false;
            int i=0;
            List<String> listRecord= new ArrayList<String>();
            while(c.moveToNext()) {
                Log.i("read databse", c.getInt(0) + " " + c.getString(1) + " " + c.getString(2) + " " + c.getString(3) + " " + c.getFloat(4) + " " + c.getString(5) + " " + c.getString(6));
                int id= c.getInt(0);
                String date = c.getString(1);
                String categoryString = c.getString(3);
                Float money =c.getFloat(4);
                String description=c.getString(5);
                String Type=c.getString(6);

                if(!listRecord.contains(categoryString)) {
                    listRecord.add(categoryString);
                    Category category = new Category(categoryString);
                    category.ID.add(id);
                    category.money.add(money);
                    category.date.add(date);
                    category.description.add(description);
                    category.CategoryType=Type;
                    categorySparseArray.append(i, category);
                    i++;
                }
                else {
                    for (int j = 0; j < categorySparseArray.size(); j++) {
                        Category category = categorySparseArray.get(j);
                        if(category!=null) {
                            if (category.name.equals(categoryString)) {
                                category.money.add(money);
                                category.date.add(date);
                                category.description.add(description);
                                category.ID.add(id);
                            }
                        }
                    }
                }

                Log.i("array size", categorySparseArray.size() + " array size");
            }
            db.close();
            sort(categorySparseArray);
        }

        private void sort(SparseArray<Category> categorySparseArray) {}

        private String getEndOfMonth(String startDate, int position) {

            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date sDate=null;
            try {
                sDate = format.parse(startDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar cal = Calendar.getInstance();
            cal.setTime(sDate);
            cal.add(Calendar.MONTH, +position);
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            Date dateEndOfMonth = cal.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.format(dateEndOfMonth);
        }
        private String getStartOfMonth(String startDate, int position) {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date sDate=null;
            try {
                sDate = format.parse(startDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar cal = Calendar.getInstance();
            cal.setTime(sDate);
            cal.add(Calendar.MONTH, +position);
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
            Date dateStartOfMonth = cal.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return  sdf.format(dateStartOfMonth);
        }
    }
}