package com.example.sautinews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.IntentCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class ReadNewsActivity extends AppCompatActivity {
private ImageView imageViewBookmark,imageViewSend,imageViewMenu,imageViewCoverPicture,imageViewLike;
TextView textViewName,textViewUserName,textViewTime,textViewContent,textViewTitle;
String authorUserName;
String articleId,authorId;
TextView followButton;
boolean isBookmarked=false,isFollowing=false,isLiked=false;

    @SuppressLint("MissingInflatedId")
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
        imageViewLike=findViewById(R.id.imageViewAuthorPicReadLike);
        Intent intent=getIntent();
        followButton=findViewById(R.id.buttonFollowRead);
         articleId=intent.getStringExtra("ArticleId");
        authorId=intent.getStringExtra("AuthorId");
         checkBookmarkStatus(articleId,imageViewBookmark);
         checkFollowingStatus(authorId);
         checkLikeStatus();
        followButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                isFollowing=!isFollowing;
                updateFollowingStatus();
                if (isFollowing) {
                    followButton.setText("Following");
                    followButton.setTextColor( ContextCompat.getColor(getApplicationContext(), R.color.black));
                    followButton.setBackgroundResource(R.drawable.rounded_button_following); // Set the custom shape drawable as the background
                } else {
                    followButton.setText("Follow");
                    followButton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.orange));
                    followButton.setBackgroundResource(R.drawable.rounded_button_follow); // Set the same custom shape drawable as the background
                }

            }
        });


        imageViewLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toggle the like state
                isLiked = !isLiked;
                updateLikeStatus();

                // Update the ImageView with the appropriate heart icon
                if (isLiked) {
                    imageViewLike.setImageResource(R.drawable.thumbs_up_filled);
                    animateHeartEmpty();
                } else {
                    imageViewLike.setImageResource(R.drawable.thumbs_up_unfilled);
                    animateHeartEmpty();
                }
            }
        });



        DarkModeIcons();
        getNewsData();
       imageViewBookmark.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               shakeAnimation(imageViewBookmark);
               updateBookmarkStatus(articleId);
               if (isBookmarked) {
                   imageViewBookmark.setImageResource(R.drawable.ic_bookmark_filled);
               } else {
                   imageViewBookmark.setImageResource(R.drawable.ic_bookmark_unmarked);
               }

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

    private void animateHeartEmpty() {

            ScaleAnimation scaleAnimation = new ScaleAnimation(
                    1.0f, 1.2f, // fromX, toX
                    1.0f, 1.2f, // fromY, toY
                    Animation.RELATIVE_TO_SELF, 0.5f, // pivotXType, pivotXValue
                    Animation.RELATIVE_TO_SELF, 0.5f  // pivotYType, pivotYValue
            );

            scaleAnimation.setDuration(200);
            scaleAnimation.setFillAfter(true);
            scaleAnimation.setRepeatCount(1);
            scaleAnimation.setRepeatMode(Animation.REVERSE);

            imageViewLike.startAnimation(scaleAnimation);

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
                            String authorId=article.getAuthorId();

                            getUserInfo(authorId);
        Picasso.get().load(CoverPicurl).placeholder(R.drawable.news_icon3).
                error(R.drawable.news_icon3).into(imageViewCoverPicture);
        textViewTime.setText(time);
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
    public void getUserInfo(String authorId){
        // ...
DatabaseReference myRef1;
        myRef1 = FirebaseDatabase.getInstance().getReference().child("users");
        myRef1.orderByChild("authorId").equalTo(authorId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        // Get the user data from the matched user node
                        User user = userSnapshot.getValue(User.class);
                        if (user != null) {
                            authorUserName = user.getUserName();
                            textViewUserName.setText(authorUserName);
                            String name=user.getFullName();
                            textViewName.setText(name);
                            // Rest of the code...
                        }
                        return; // Exit the loop after finding the user
                    }
                } else {
                    // Handle the case where the user data doesn't exist or the currentId is not found
                    // (Optional: Show a message or take appropriate action)
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle any database error that occurred during the query
                // (Optional: Show a message or take appropriate action)
            }
        });

    }
    private void updateBookmarkStatus(String articleId) {
        // Get the authenticated user's ID
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();

// Get a reference to the "articles" node in the Firebase Database
        DatabaseReference articlesRef = FirebaseDatabase.getInstance().getReference().child("articlesInfo");

// Get a reference to the specific article node
        DatabaseReference articleRef = articlesRef.child(userId).child(articleId);
// Assume you have a boolean variable indicating the bookmark status
//        boolean isArticleBookmarked = true; // Replace this with the actual bookmark status

// Create a HashMap to update the bookmark status field in the database
        HashMap<String, Object> updateData = new HashMap<>();
        updateData.put("isBookmarked", isBookmarked);

// Use the updateChildren() method to update the bookmark status field for the specific article
        articleRef.updateChildren(updateData);
    }
    private  void checkBookmarkStatus(String articleId,ImageView imageViewBookmark)
    {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("articlesInfo").child(userId).child(articleId);



        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    // Username already exists
                    // Handle the case accordinglyint position =
                    Article article1=dataSnapshot.getValue(Article.class);
                    if (article1!=null)
                    {

                        isBookmarked=article1.isBookmarked();
                        if (isBookmarked) {
                            imageViewBookmark.setImageResource(R.drawable.ic_bookmark_filled);
                        } else {
                            imageViewBookmark.setImageResource(R.drawable.ic_bookmark_unmarked);
                        }
                    }
                    else {
                        imageViewBookmark.setImageResource(R.drawable.ic_bookmark_unmarked);
                    }

                }
                else {
                    imageViewBookmark.setImageResource(R.drawable.ic_bookmark_unmarked);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // An error occurred while retrieving the data
                // Handle the error accordingly
            }
        });

    }
    private void updateFollowingStatus() {
        // Get the authenticated user's ID
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();

// Get a reference to the "articles" node in the Firebase Database
        DatabaseReference articlesRef = FirebaseDatabase.getInstance().getReference().child("usersInfo");

// Get a reference to the specific article node
        DatabaseReference articleRef = articlesRef.child(userId).child(authorId);
// Assume you have a boolean variable indicating the bookmark status
//        boolean isArticleBookmarked = true; // Replace this with the actual bookmark status

// Create a HashMap to update the bookmark status field in the database
        HashMap<String, Object> updateData = new HashMap<>();
        updateData.put("isFollowing", isFollowing);

// Use the updateChildren() method to update the bookmark status field for the specific article
        articleRef.updateChildren(updateData);
    }
    private  void checkFollowingStatus(String authorId)
    {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("usersInfo").child(userId).child(authorId);



        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    // Username already exists
                    // Handle the case accordinglyint position =
                    Article article1=dataSnapshot.getValue(Article.class);
                    if (article1!=null)
                    {

                        isFollowing=article1.isFollowing();

                        if (isFollowing) {
                            followButton.setText("Following");
                            followButton.setTextColor(R.color.black);
                            followButton.setBackgroundResource(R.drawable.rounded_button_following); // Set the custom shape drawable as the background
                        } else {
                            followButton.setText("Follow");
                            followButton.setTextColor(R.color.orange);
                            followButton.setBackgroundResource(R.drawable.rounded_button_follow); // Set the same custom shape drawable as the background
                        }


                    }
                    else {
                        followButton.setText("Follow");
                        followButton.setTextColor(R.color.orange);
                        followButton.setBackgroundResource(R.drawable.rounded_button_follow); // Set the same custom shape drawable as the background
                    }

                }
                else {
                    followButton.setText("Follow");
                    followButton.setTextColor(R.color.orange);
                    followButton.setBackgroundResource(R.drawable.rounded_button_follow); // Set the same custom shape drawable as the background
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // An error occurred while retrieving the data
                // Handle the error accordingly
            }
        });

    }
    private void updateLikeStatus() {
        // Get the authenticated user's ID
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();

// Get a reference to the "articles" node in the Firebase Database
        DatabaseReference articlesRef = FirebaseDatabase.getInstance().getReference().child("articlesInfo");

// Get a reference to the specific article node
        DatabaseReference articleRef = articlesRef.child(userId).child(articleId);
// Assume you have a boolean variable indicating the bookmark status
//        boolean isArticleBookmarked = true; // Replace this with the actual bookmark status

// Create a HashMap to update the bookmark status field in the database
        HashMap<String, Object> updateData = new HashMap<>();
        updateData.put("isLiked", isLiked);

// Use the updateChildren() method to update the bookmark status field for the specific article
        articleRef.updateChildren(updateData);
    }
    private  void checkLikeStatus()
    {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("articlesInfo").child(userId).child(articleId);



        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    // Username already exists
                    // Handle the case accordinglyint position =
                    Article article1=dataSnapshot.getValue(Article.class);
                    if (article1!=null)
                    {

                        isLiked=article1.isLiked();

                        if (isLiked) {
                            imageViewLike.setImageResource(R.drawable.thumbs_up_filled);
                            animateHeartEmpty();
                        } else {
                            imageViewLike.setImageResource(R.drawable.thumbs_up_unfilled);
                            animateHeartEmpty();
                        }


                    }
                    else {
                        imageViewLike.setImageResource(R.drawable.thumbs_up_unfilled);
                        animateHeartEmpty();
                    }

                }
                else {
                    imageViewLike.setImageResource(R.drawable.thumbs_up_unfilled);
                    animateHeartEmpty();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // An error occurred while retrieving the data
                // Handle the error accordingly

                    imageViewLike.setImageResource(R.drawable.thumbs_up_unfilled);
                    animateHeartEmpty();

            }
        });

    }


}          // Dark mode is not active