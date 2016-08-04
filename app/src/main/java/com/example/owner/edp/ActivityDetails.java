package com.example.owner.edp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Krishna N. Deoram on 2016-03-26.
 * Gumi is love. Gumi is life.
 */
public class ActivityDetails extends AppCompatActivity implements Strings, DialogInterface.OnClickListener, View.OnClickListener {

    private TextView course, section, userMark, updatedBy, updatedOn, name;
    private Context mContext;
    private EditText text;
    private  Marks mark;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_details);
        mContext = this;
        course = (TextView)findViewById(R.id.course_code);
        section = (TextView)findViewById(R.id.section);
        userMark = (TextView)findViewById(R.id.mark);
        updatedBy = (TextView)findViewById(R.id.updated_by);
        updatedOn = (TextView)findViewById(R.id.updated_on);
        name = (TextView)findViewById(R.id.name);
        text = new EditText(mContext);
        text.setInputType(InputType.TYPE_CLASS_NUMBER);

        int role = Variables.role;
        mark = getIntent().getParcelableExtra("mark");

        if (!Role.getInstance().isEditGrades()) {
            name.setVisibility(View.GONE);
            course.setText(mark.getCourse());
            section.setText("0" + mark.getSection());
            updatedBy.setText("Updated by " + mark.getUpdatedBy());
            updatedOn.setText("on " + mark.getUpdatedLast());
            userMark.setText("" + mark.getMark());

        } else {
            name.setVisibility(View.VISIBLE);
            course.setText(mark.getCourse());
            section.setText("0" + mark.getSection());
            updatedBy.setText("Updated by " + mark.getUpdatedBy());
            updatedOn.setText("on " + mark.getUpdatedLast());
            name.setText(mark.getFirstName() + " " + mark.getLastName());
            userMark.setText("" + mark.getMark());
        }
        AlertDialog.Builder promptBuilder = new AlertDialog.Builder(mContext);
        promptBuilder.setView(text).setPositiveButton("Edit", this).setNegativeButton("Cancel", this);
        alertDialog = promptBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);

        Log.i("Check", "Before");
        if (Role.getInstance().isEditGrades()) {
            userMark.setOnClickListener(this);
        }
        Log.i("Check", "After");
    }

    @Override
    public void onClick(final DialogInterface dialog, int which) {

        Log.i("Dialog", "" + which);
        if (which == -1) { //Positive Button
            if (!text.getText().toString().isEmpty()) {
                int newmark = Integer.valueOf(text.getText().toString());
                if (newmark >= 0 && newmark <= 100) {
                    Log.i("Edit", text.getText().toString());
                    new APICaller(new APICaller.AsyncResponse() {
                        @Override
                        public void response(String output) {
                            if (output.equals("1")) {
                                dialog.dismiss();
                                finish();
                            }
                        }
                    }).execute(editMarkCall, "id=" + mark.getId(), "&mark=" + newmark, "&name=" + Variables.firstName + "+" + Variables.lastName);

                } else {
                    Toast.makeText(mContext, "Invalid value for mark.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(mContext, "Invalid value for mark.", Toast.LENGTH_SHORT).show();
            }
        } else if (which == -2) { //Negative Button
            Log.i("Dialog", "" + which);
            dialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        text.setText("" + mark.getMark());
        alertDialog.show();
    }
}
