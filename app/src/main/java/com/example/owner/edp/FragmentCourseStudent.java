package com.example.owner.edp;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class FragmentCourseStudent extends Fragment implements Strings, View.OnClickListener{

    private Button button, showStudents, showSectionCourses;
    private TextView studentId, sectionId;
    private ListView listView;
    private User selectedStudeng;
    private CourseSectionStudent selectedCourseSection;
    private ArrayList<User> users;
    private ArrayList<CourseSectionStudent> listOfCourseSections;
    private AdapterSectionCourse courseSectionAdapter;
    private AdapterUserList userAdapter;
    private int selectedAdapter = 1;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_create_course_student, container, false);
        studentId = (TextView)rootView.findViewById(R.id.tv_selected_student);
        sectionId = (TextView)rootView.findViewById(R.id.tv_selected_course_section);

        button = (Button)rootView.findViewById(R.id.button_create_student_course);
        button.setOnClickListener(this);
        showStudents = (Button)rootView.findViewById(R.id.button_show_students);
        showStudents.setOnClickListener(this);
        showSectionCourses = (Button)rootView.findViewById(R.id.button_show_courses_sections);
        showSectionCourses.setOnClickListener(this);
        progressBar = (ProgressBar)rootView.findViewById(R.id.progressBar_student_section);
        listView = (ListView)rootView.findViewById(R.id.listView5);
        courseSectionAdapter = new AdapterSectionCourse(getActivity());
        userAdapter = new AdapterUserList(getActivity());

        new APICaller(new APICaller.AsyncResponse() {
            @Override
            public void response(String output) {
                listOfCourseSections = new ArrayList<>();
                organizeCourseSection(output);
            }
        }).execute(getCourseSectionCall);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (selectedAdapter == 1) {
                    selectedCourseSection = listOfCourseSections.get(position);
                    sectionId.setText(selectedCourseSection.getCourseCode() + " Section 0" + selectedCourseSection.getSectionNumber());
                } else if (selectedAdapter == 2) {
                    selectedStudeng = users.get(position);
                    studentId.setText(selectedStudeng.getFirstName() + " " + selectedStudeng.getLastName());
                }
            }
        });

        return rootView;
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.button_create_student_course) {
            addNewStudentCourseSection();

        } else if (v.getId() == R.id.button_show_students) {
            selectedAdapter = 2;

            if (users != null) {
                listView.setAdapter(userAdapter);
            } else  {
                new APICaller(new APICaller.AsyncResponse() {
                    @Override
                    public void response(String output) {
                        users = new ArrayList<>();
                        organizeUsers(output);
                    }
                }).execute(getStudentsCall);
            }

        } else if (v.getId() == R.id.button_show_courses_sections) {
            selectedAdapter = 1;

            if (listOfCourseSections != null) {
                listView.setAdapter(courseSectionAdapter);
            } else {
                new APICaller(new APICaller.AsyncResponse() {
                    @Override
                    public void response(String output) {
                        listOfCourseSections = new ArrayList<>();
                        organizeCourseSection(output);
                    }
                }).execute(getCourseSectionCall);
            }
        }
    }

    private void addNewStudentCourseSection() {

        if (selectedStudeng != null && selectedCourseSection != null) {
            progressBar.setVisibility(View.VISIBLE);
            button.setVisibility(View.GONE);
            new APICaller(new APICaller.AsyncResponse() {
                @Override
                public void response(String output) {
                    if (!output.equals("0") && !output.equals("-1")) {
                        Toast.makeText(getActivity(), "Successfully added " + selectedStudeng.getFirstName() + " " + selectedStudeng.getLastName()
                                + " to " + selectedCourseSection.getCourseCode() + " Section 0" + selectedCourseSection.getSectionNumber(),
                                Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        button.setVisibility(View.VISIBLE);
                    } else if (output.equals("0")) {
                        Toast.makeText(getActivity(), "Student is already in that section.", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        button.setVisibility(View.VISIBLE);
                    } else if (output.equals("-1")) {
                        Toast.makeText(getActivity(), "Parameters not met..", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        button.setVisibility(View.VISIBLE);
                    }
                }
            }).execute(addCourseSectionStudentCall, "student="+selectedStudeng.getUserId(), "&courseSection="+selectedCourseSection.getCourseSectionId());

        } else {
            Toast.makeText(getActivity(), "Select a value for everything.", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            button.setVisibility(View.VISIBLE);
        }
    }

    private void organizeCourseSection(String output) {

        try {
            JSONObject jsonObject;
            JSONArray jsonArray = new JSONArray(output);

            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                this.listOfCourseSections.add(new CourseSectionStudent(jsonObject.getString("id"), jsonObject.getString("courseId"),
                        jsonObject.getString("courseCode"), jsonObject.getString("sectionId"), jsonObject.getString("sectionNumber"),
                        jsonObject.getString("taId"), jsonObject.getString("taUsername")));
            }

            courseSectionAdapter.replaceWith(listOfCourseSections);
            courseSectionAdapter.type = 3;
            listView.setAdapter(courseSectionAdapter);

        } catch (Exception e) {
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void organizeUsers(String output) {

        try {
            JSONObject jsonObject;
            JSONArray jsonArray = new JSONArray(output);

            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                this.users.add(new User(jsonObject.getString("id"), jsonObject.getString("username"),
                        "3", jsonObject.getString("firstname"), jsonObject.getString("lastname")));
            }

            userAdapter.replaceWith(users);
            listView.setAdapter(userAdapter);

        } catch (Exception e) {
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
