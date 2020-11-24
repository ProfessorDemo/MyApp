package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

//    ImageButton IBProfile, IBHome, IBBook, IBSetting;

    BottomNavigationView bottomNavigationView;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.BottomNav);

        bottomNavigationView.setSelectedItemId(R.id.NavHome);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

/*
        IBProfile = findViewById(R.id.IBProfile);
        IBBook = findViewById(R.id.IBBook);
        IBSetting = findViewById(R.id.IBSetting);
        IBHome = findViewById(R.id.IBHome);


        mAuth = FirebaseAuth.getInstance();

        IBProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mAuth.getCurrentUser() != null){
                    startActivity(new Intent(HomeActivity.this,ProfileActivity.class));
                }
                else{
                    Toast.makeText(HomeActivity.this,"Please Sign In to Continue",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(HomeActivity.this,LoginActivity.class));
                }
            }
        });


        IBBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAuth.getCurrentUser() != null){
                    startActivity(new Intent(HomeActivity.this,Book1Activity.class));

                }
                else{
                    Toast.makeText(HomeActivity.this,"Please Sign In to Continue",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(HomeActivity.this,LoginActivity.class));
                }
            }
        });


 */
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.NavHome:
                break;

            case R.id.NavProfile:
               NavProfile();
                break;

            case R.id.NavBook:
               NavBook();
                break;

            case R.id.NavSetting:
                break;
        }

        return true;
    }

    private void NavBook() {
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null){
            startActivity(new Intent(HomeActivity.this,Book1Activity.class));
        }
        else{
            Toast.makeText(HomeActivity.this,"Please Sign In to Continue",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(HomeActivity.this,LoginActivity.class));
        }
    }

    private void NavProfile() {
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null){
            startActivity(new Intent(HomeActivity.this,ProfileActivity.class));
        }
        else{
            Toast.makeText(HomeActivity.this,"Please Sign In to Continue",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(HomeActivity.this,LoginActivity.class));
        }
    }
}
