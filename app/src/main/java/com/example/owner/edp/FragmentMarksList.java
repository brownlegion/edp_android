package com.example.owner.edp;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by Krishna N. Deoram on 2016-03-27.
 * Gumi is love. Gumi is life.
 */
public class FragmentMarksList extends Fragment implements Strings{

    private ArrayList<Marks> marks;
    private AdapterMarksList marksListAdapter;
    private ListView listView;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootVIew = inflater.inflate(R.layout.fragment_after, container, false);
        //ActivityAfterSignedIn.currentFragment = this;

        listView = (ListView)rootVIew.findViewById(R.id.listView);
        progressBar = (ProgressBar)rootVIew.findViewById(R.id.progressBar);

        if (Variables.role == 1) {
            new APICaller(new APICaller.AsyncResponse() {
                @Override
                public void response(String output) {
                    marksListAdapter = new AdapterMarksList(getActivity());
                    marksListAdapter.notStudent = true;
                    marks = new ArrayList<>();
                    progressBar.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                    organizeMarks(output);
                }
            }).execute(marksForProfessor + Variables.id);

        } else if (Variables.role == 2) {
            new APICaller(new APICaller.AsyncResponse() {
                @Override
                public void response(String output) {
                    marksListAdapter = new AdapterMarksList(getActivity());
                    marksListAdapter.notStudent = true;
                    marks = new ArrayList<>();
                    progressBar.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                    organizeMarks(output);
                }
            }).execute(marksForTa + Variables.id);

        } else if (Variables.role == 3) {
            new APICaller(new APICaller.AsyncResponse() {
                @Override
                public void response(String output) {
                    marksListAdapter = new AdapterMarksList(getActivity());
                    marksListAdapter.notStudent = false;
                    marks = new ArrayList<>();
                    progressBar.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                    organizeMarks(output);
                }
            }).execute(marksForStudents + Variables.id);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (Variables.role == 3) {
                    Log.i("Student Details", "Role = " + Variables.role);
                    startActivity(new Intent(getActivity(), ActivityDetails.class).putExtra("mark", marks.get(position)));
                } else if (Variables.role == 0) {
                    Log.i("Admin", "Role = " + Variables.role);
                } else {
                    Log.i("Prof or TA", "Role = " + Variables.role);
                    startActivity(new Intent(getActivity(), ActivityDetails.class).putExtra("mark", marks.get(position)));
                }
            }
        });

        return rootVIew;
    }

    private void organizeMarks(String output) {

        try {
            JSONObject jsonObject;
            JSONArray jsonArray = new JSONArray(output);

            for (int i = 0; i < jsonArray.length(); i ++) {
                jsonObject = jsonArray.getJSONObject(i);
                this.marks.add(new Marks(jsonObject.getInt("id"), jsonObject.getInt("mark"), jsonObject.getString("updatedLast"),
                        jsonObject.getString("updatedBy"), jsonObject.getString("course"), jsonObject.getInt("section"),
                        jsonObject.getString("firstname"), jsonObject.getString("lastname")));
            }

            marksListAdapter.replaceWith(this.marks);
            listView.setAdapter(marksListAdapter);
        } catch (Exception e) {
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
