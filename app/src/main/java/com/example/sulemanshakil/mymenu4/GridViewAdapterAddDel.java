package com.example.sulemanshakil.mymenu4;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Suleman Shakil on 12.08.2015.
 */
public class GridViewAdapterAddDel extends BaseAdapter{

    private Context context;
    String[] data;
    String pictureID;
    public GridViewAdapterAddDel(Context context,String[] data) {
        this.context = context;
        this.data=data;
    }

    @Override
    public int getCount() {
        return data.length;
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

        if(convertView==null) {
            // get layout from mobile.xml
            convertView = inflater.inflate(R.layout.gridviewchild, null);

        }

        // set image based on selected text
        ImageView imageView = (ImageView) convertView
                .findViewById(R.id.grid_item_image);

        int id= context.getResources().getIdentifier(data[position], "drawable", context.getPackageName());
        imageView.setImageResource(id);
        convertView.setTag(data[position]);

        Log.i("sd",pictureID+" : "+ data[position]);

        if(pictureID==data[position]){
        }else {
            convertView.setBackgroundColor(android.R.color.transparent);
        }

        return convertView;
    }

    public void setPictureID(String pictureID) {
        this.pictureID=pictureID;
    }
}

class ViewHolder {
    ImageView img;
    TextView lbl;
}
