package com.example.sautinews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class NewsHomePage extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_home_page);

        Fragment selectedFragment =new HomeFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, selectedFragment)
                .commit();
      

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

            // Set the listener for item selection
            bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;// Handle item selection here
                    switch (item.getItemId()) {
                        case R.id.bottom_Home:
                            selectedFragment=new HomeFragment();
                            // Handle Home selection
                            break;
                        case R.id.bottom_Create:
                            Intent intent=new Intent(NewsHomePage.this,EditArticleActivity.class);
                            startActivity(intent);


                            // Handle Search selection
                            break;
                        case R.id.bottom_Explore:
                            selectedFragment=new ExploreFragment();
                            // Handle Notifications selection
                            break;
                        case R.id.bottom_Profile:
                            // Handle Profile selection
                            break;
                        case R.id.bottom_Article:
                            // Handle Profile selection
                            break;
                    }
                    if (selectedFragment != null) {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragmentContainer, selectedFragment)
                                .commit();
                    }
                    return true;
                }
            });
        }


    }