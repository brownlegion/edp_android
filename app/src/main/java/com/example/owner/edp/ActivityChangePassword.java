package com.example.owner.edp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class ActivityChangePassword extends AppCompatActivity implements Strings{

    private EditText setPassword, newPassword;
    private Button button;
    private String oldPassword = "";
    private ProgressBar progressBar;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        mContext = this;

        oldPassword = getIntent().getStringExtra("old");
        setPassword = (EditText)findViewById(R.id.et_old);
        newPassword = (EditText)findViewById(R.id.et_new);
        progressBar = (ProgressBar)findViewById(R.id.progress_bar_change_password);

        button = (Button)findViewById(R.id.button_change);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String newPass = newPassword.getText().toString();
                String setPass = setPassword.getText().toString();
                button.setClickable(false);

                if (!newPass.isEmpty() && !newPass.equals("") && !setPass.isEmpty() && !setPass.equals("")) {

                    if (oldPassword.equals(setPass)) {
                        progressBar.setVisibility(View.VISIBLE);
                        button.setVisibility(View.GONE);
                        new APICaller(new APICaller.AsyncResponse() {
                            @Override
                            public void response(String output) {
                                if (output.contains(String.valueOf(Variables.id))) {
                                    startActivity(new Intent(ActivityChangePassword.this, ActivitySignin.class)
                                            .putExtra("changedPassword", true).putExtra("animate", true));
                                    finish();
                                }
                            }
                        }).execute(editUserCall, "id="+Variables.id, "&password="+newPass);
                    } else {
                        Toast.makeText(mContext, "Old password does not match.", Toast.LENGTH_SHORT).show();
                        button.setClickable(true);
                        button.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                } else {
                    Toast.makeText(mContext, "Both fields cannot be empty.", Toast.LENGTH_SHORT).show();
                    button.setClickable(true);
                }
            }
        });
    }
}
