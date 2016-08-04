package com.example.owner.edp;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Krishna N. Deoram on 2016-03-27.
 * Gumi is love. Gumi is life.
 */
public class FragmentAdminList extends Fragment implements Strings{

    private ArrayList<User> users;
    private AdapterUserList userListAdapter;
    private ListView listView;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_after, container, false);

        listView = (ListView)rootView.findViewById(R.id.listView);
        progressBar = (ProgressBar)rootView.findViewById(R.id.progressBar);

        new APICaller(new APICaller.AsyncResponse() {
            @Override
            public void response(String output) {
                userListAdapter = new AdapterUserList(getActivity());
                users = new ArrayList<>();
                progressBar.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                organizeUsers(output);
            }
        }).execute(recordsForAdmin);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //startActivity(new Intent(getActivity(), ActivityAdmin.class).putExtra("user", users.get(position)));
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
                        jsonObject.getString("role"), jsonObject.getString("firstname"), jsonObject.getString("lastname")));
            }

            userListAdapter.replaceWith(users);
            listView.setAdapter(userListAdapter);

        } catch (Exception e) {
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
