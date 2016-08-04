package com.example.owner.edp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Krishna N. Deoram on 2016-03-18.
 * Gumi is love. Gumi is life.
 */
public class AdapterMarksList extends BaseAdapter {

    private ArrayList<Marks> marks;
    private final LayoutInflater mLayoutInflater;
    private final String TAG = "User List Adapter";
    public static boolean notStudent;

    public AdapterMarksList(Context context) {
        super();
        marks = new ArrayList<>();
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void replaceWith(Collection<Marks> newMarks) {

        this.marks.clear();
        this.marks.addAll(newMarks);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return marks.size();
    }

    @Override
    public Marks getItem(int position) {
        return marks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.list_item_marks, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }

        else
            viewHolder = (ViewHolder)convertView.getTag();

        Marks u = marks.get(position);
        viewHolder.course.setText(u.getCourse());
        viewHolder.section.setText("Section 0" + u.getSection());
        viewHolder.grade.setText("" + u.getMark());
        //viewHolder.updated.setText("Updated by " + u.getUpdatedBy() + " on " + u.getUpdatedLast());
        viewHolder.firstName.setText(u.getFirstName());
        viewHolder.lastName.setText(u.getLastName());

        if (notStudent) {
            viewHolder.firstName.setVisibility(View.VISIBLE);
            viewHolder.lastName.setVisibility(View.VISIBLE);
        } else {
            viewHolder.firstName.setVisibility(View.GONE);
            viewHolder.lastName.setVisibility(View.GONE);
        }

        return convertView;
    }

    public static class ViewHolder {

        public TextView course, section, grade, firstName, lastName;// updated, ;

        public ViewHolder(View convertView) {

            course = (TextView)convertView.findViewById(R.id.textView);
            section = (TextView)convertView.findViewById(R.id.textView2);
            //updated = (TextView)convertView.findViewById(R.id.textView4);
            grade = (TextView)convertView.findViewById(R.id.textView3);
            firstName = (TextView)convertView.findViewById(R.id.textView5);
            lastName = (TextView)convertView.findViewById(R.id.textView6);
        }
    }
}
