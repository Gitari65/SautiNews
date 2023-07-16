package com.example.sautinews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class WelcomeMainActivity extends AppCompatActivity {
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_main);
        viewPager=findViewById(R.id.viewpager);
       SimpleFragmentPageAdapter simpleFragmentPageAdapter=new SimpleFragmentPageAdapter(getSupportFragmentManager());
       viewPager.setAdapter(simpleFragmentPageAdapter);
        // Check if it's the first time opening the app
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        boolean isFirstTime = sharedPreferences.getBoolean("isFirstTime", true);

        if (isFirstTime) {
            // First time opening the app, start the FirstTimeActivity
            Intent intent= new Intent(WelcomeMainActivity.this,WelcomeMainActivity.class);
            startActivity(intent);

            // Set the flag indicating that it's not the first time anymore
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isFirstTime", false);
            editor.apply();
        } else {
            // Not the first time, start another activity
            Intent intent = new Intent(WelcomeMainActivity.this, LoginActivity.class);
            startActivity(intent);
        }


    }
}