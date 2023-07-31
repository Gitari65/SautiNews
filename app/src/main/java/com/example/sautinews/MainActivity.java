package com.example.sautinews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
Handler handler;
ProgressBar progressBar;
ImageView imageView;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView=findViewById(R.id.imageViewLogo);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.spin_animation);

        // Start the animation on the ImageView
        imageView.startAnimation(animation);
        progressBar=new ProgressBar(MainActivity.this);
        progressBar.setVisibility(View.VISIBLE);
        handler=new Handler();
        handler.postDelayed(()-> {
Intent intent=new Intent(MainActivity.this,WelcomeMainActivity.class);
startActivity(intent);
        },2000);



    }

}