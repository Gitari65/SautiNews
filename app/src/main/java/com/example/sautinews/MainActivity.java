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
Intent intent=new Intent(MainActivity.this,WelcomeMainActivity.class);
startActivity(intent);
        },2000);



    }
}