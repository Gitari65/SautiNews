package com.example.sautinews;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {
Handler handler;
ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar=new ProgressBar(MainActivity.this);
        progressBar.setVisibility(View.VISIBLE);
        handler=new Handler();
        handler.postDelayed(()-> {

        },3000);
        // Check if it's the first time opening the app
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        boolean isFirstTime = sharedPreferences.getBoolean("isFirstTime", true);

        if (isFirstTime) {
            // First time opening the app, start the FirstTimeActivity
            Intent intent= new Intent(MainActivity.this,WelcomeMainActivity.class);
            startActivity(intent);

            // Set the flag indicating that it's not the first time anymore
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isFirstTime", false);
            editor.apply();
        } else {
            // Not the first time, start another activity
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        // Finish the launcher activity to prevent going back to it
        finish();


    }
}