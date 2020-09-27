package com.example.codeit2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;


public class AdminDashActivity extends AppCompatActivity
{

    class Course
    {
        private String name;
        private int enrolled;
        private String adminId;

        public Course() {}
        public Course(String name, int enr, String admin)
        {
            this.name = name;
            this.enrolled = enr;
            this.adminId = admin;
        }


        public String getName() {
            return name;
        }

        public int getEnrolled() {
            return enrolled;
        }

        public String getAdminId() {
            return adminId;
        }
    }

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dash);

        firebaseAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.admin_dash_toolbar);
        toolbar.setTitle(String.format("Welcome - %s", firebaseAuth.getCurrentUser().getDisplayName()));
        setSupportActionBar(toolbar);

        displayFragment(new AdminCoursesFragment(), true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.admin_dash_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == R.id.admin_logout) {
            firebaseAuth.signOut();

            Intent intent = new Intent(this, AuthActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        return true;
    }

    void displayFragment(Fragment frag, boolean addToBackstack)
    {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.admin_dash_frame, frag);
        if(addToBackstack) transaction.addToBackStack(null);
        transaction.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commit();
    }
}