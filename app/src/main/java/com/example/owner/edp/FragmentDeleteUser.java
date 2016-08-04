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
public class FragmentDeleteUser extends Fragment implements Strings, View.OnClickListener{

    private Button button;
    private TextView userId;
    private ListView listView;
    private ProgressBar progressBar, progressBar2;
    private ArrayList<User> users = new ArrayList<>();
    private AdapterUserList userListAdapter;
    private User userToDelete;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_delete_user, container, false);
        userId = (TextView)rootView.findViewById(R.id.tv_delete_id);
        listView = (ListView)rootView.findViewById(R.id.listView2);
        progressBar = (ProgressBar)rootView.findViewById(R.id.progressBarDelete);
        progressBar2 = (ProgressBar)rootView.findViewById(R.id.progess_bar_delete_user);

        getUsers();

        button = (Button)rootView.findViewById(R.id.button_delete);
        button.setOnClickListener(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getActivity(), "User ID: " + users.get(position).getUserId(), Toast.LENGTH_SHORT).show();
                userId.setText("User: " + users.get(position).getUsername() + " ID: " + users.get(position).getUserId());
                userToDelete = users.get(position);
            }
        });

        return rootView;
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

            userListAdapter.replaceWith(users);
            listView.setAdapter(userListAdapter);

        } catch (Exception e) {
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {

        if (userToDelete != null) {
            button.setVisibility(View.GONE);
            progressBar2.setVisibility(View.VISIBLE);
            new APICaller(new APICaller.AsyncResponse() {
                @Override
                public void response(String output) {
                    if (output.equals("1")) {
                        Toast.makeText(getActivity(), "User successfully deleted", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.GONE);
                        getUsers();
                        userId.setText("No Student Selected");
                        button.setVisibility(View.VISIBLE);
                        progressBar2.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(getActivity(), "User could not be deleted.", Toast.LENGTH_SHORT).show();
                        button.setVisibility(View.VISIBLE);
                        progressBar2.setVisibility(View.GONE);
                    }
                }
            }).execute(deleteUserCall, "id=" + userToDelete.getUserId());
        } else
            Toast.makeText(getActivity(), "Select a user to delete.", Toast.LENGTH_SHORT).show();
    }

    private void getUsers() {

        new APICaller(new APICaller.AsyncResponse() {
            @Override
            public void response(String output) {
                userListAdapter = new AdapterUserList(getActivity());
                users.clear();
                progressBar.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                organizeUsers(output);
            }
        }).execute(studentsForAdmin);
    }
}
