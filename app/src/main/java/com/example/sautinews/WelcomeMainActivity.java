package com.example.sautinews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

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
    }
}