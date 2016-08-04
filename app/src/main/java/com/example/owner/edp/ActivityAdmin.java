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

public class ActivityAdmin extends AppCompatActivity implements Strings{

    private Context mContext;
    public FragmentAdminList fragmentAdminList = new FragmentAdminList();
    public Fragment currentFragment = fragmentAdminList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container_admin);
        mContext = this;
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.admin_container, currentFragment, fragmentUsersListTag).commit();
        }
        getRole();
    }

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
                    Log.i("Admin", "Got permissions.");
                } catch (Exception e) {
                    Log.e("Exception", e.toString());
                    e.printStackTrace();
                }
            }
        }).execute(getUserRoleCall, "id=" + Variables.id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.remove(currentFragment);

        if (id == R.id.sign_out) {
            finish();
            startActivity(new Intent(mContext, ActivitySignin.class).putExtra("animate", true));
            //currentFragment = new FragmentAdminList();
        } else if (id == R.id.create_course_section) {
            if (getFragmentManager().getBackStackEntryCount() == 0) {
                currentFragment = new FragmentCourseSection();
                ft.addToBackStack(fragmentCourseSection);
                ft.replace(R.id.admin_container, currentFragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }
        } else if (id == R.id.create_user) {
            if (getFragmentManager().getBackStackEntryCount() == 0) {
                currentFragment = new FragmentCreateUser();
                ft.addToBackStack(fragmentAddUser);
                ft.replace(R.id.admin_container, currentFragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }
        } else if (id == R.id.create_course) {
            if (getFragmentManager().getBackStackEntryCount() == 0) {
                currentFragment = new FragmentCourse();
                ft.addToBackStack(fragmentAddCourse);
                ft.replace(R.id.admin_container, currentFragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }
        } else if (id == R.id.delete_user) {
            if (getFragmentManager().getBackStackEntryCount() == 0) {
                currentFragment = new FragmentDeleteUser();
                ft.addToBackStack(fragmentDeleteUser);
                ft.replace(R.id.admin_container, currentFragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }
        }  else if (id == R.id.create_course_student) {
            if (getFragmentManager().getBackStackEntryCount() == 0) {
                currentFragment = new FragmentCourseStudent();
                ft.addToBackStack(fragmentCourseStudent);
                ft.replace(R.id.admin_container, currentFragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        final Fragment f = getFragmentManager().findFragmentByTag(fragmentUsersListTag);
        //Log.i("current fragment", f.getTag());
        if (f.isVisible())  {
            finish();
            startActivity(new Intent(mContext, ActivitySignin.class).putExtra("animate", true));
        } else  {
            Log.i("ActivityAfterSignedIn", "new FragmentAdminList()");
            getFragmentManager().popBackStack();
        }
    }

    public void pressBackButton() {
        onBackPressed();
    }

    public void getRidOfFragment(Fragment f) {
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        getFragmentManager().popBackStack();
        ft.remove(f);
        currentFragment = new FragmentAdminList();
        ft.replace(R.id.admin_container, currentFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }
}
