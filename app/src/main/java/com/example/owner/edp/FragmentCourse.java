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
 * Created by Krishna N. Deoram on 2016-03-27.
 * Gumi is love. Gumi is life.
 */
public class FragmentCourse extends Fragment implements Strings, View.OnClickListener{

    private Button button;
    private EditText courseId, title;
    //private Fragment fragment;
    private TextView  profId;
    private ListView listView;
    private AdapterUserList userListAdapter;
    private ArrayList<User> users;
    private User selectedProfessor;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_course, container, false);

        courseId = (EditText)rootView.findViewById(R.id.et_course_id_for_section);
        profId = (TextView)rootView.findViewById(R.id.tv_selected_professor);
        title = (EditText)rootView.findViewById(R.id.et_course_title);
        listView = (ListView)rootView.findViewById(R.id.listView4);
        progressBar = (ProgressBar)rootView.findViewById(R.id.progress_bar_create_course);
        //fragment = this;

        button = (Button)rootView.findViewById(R.id.button_add_course);
        button.setOnClickListener(this);

        new APICaller(new APICaller.AsyncResponse() {
            @Override
            public void response(String output) {
                userListAdapter = new AdapterUserList(getActivity());
                users = new ArrayList<>();
                organizeUsers(output);
            }
        }).execute(getProfessorCall);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedProfessor = users.get(position);
                profId.setText("Professor Selected: " + selectedProfessor.getUsername());
            }
        });

        return rootView;
    }

    @Override
    public void onClick(View v) {

        final String course = courseId.getText().toString().toUpperCase();
        final String course2 = course.replace(" ", "+");
        String prof = String.valueOf(selectedProfessor.getUserId());
        String courseTitle = title.getText().toString();
        courseTitle = courseTitle.replace(" ", "+");
        button.setClickable(false);

        if (!course.equals("") && !course.isEmpty() && !courseTitle.equals("") && !courseTitle.isEmpty() &&
                !prof.equals("") && !prof.isEmpty() && course2.matches("^[A-Z]{3}[+]\\d{3}")) {
            progressBar.setVisibility(View.VISIBLE);
            button.setVisibility(View.GONE);
            new APICaller(new APICaller.AsyncResponse() {
                @Override
                public void response(String output) {
                    if (!output.equals("-1") && !output.equals("0")) {
                        button.setClickable(true);
                        Toast.makeText(getActivity(), "Successfully made " + course + ".", Toast.LENGTH_SHORT).show();
                        ((ActivityAdmin)getActivity()).pressBackButton();
                    } else if (output.equals("0")) {
                        Toast.makeText(getActivity(), "Course code already exists.", Toast.LENGTH_SHORT).show();
                        button.setClickable(true);
                        button.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                    } else if (output.equals("-1")) {
                        Toast.makeText(getActivity(), "Parameters are not met.", Toast.LENGTH_SHORT).show();
                        button.setClickable(true);
                        button.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }
            }).execute(addCourseCall, "code="+course2, "&title="+courseTitle, "&profid="+prof);
        } else if (!course2.matches("^[A-Z]{3}[+]\\d{3}")) {
            Toast.makeText(getActivity(), "Properly format the course code.", Toast.LENGTH_SHORT).show();
            button.setClickable(true);
        } else {
            Toast.makeText(getActivity(), "Make sure all the fields aren't empty.", Toast.LENGTH_SHORT).show();
            button.setClickable(true);
        }
    }

    private void organizeUsers(String output) {

        try {
            JSONObject jsonObject;
            JSONArray jsonArray = new JSONArray(output);

            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                this.users.add(new User(jsonObject.getString("id"), jsonObject.getString("username"),
                        "1", jsonObject.getString("firstname"), jsonObject.getString("lastname")));
            }

            userListAdapter.replaceWith(users);
            listView.setAdapter(userListAdapter);

        } catch (Exception e) {
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
