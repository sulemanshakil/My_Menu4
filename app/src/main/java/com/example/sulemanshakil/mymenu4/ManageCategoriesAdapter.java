package com.example.sulemanshakil.mymenu4;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ManageCategoriesAdapter extends BaseAdapter {
    List<String> values;
    public LayoutInflater inflater;
    public Activity activity;

    public ManageCategoriesAdapter(Activity act,List<String> values) {
        this.values=values;
        inflater = act.getLayoutInflater();

    }

    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.managecategories_row, null);
        }
        TextView textViewCat=(TextView)convertView.findViewById(R.id.textViewCat);
        TextView textViewPlus=(TextView)convertView.findViewById(R.id.textViewPlus);

        switch (values.get(position)) {
            case "EXPENSES":
                textViewCat.setText(values.get(position));
                textViewPlus.setText("+");
                textViewCat.setTypeface(null, Typeface.BOLD_ITALIC);
                break;

            case "INCOMES":
                textViewCat.setText(values.get(position));
                textViewPlus.setText("+");
                textViewCat.setTypeface(null, Typeface.BOLD_ITALIC);
                break;

            default:
                textViewCat.setText(values.get(position));
                textViewCat.setTypeface(null, Typeface.NORMAL);
                textViewPlus.setText("");
                convertView.setBackgroundColor(android.R.color.transparent);
                break;
        }

        return convertView;
    }
}
