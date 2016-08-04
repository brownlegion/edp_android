package com.example.owner.edp;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.util.Log;
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
public class FragmentAddMark extends Fragment implements Strings, AdapterView.OnItemClickListener, DialogInterface.OnClickListener{

    private TextView student, course;
    private Button button;
    private ListView listView;
    private ArrayList<CourseSectionStudent> listOfStudents;
    private AdapterSectionCourse courseSectionAdapter;
    private CourseSectionStudent selectedStudent;
    private ProgressBar progressBar;
    private EditText text;
    private ProgressBar progressBar2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_add_mark, container, false);
        student = (TextView)rootView.findViewById(R.id.tv_selected_student2);
        course = (TextView)rootView.findViewById(R.id.tv_selected_course_section2);
        listView = (ListView)rootView.findViewById(R.id.listView6);
        button = (Button)rootView.findViewById(R.id.button_create_mark);
        progressBar2 = (ProgressBar)rootView.findViewById(R.id.progress_bar_add_mark);
        progressBar = (ProgressBar)rootView.findViewById(R.id.progressBar_add_mark);
        courseSectionAdapter = new AdapterSectionCourse(getActivity());
        getListOfStudents();
        listView.setOnItemClickListener(this);

        text = new EditText(getActivity());
        text.setInputType(InputType.TYPE_CLASS_NUMBER);
        AlertDialog.Builder promptBuilder = new AlertDialog.Builder(getActivity());
        promptBuilder.setView(text).setPositiveButton("Add", this).setNegativeButton("Cancel", this);
        final AlertDialog alertDialog = promptBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text.setText("0");
                alertDialog.show();
            }
        });

        return rootView;
    }

    private void getListOfStudents() {
        listView.setVisibility(View.INVISIBLE);
        progressBar2.setVisibility(View.VISIBLE);
        new APICaller(new APICaller.AsyncResponse() {
            @Override
            public void response(String output) {
                if (listOfStudents == null)
                    listOfStudents = new ArrayList<>();
                else
                    listOfStudents.clear();
                organizeStudents(output);
            }
        }).execute(getStudentsAndSectionsForProfessor, "id=" + Variables.id);
    }

    private void organizeStudents(String output) {

        try {
            JSONObject jsonObject;
            JSONArray jsonArray = new JSONArray(output);

            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                this.listOfStudents.add(new CourseSectionStudent(jsonObject.getString("id"), jsonObject.getString("studentId"),
                        jsonObject.getString("username"), jsonObject.getString("firstname"), jsonObject.getString("lastname"),
                        jsonObject.getString("courseSectionId"), jsonObject.getString("course"), jsonObject.getString("section")));
            }

            courseSectionAdapter.replaceWith(listOfStudents);
            courseSectionAdapter.type = 4;
            listView.setAdapter(courseSectionAdapter);
            listView.setVisibility(View.VISIBLE);
            progressBar2.setVisibility(View.INVISIBLE);

        } catch (Exception e) {
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        selectedStudent = listOfStudents.get(position);
        student.setText(selectedStudent.getFirstname() + " " + selectedStudent.getLastname());
        course.setText(selectedStudent.getCourseCode() + " Section 0" + selectedStudent.getSectionNumber());
    }

    @Override
    public void onClick(final DialogInterface dialog, int which) {

        if (which == -1) { //Positive Button
            if (selectedStudent != null) {
                if (!text.getText().toString().isEmpty()) {
                    int newmark = Integer.valueOf(text.getText().toString());
                    if (newmark >= 0 && newmark <= 100) {
                        Log.i("Edit", text.getText().toString());
                        new APICaller(new APICaller.AsyncResponse() {
                            @Override
                            public void response(String output) {
                                if (!output.equals("0") && !output.equals("-1")) {
                                    Toast.makeText(getActivity(), "Successfully added a mark.", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                    button.setVisibility(View.VISIBLE);
                                    getListOfStudents();
                                    selectedStudent = null;
                                    course.setText("");
                                    student.setText("");
                                    dialog.dismiss();
                                } else if (output.equals("0")) {
                                    Toast.makeText(getActivity(), "Mark already exists..", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                    button.setVisibility(View.VISIBLE);
                                    dialog.dismiss();
                                } else if (output.equals("-1")) {
                                    Toast.makeText(getActivity(), "Unable to add mark.", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                    button.setVisibility(View.VISIBLE);
                                    dialog.dismiss();
                                }
                            }
                        }).execute(addMarkCall, "studentCourse=" + selectedStudent.getCourseSectionStudentId(),
                                "&name=" + Variables.firstName + "+" + Variables.lastName, "&mark=" + newmark);
                    } else {
                        Toast.makeText(getActivity(), "Invalid value for mark.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Invalid value for mark.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Select a student.", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                button.setVisibility(View.VISIBLE);
            }
        } else if (which == -2) { //Negative Button
            Log.i("Dialog", "" + which);
            dialog.dismiss();
        }
    }
}
