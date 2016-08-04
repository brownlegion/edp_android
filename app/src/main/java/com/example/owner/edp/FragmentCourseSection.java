package com.example.owner.edp;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Krishna N. Deoram on 2016-04-04.
 * Gumi is love. Gumi is life.
 */
public class FragmentCourseSection extends Fragment implements Strings, View.OnClickListener{

    private Button button, selectCourse, selectSection, selectTA;
    private TextView courseId, sectionId, taId;
    private ArrayList<User> users;
    private ArrayList<CourseSectionStudent> list1, list2;
    private AdapterSectionCourse list;
    private AdapterUserList userAdapter;
    private ListView listView;
    //private String selectedCourse = "", selectedSection = "", selectedTA = "";
    private User selectedTa;
    private CourseSectionStudent selectedCourse, selectedSection;
    private int selectedAdapter = 1;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_course_section, container, false);

        courseId = (TextView)rootView.findViewById(R.id.tv_selected_course);
        courseId.setOnClickListener(this);
        sectionId = (TextView)rootView.findViewById(R.id.tv_selected_section);
        sectionId.setOnClickListener(this);
        taId = (TextView)rootView.findViewById(R.id.tv_selected_ta);
        taId.setOnClickListener(this);
        listView = (ListView)rootView.findViewById(R.id.listView3);
        list = new AdapterSectionCourse(getActivity());
        userAdapter = new AdapterUserList(getActivity());
        progressBar = (ProgressBar)rootView.findViewById(R.id.progressBar_course_section);

        button = (Button)rootView.findViewById(R.id.button_add_new_section);
        button.setOnClickListener(this);
        selectCourse = (Button)rootView.findViewById(R.id.button_show_courses);
        selectCourse.setOnClickListener(this);
        selectSection = (Button)rootView.findViewById(R.id.button_show_sections);
        selectSection.setOnClickListener(this);
        selectTA = (Button)rootView.findViewById(R.id.button_show_tas);
        selectTA.setOnClickListener(this);

        new APICaller(new APICaller.AsyncResponse() {
            @Override
            public void response(String output) {
                list1 = new ArrayList<>();
                organizeCourses(output);
            }
        }).execute(getCoursesCall);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getActivity(), "Will add thing later.", Toast.LENGTH_SHORT).show();
                if (selectedAdapter == 1) {
                    selectedCourse = list1.get(position);
                    courseId.setText(selectedCourse.getCourseCode());
                    //selectedCourse = list1.get(position).getCourseId();
                } else if (selectedAdapter == 2) {
                    selectedSection = list2.get(position);
                    sectionId.setText(selectedSection.getSectionNumber());
                    //selectedSection = String.valueOf(list2.get(position).getSectionId());
                } else if (selectedAdapter == 3) {
                    selectedTa = users.get(position);
                    taId.setText("" +selectedTa.getUsername());
                    //selectedTA = String.valueOf(users.get(position).getUserId());
                }
            }
        });

        return rootView;
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.button_show_courses) {
            Log.i("onClick", "show courses button");
            selectedAdapter = 1;
            if (list1 != null) {
                list.replaceWith(list1);
                list.type = 1;
                listView.setAdapter(list);
            } else {
                new APICaller(new APICaller.AsyncResponse() {
                    @Override
                    public void response(String output) {
                        list1 = new ArrayList<>();
                        organizeCourses(output);
                    }
                }).execute(getCoursesCall);
            }

        } else if (v.getId() == R.id.button_show_sections) {
            Log.i("onClick", "show sections button");
            selectedAdapter = 2;
            if (list2 != null) {
                list.replaceWith(list2);
                list.type = 2;
                listView.setAdapter(list);
            } else {
                new APICaller(new APICaller.AsyncResponse() {
                    @Override
                    public void response(String output) {
                        list2 = new ArrayList<>();
                        organizeSections(output);
                    }
                }).execute(getSectionsCall);
            }

        } else if (v.getId() == R.id.button_show_tas) {
            Log.i("onClick", "show TAs button");
            selectedAdapter = 3;
            if (users != null) {
                listView.setAdapter(userAdapter);
            } else {
                new APICaller(new APICaller.AsyncResponse() {
                    @Override
                    public void response(String output) {
                        users = new ArrayList<>();
                        organizeTAs(output);
                    }
                }).execute(getTAsCall);
            }

        } else if (v.getId() == R.id.button_add_new_section) {
            addNewCourseSection();
            //button.setClickable(false);

        } else if (v.getId() == R.id.tv_selected_course) {

            if (selectedCourse != null)
                Toast.makeText(getActivity(), selectedCourse.getCourseTitle(), Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getActivity(), "Selected a course.", Toast.LENGTH_SHORT).show();

        } else if (v.getId() == R.id.tv_selected_section) {

            if (selectedSection != null)
                Toast.makeText(getActivity(), selectedSection.getSemester() + " " + selectedSection.getYear(), Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getActivity(), "Selected a section.", Toast.LENGTH_SHORT).show();

        } else if (v.getId() == R.id.tv_selected_ta) {

            if (selectedTa != null)
                Toast.makeText(getActivity(), selectedTa.getFirstName() + " " + selectedTa.getLastName(), Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getActivity(), "Selected a TA.", Toast.LENGTH_SHORT).show();
        }
    }

    private void addNewCourseSection() {

        if (selectedCourse != null && selectedSection != null && selectedTa != null) {
            progressBar.setVisibility(View.VISIBLE);
            button.setVisibility(View.GONE);
            new APICaller(new APICaller.AsyncResponse() {
                @Override
                public void response(String output) {
                    if (!output.equals("0") && !output.equals("-1")) {
                        Toast.makeText(getActivity(), "Successfully added " + selectedCourse.getCourseCode() + " Section 0" + selectedSection.getSectionNumber(),
                                Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        button.setVisibility(View.VISIBLE);
                        //button.setClickable(true);
                    } else if (output.equals("0")){
                        Toast.makeText(getActivity(), "Section already exists.", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        button.setVisibility(View.VISIBLE);
                        //button.setClickable(true);
                    }
                }
            }).execute(addCourseSectionCall, "course=" + selectedCourse.getCourseId(), "&section=" + selectedSection.getSectionId(),
                    "&ta=" + selectedTa.getUserId());
        } else {
            //button.setClickable(true);
            Toast.makeText(getActivity(), "Select a value for everything.", Toast.LENGTH_SHORT).show();
        }
        //button.setClickable(true);
    }

    private void organizeCourses(String output) {

        try {
            JSONObject jsonObject;
            JSONArray jsonArray = new JSONArray(output);

            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                this.list1.add(new CourseSectionStudent(jsonObject.getString("code"), jsonObject.getString("id"), jsonObject.getString("title")));
            }

            list.replaceWith(list1);
            list.type = 1;
            listView.setAdapter(list);

        } catch (Exception e) {
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void organizeSections(String output) {

        try {
            JSONObject jsonObject;
            JSONArray jsonArray = new JSONArray(output);

            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                this.list2.add(new CourseSectionStudent(jsonObject.getString("sectionNumber"), jsonObject.getString("id"), jsonObject.getString("semester"),
                        jsonObject.getString("year")));
            }

            list.replaceWith(list2);
            list.type = 2;
            listView.setAdapter(list);

        } catch (Exception e) {
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void organizeTAs(String output) {

        try {
            JSONObject jsonObject;
            JSONArray jsonArray = new JSONArray(output);

            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                this.users.add(new User(jsonObject.getString("id"), jsonObject.getString("username"),
                        "2", jsonObject.getString("firstname"), jsonObject.getString("lastname")));
            }

            userAdapter.replaceWith(users);
            listView.setAdapter(userAdapter);

        } catch (Exception e) {
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
