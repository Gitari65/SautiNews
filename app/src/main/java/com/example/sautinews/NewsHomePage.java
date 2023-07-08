package com.example.sautinews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class NewsHomePage extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_home_page);


            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new HomeFragment())
                    .commit();




            bottomNavigationView = findViewById(R.id.bottom_navigation);

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