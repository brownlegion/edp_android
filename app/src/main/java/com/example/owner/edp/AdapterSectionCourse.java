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
 * Created by Krishna N. Deoram on 2016-04-16.
 * Gumi is love. Gumi is life.
 */
public class AdapterSectionCourse extends BaseAdapter {

    private ArrayList<CourseSectionStudent> arrayList;
    private final LayoutInflater mLayoutInflater;
    private final String TAG = "Section Course Adapter";
    public static int type;

    public AdapterSectionCourse(Context context) {
        super();
        arrayList = new ArrayList<>();
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void replaceWith(Collection<CourseSectionStudent> newMarks) {

        this.arrayList.clear();
        this.arrayList.addAll(newMarks);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.list_item_courses_sections_students, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }

        else
            viewHolder = (ViewHolder)convertView.getTag();

        CourseSectionStudent single = arrayList.get(position);

        if (type == 1) { //Courses only and their professors.
            viewHolder.bottom.setVisibility(View.GONE);
            viewHolder.top.setText(single.getCourseCode());
            viewHolder.middle.setText(single.getCourseTitle());
        } else if (type == 2) { //Sections only.
            viewHolder.bottom.setVisibility(View.GONE);
            viewHolder.middle.setVisibility(View.GONE);
            viewHolder.top.setText(single.getSemester() + " " + single.getYear() + " Section 0" + single.getSectionNumber());
        } else if (type == 3) { //Course, section, Ta only.
            viewHolder.bottom.setVisibility(View.GONE);
            viewHolder.top.setText(single.getCourseCode() + " Section 0" + single.getSectionNumber());
            viewHolder.middle.setText("TA: " + single.getTaUsername());
        } else if (type == 4) { //Student and course section they are in.
            viewHolder.top.setText(single.getFirstname() + " " + single.getLastname());
            viewHolder.middle.setText(single.getCourseCode());
            viewHolder.bottom.setText("Section 0" + single.getSectionNumber());
        }

        return convertView;
    }

    public static class ViewHolder {

        private TextView top, middle, bottom;

        public ViewHolder(View convertView) {

            top = (TextView)convertView.findViewById(R.id.top_element);
            middle = (TextView)convertView.findViewById(R.id.middle_element);
            bottom = (TextView)convertView.findViewById(R.id.bottom_element);
        }
    }
}
