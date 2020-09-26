package com.example.codeit2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        auth = FirebaseAuth.getInstance();

        ((TextView)findViewById(R.id.details)).setText(auth.getCurrentUser().getDisplayName() + ", " + auth.getCurrentUser().getEmail());

        ((Button)findViewById(R.id.logout_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();

                Intent intent = new Intent(HomeActivity.this, AuthActivity.class);
                startActivity(intent);
            }
        });
    }
}