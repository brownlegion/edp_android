package com.example.owner.edp;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONObject;

/**
 * Created by Krishna N. Deoram on 2016-02-22.
 * Gumi is love. Gumi is life.
 */
public class ActivityAfterSignedIn extends AppCompatActivity implements Strings{

    //private ArrayList<User> users;
    private Context mContext;
    public FragmentMarksList fragmentMarksList = new FragmentMarksList();
    public Fragment currentFragment = fragmentMarksList;
    private boolean onResume = false;
    public static boolean somethingAlreadyVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container_marks);
        mContext = this;
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.marks_container, currentFragment, fragmentMarksListTag).commit();
        }
        getRole();
    }

    /*@Override
    protected void onPause() {
        Log.i("ActivityAfterSignedIn", "onPause()");
        onResume = true;
        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.i("ActivityAfterSignedIn", "onResume()");
        if (onResume) {
            onResume = false;
            final FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.remove(currentFragment);
            currentFragment = new FragmentMarksList();
            ft.add(R.id.marks_container, currentFragment, fragmentMarksListTag);
            ft.commit();
        }
        super.onResume();
    }*/

    private void getRole() {
        new APICaller(new APICaller.AsyncResponse() {
            @Override
            public void response(String output) {
                try {
                    JSONObject object = new JSONObject(output);
                    Role.getInstance().setCreateGrades(object.getBoolean("addGrades"));
                    Role.getInstance().setDeleteGrades(object.getBoolean("deleteGrades"));
                    Role.getInstance().setEditGrades(object.getBoolean("editGrades"));
                    Role.getInstance().setViewGrades(object.getBoolean("viewGrades"));
                    Role.getInstance().setCreateUsers(object.getBoolean("createUsers"));
                    Role.getInstance().setDeleteGrades(object.getBoolean("deleteGrades"));
                    Role.getInstance().setEditGrades(object.getBoolean("editGrades"));
                    Role.getInstance().setViewGrades(object.getBoolean("viewGrades"));
                    Role.getInstance().setCreateCourse(object.getBoolean("createCourses"));
                    Role.getInstance().setCreateSection(object.getBoolean("createSections"));
                    Role.getInstance().setAddStudents(object.getBoolean("addStudents"));
                    Log.i("User", "Got permissions.");
                } catch (Exception e) {
                    Log.e("Exception", e.toString());
                    e.printStackTrace();
                }
            }
        }).execute(getUserRoleCall, "id=" + Variables.id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (Variables.role == 1)
            getMenuInflater().inflate(R.menu.menu_professor, menu);

        else if (Variables.role == 2)
            getMenuInflater().inflate(R.menu.menu_ta, menu);

        else if (Variables.role == 3)
            getMenuInflater().inflate(R.menu.menu_student, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        Log.i("stack size", "" + getFragmentManager().getBackStackEntryCount());
        //String tag = currentFragment.getTag();
        ft.remove(currentFragment);

        if (id == R.id.sign_out) {
            finish();
            startActivity(new Intent(mContext, ActivitySignin.class).putExtra("animate", true));
            //currentFragment = new FragmentMarksList();
        }  else if (id == R.id.add_mark) {
            if (getFragmentManager().getBackStackEntryCount() == 0) {
                currentFragment = new FragmentAddMark();
                ft.addToBackStack(fragmentAddMark);
                ft.replace(R.id.marks_container, currentFragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        Log.i("ActivityAfterSignedIn", "onBackPressed");
        //Log.i("ActivityAfterSignedIn", "" + currentFragment.getId());
        final Fragment f = getFragmentManager().findFragmentByTag(fragmentMarksListTag);
        if (f.isVisible())  {
            finish();
            startActivity(new Intent(mContext, ActivitySignin.class).putExtra("animate", true));
        } else  {
            Log.i("ActivityAfterSignedIn", "new FragmentMarksList()");
            getFragmentManager().popBackStack();
        }
    }

    public void pressBackButton() {
        onBackPressed();
    }

    public void getRidOfFragment(Fragment f) {
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        //getFragmentManager().popBackStack();
        ft.remove(f);
        currentFragment = new FragmentMarksList();
        ft.replace(R.id.marks_container, currentFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }
}
