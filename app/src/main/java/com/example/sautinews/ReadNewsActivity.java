package com.example.sautinews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class ReadNewsActivity extends AppCompatActivity {
private ImageView imageViewBookmark,imageViewSend,imageViewMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_news);
        imageViewMenu=findViewById(R.id.imageViewMenuRead);
        imageViewSend=findViewById(R.id.imageViewSendRead);
        imageViewBookmark=findViewById(R.id.imageViewBookmarkRead);

        DarkModeIcons();
       imageViewBookmark.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               shakeAnimation(imageViewBookmark);
               selectIcon(imageViewBookmark);

           }
       });

        imageViewMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shakeAnimation(imageViewMenu);

                selectIcon(imageViewMenu);
            }
        });
        imageViewSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shakeAnimation(imageViewSend);
                selectIcon(imageViewSend);

            }
        });
    }
    public void shakeAnimation(ImageView imageView){
        Animation animationShake= AnimationUtils.loadAnimation(ReadNewsActivity.this,R.anim.shake_animation);
       imageView.startAnimation(animationShake);


    }
    public void  DarkModeIcons()
    {
       // Inside onCreate() or when handling dark mode change
       int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
       if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
           // Dark mode is active
           ColorStateList colorStateList = ContextCompat.getColorStateList(this, R.color.white);

           imageViewMenu.setImageTintList(colorStateList);
           imageViewSend.setImageTintList(colorStateList);
           imageViewBookmark.setImageTintList(colorStateList);


           // Add more ImageViews as needed
    }  }
    public void selectIcon(ImageView imageView){
  ColorStateList colorStateList = ContextCompat.getColorStateList(this, R.color.orange);
   imageView.setImageTintList(colorStateList);

    }

}          // Dark mode is not active