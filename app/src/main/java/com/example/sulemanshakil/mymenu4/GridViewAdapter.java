package com.example.sulemanshakil.mymenu4;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Suleman Shakil on 29.07.2015.
 */
public class GridViewAdapter extends BaseAdapter {

    private Context context;
    HashMap<String, String> data;
    ArrayList<String> keys;
    public GridViewAdapter(Context context,HashMap<String, String> data) {
        this.context = context;
        this.data=data;
        keys= new ArrayList<String>();
        keys.clear();
        for ( String key : data.keySet() ) {
            keys.add(key);
        }
    }

    @Override
    public int getCount() {
        return data.size();
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

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

     //   View gridViewChild;
        if(convertView==null) {
            // get layout from mobile.xml
            convertView = inflater.inflate(R.layout.gridviewchild, null);
        }
            // set value into textview
            TextView textView = (TextView) convertView
                    .findViewById(R.id.textViewGridItem);
            textView.setText(keys.get(position));

            // set image based on selected text
            ImageView imageView = (ImageView) convertView
                    .findViewById(R.id.grid_item_image);

            int id = context.getResources().getIdentifier(data.get(keys.get(position)), "drawable", context.getPackageName());
            imageView.setImageResource(id);

        return convertView;
    }
}
