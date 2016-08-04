package com.example.owner.edp;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

/**
 * Created by Krishna N. Deoram on 2016-03-27.
 * Gumi is love. Gumi is life.
 */
public class FragmentCreateUser extends Fragment implements Strings{

    private EditText username, firstname, lastname, password;
    private Button addUser;
    private RadioGroup group;
    private int role = -1;
    private Fragment fragment;
    private ProgressBar progessBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_create_user, container, false);
        fragment = this;

        username = (EditText)rootView.findViewById(R.id.et_username);
        firstname = (EditText)rootView.findViewById(R.id.et_firstname);
        lastname = (EditText)rootView.findViewById(R.id.et_lastname);
        password = (EditText)rootView.findViewById(R.id.et_password);
        addUser = (Button)rootView.findViewById(R.id.submit_user);
        group = (RadioGroup)rootView.findViewById(R.id.rg_role);
        progessBar = (ProgressBar)rootView.findViewById(R.id.progess_bar_create_user);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radio_prof)
                    role = 1;
                else if (checkedId == R.id.radio_ta)
                    role = 2;
                else if (checkedId == R.id.radio_student)
                    role = 3;
            }
        });

        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getFragmentManager().beginTransaction().replace(R.id.container_admin, new FragmentAdminList()).commit();
                //((ActivityAdmin)getActivity()).pressBackButton();
                final String user = username.getText().toString();
                String first = firstname.getText().toString();
                String last = lastname.getText().toString();
                String pass = password.getText().toString();
                addUser.setClickable(false);

                if (!user.isEmpty() && !user.equals("") && !first.isEmpty() && !first.equals("") &&
                        !last.isEmpty() && !last.equals("") && !pass.isEmpty() && !pass.equals("") && role != 1) {
                    addUser.setVisibility(View.GONE);
                    progessBar.setVisibility(View.VISIBLE);
                    new APICaller(new APICaller.AsyncResponse() {
                        @Override
                        public void response(String output) {
                            if (output.equals("1")) {
                                addUser.setClickable(true);
                                Toast.makeText(getActivity(), "Successfully made " + user + ".", Toast.LENGTH_SHORT).show();
                                ((ActivityAdmin)getActivity()).pressBackButton();
                                //progessBar.setVisibility(View.GONE);
                                //addUser.setVisibility(View.VISIBLE);
                                //username.setText("");
                                //firstname.setText("");
                                //lastname.setText("");
                                //password.setText("");
                                //group.clearCheck();
                            } else if (output.equals("0")) {
                                Toast.makeText(getActivity(), "Username already exists.", Toast.LENGTH_SHORT).show();
                                addUser.setClickable(true);
                            } else if (output.equals("-1")) {
                                Toast.makeText(getActivity(), "Parameters are not met.", Toast.LENGTH_SHORT).show();
                                addUser.setClickable(true);
                            }
                        }
                    }).execute(addUserCall, "username=" + user, "&firstname=" + first, "&lastname=" + last, "&password=" + pass, "&role=" + role);
                } else {
                    Toast.makeText(getActivity(), "Make sure all the fields aren't empty.", Toast.LENGTH_SHORT).show();
                    addUser.setClickable(true);
                }
            }
        });

        return rootView;
    }
}
