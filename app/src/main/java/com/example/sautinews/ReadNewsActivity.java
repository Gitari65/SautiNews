package com.example.sautinews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.IntentCompat;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ReadNewsActivity extends AppCompatActivity {
private ImageView imageViewBookmark,imageViewSend,imageViewMenu,imageViewCoverPicture;
TextView textViewName,textViewUserName,textViewTime,textViewContent,textViewTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_news);
        imageViewMenu=findViewById(R.id.imageViewMenuRead);
        imageViewSend=findViewById(R.id.imageViewSendRead);
        imageViewBookmark=findViewById(R.id.imageViewBookmarkRead);
        imageViewCoverPicture=findViewById(R.id.imageViewCoverPictureRead);
        textViewName=findViewById(R.id.textViewAuthorNameRead);
        textViewUserName=findViewById(R.id.textViewAuthorUserName);
        textViewTime=findViewById(R.id.textViewTimeposted);
        textViewTitle=findViewById(R.id.textViewArticleTitleRead);
        textViewContent=findViewById(R.id.textViewArticleContentRead);





        DarkModeIcons();
        getNewsData();
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
    public void getNewsData()
    {
        Intent intent=getIntent();
        String articleId=intent.getStringExtra("ArticleId");
        if (articleId!=null){
            DatabaseReference myRef= FirebaseDatabase.getInstance().getReference("Articles").child("published").child(articleId);
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){

                        Article article= snapshot.getValue(Article.class);
                        if(article!=null)
                        {

                            String timestamp= String.valueOf(article.getTimestamp());
                            String time=getFormattedTimestamp(timestamp);
                            String name=article.getFullName();
                            String articleContent=article.getArticleContent();
                            String articleTitle=article.getArticleTitle();
                            String CoverPicurl=article.getCoverPicUrl();

        Picasso.get().load(CoverPicurl).placeholder(R.drawable.news_icon3).
                error(R.drawable.news_icon3).into(imageViewCoverPicture);
        textViewTime.setText(time);
        textViewName.setText(name);
        textViewContent.setText(articleContent);
        textViewTitle.setText(articleTitle);
                        }
                        }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
    }



    }
    private String getFormattedTimestamp(String timestamp) {
//        String timestamp= String.valueOf(article.getTimestamp());
        // Get the current time in milliseconds
        long currentTime = System.currentTimeMillis();

        // Convert the timestamp to milliseconds
        long articleTime = Long.parseLong(timestamp);

        // Calculate the time difference in milliseconds
        long timeDifference = currentTime - articleTime;

        // Convert the time difference to appropriate units (seconds, minutes, hours, days)
        long seconds = timeDifference / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        // Format the timestamp text based on the time difference
        if (days ==1) {
            // More than a day ago
            return days + " day ago";
        }
        if (days > 1) {
            // More than a day ago
            return days + " days ago";
        }else if (hours > 0) {
            // More than an hour ago
            return hours + " hrs ago";
        } else if (minutes > 0) {
            // More than a minute ago
            return minutes + " mins ago";
        } else {
            // Less than a minute ago or just a few seconds ago
            return seconds+" seconds ago";
        }
    }

}          // Dark mode is not active