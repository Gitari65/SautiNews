package com.example.sautinews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.os.Bundle;

public class NotificationMainActifity extends AppCompatActivity {
ViewPager viewPager;
NotificationPageAdapter adapter;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_main_actifity);
        viewPager=findViewById(R.id.viewPager_Notification);
        adapter=new NotificationPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
    }
}