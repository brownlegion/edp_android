package com.example.owner.edp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

/**
 * Created by Krishna N. Deoram on 2016-01-30.
 * Gumi is love. Gumi is life.
 */
public class ActivitySignin extends AppCompatActivity implements Strings{

    private Context mContext;
    private EditText username;
    private EditText password;
    private boolean fromPasswordChanged;
    private TextView text;
    private Button signin;
    private ProgressBar progressBar;
    private boolean startup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_signin);
        mContext = this;
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        text = (TextView)findViewById(R.id.startup_text);
        signin = (Button)findViewById(R.id.signin);
        progressBar = (ProgressBar)findViewById(R.id.signin_progressbar);
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        String lastUsername = prefs.getString("username", "");
        String lastPassword = prefs.getString("password", "");
        fromPasswordChanged = getIntent().getBooleanExtra("changedPassword", false);
        startup = getIntent().getBooleanExtra("animate", false);

        if (!fromPasswordChanged) {
            if (!lastUsername.isEmpty())
                username.setText(lastUsername);

            if (!lastPassword.isEmpty())
                password.setText(lastPassword);
        }

        animate(startup);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                signin.setVisibility(View.GONE);
                new APICaller(new APICaller.AsyncResponse() {
                    @Override
                    public void response(String output) {
                        try {
                            getEverything(output);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }).execute(signIn, "username=" + username.getText().toString(), "&password=" + password.getText().toString());
            }
        });
    }

    private void animate(boolean animate) {

        if (!animate) {

            text.animate().alpha(0f).setDuration(1500).start();
            username.animate().alpha(1f).setDuration(3000).start();
            password.animate().alpha(1f).setDuration(3000).start();
            signin.animate().alpha(1f).setDuration(3000).start();
        } else {
            text.animate().alpha(0f).setDuration(0).start();
            username.animate().alpha(1f).setDuration(0).start();
            password.animate().alpha(1f).setDuration(0).start();
            signin.animate().alpha(1f).setDuration(0).start();
        }
    }

    private void getEverything(final String output) throws Exception {

        Log.i("username and password", username.getText().toString() + " " + password.getText().toString());
        JSONObject object = new JSONObject(output);

        if (!object.getBoolean("Sign In")) {
            Toast.makeText(mContext, "Sign in unsuccessful.", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            signin.setVisibility(View.VISIBLE);
        } else {

            Variables.id = object.getInt("ID");

            if (object.getBoolean("Registered")) {
                signIn(output);
            } else {
                AlertDialog.Builder promptBuilder = new AlertDialog.Builder(mContext);
                promptBuilder.setTitle("")
                        .setMessage("You haven't changed your password yet. Would you like to change your password now?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(ActivitySignin.this, ActivityChangePassword.class).putExtra("old", password.getText().toString()));
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                signIn(output);
                            }
                        });
                final AlertDialog alertDialog = promptBuilder.create();
                alertDialog.show();
            }
        }
    }

    private void signIn(String output) {

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString("username", username.getText().toString());
        editor.putString("password", password.getText().toString());
        editor.apply();
        Variables.username = username.getText().toString();

        try {
            JSONObject jsonObject = new JSONObject(output);
            Variables.id = jsonObject.getInt("ID");
            Variables.role = jsonObject.getInt("Role");
            Variables.firstName = jsonObject.getString("FirstName");
            Variables.lastName = jsonObject.getString("LastName");

            if (Variables.role == 0) {
                Toast.makeText(mContext, "Sign In Successful. Welcome, " + Variables.firstName + " " + Variables.lastName, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(mContext, ActivityAdmin.class));
                finish();
            } else {
                Toast.makeText(mContext, "Sign In Successful. Welcome, " + Variables.firstName + " " + Variables.lastName + "!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(mContext, ActivityAfterSignedIn.class));
                finish();
            }

        } catch (Exception e) {
            Log.e("Sign In Exception", e.toString());
            Toast.makeText(mContext, e.toString(), Toast.LENGTH_LONG).show();
        }
    }
}
