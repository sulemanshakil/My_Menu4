package com.example.sulemanshakil.mymenu4;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Parcelable;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sulemanshakil.mymenu4.Model.Category;

import java.io.Serializable;


/**
 * Created by Suleman Shakil on 06.08.2015.
 */
public class MyExpandableListAdapter extends BaseExpandableListAdapter {

    private final SparseArray<Category> groups;
    public LayoutInflater inflater;
    public Activity activity;

    public MyExpandableListAdapter(Activity act, SparseArray<Category> groups) {
        activity = act;
        this.groups = groups;
        inflater = act.getLayoutInflater();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final Category category = (Category) getChild(groupPosition, childPosition);

        Float amount= (Float) category.money.get(childPosition);
        final String children = amount.toString();
        TextView textViewCategory = null;
        TextView textViewDate=null;
        TextView textViewDes=null;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listrow_details, null);
        }
        textViewCategory = (TextView) convertView.findViewById(R.id.textViewCategory);
        textViewDate=(TextView) convertView.findViewById(R.id.textViewDate);
        textViewDes=(TextView) convertView.findViewById(R.id.textViewDes);

        textViewCategory.setText(children);
        textViewDate.setText(category.date.get(childPosition));
        textViewDes.setText(category.description.get(childPosition));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, category.CategoryType,
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(activity, CategoryEdit.class);
                intent.putExtra("message", category);
                intent.putExtra("Position", childPosition);
                Log.i("intExtra", "" + childPosition);
                activity.startActivity(intent);
            }
        });

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
            return groups.get(groupPosition).money.size();

    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listrow_group, null);
        }
        Category group = (Category) getGroup(groupPosition);
        String totalAmount =group.getTotalAmount().toString();
        String Type= group.CategoryType;

        TextView textViewHeader=(TextView)convertView.findViewById(R.id.textViewHeader);
        TextView textViewAmount=(TextView)convertView.findViewById(R.id.textViewAmount);

        textViewHeader.setText(group.name);
        textViewAmount.setText(totalAmount);

        if(Type.equals("Expense") ){
            textViewAmount.setTextColor(Color.parseColor("#D32F2F"));
        }else {
            textViewAmount.setTextColor(Color.parseColor("#8BC34A"));
        }

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
