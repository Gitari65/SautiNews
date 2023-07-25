package com.example.sautinews;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

public class AdapterArticle extends RecyclerView.Adapter<AdapterArticle.ArticleViewHolder> {

    private List<Article> articles;
    private Context context;
    private String articleTitle,articleContent,articleTime,AuthorId,CoverPicUrl,AuthorName,time,articleId;


    public AdapterArticle(List<Article> articles, Context context) {
        this.articles = articles;
        this.context = context;
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_layout, parent, false);
        return new ArticleViewHolder(view);
    }
Article article;
    boolean isBookmarked=false;
    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        article = articles.get(position);
        articleTitle=article.getArticleTitle();
        articleId=article.getArticleId();


        // Bind the data to the views in the ViewHolder
        holder.textViewTitle.setText(article.getArticleTitle());
        holder.textViewAuthorName.setText(article.getAuthorFullName());
        String timestamp= String.valueOf(article.getTimestamp());

        holder.textViewTime.setText(getFormattedTimestamp(timestamp));
       String imageUrl=article.getCoverPicUrl();

        Picasso.get()
                .load(imageUrl).placeholder(R.drawable.news_icon3)
                .error(R.drawable.news_icon3)  // Set the error placeholder image resource
                .into(holder.imageViewCoverPic);

articleId=article.getArticleId();
        Log.i(TAG, "onClick: article"+articleId);
        // Set the bookmark icon state based on the 'isArticleBookmarked' field of the Article class
        holder.isArticleBookmarked = article.isBookmarked(); // Use the appropriate method to get the bookmark state
        if (holder.isArticleBookmarked) {
            holder.imageViewBookmark.setImageResource(R.drawable.ic_bookmark_filled);
        } else {
            holder.imageViewBookmark.setImageResource(R.drawable.ic_bookmark_unmarked);
        }
        int adapterPosition = holder.getAdapterPosition();
        checkBookmarkStatus(articleId,holder.imageViewBookmark,adapterPosition);
        // Add an OnClickListener to the bookmark icon
        holder.imageViewBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toggle the bookmark state
                int adapterPosition = holder.getAdapterPosition(); // Get the correct position
                if (adapterPosition != RecyclerView.NO_POSITION) {

                    holder.isArticleBookmarked = !holder.isArticleBookmarked;

                    // Update the bookmark icon
                    if (holder.isArticleBookmarked) {
                        holder.imageViewBookmark.setImageResource(R.drawable.ic_bookmark_filled);
                        updateBookmarkStatus(article.getArticleId(), true,adapterPosition); // Update bookmark status in the database
                    } else {
                        holder.imageViewBookmark.setImageResource(R.drawable.ic_bookmark_unmarked);
                        updateBookmarkStatus(article.getArticleId(), false,adapterPosition); // Update bookmark status in the database
                    }
                }

            }
        });
    }

    // ... other methods ...

    // Method to update the bookmark status in the Firebase Database
    private void updateBookmarkStatus(String articleId, boolean isBookmarked,int position) {
        // Get the authenticated user's ID
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();

// Get a reference to the "articles" node in the Firebase Database
        DatabaseReference articlesRef = FirebaseDatabase.getInstance().getReference().child("articlesInfo");

// Get the specific article ID you want to update the bookmark status for
        Article article=articles.get(position);
        // Replace this with the actual article ID

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
    private  void checkBookmarkStatus(String articleId,ImageView imageViewBookmark,int position)
    {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();


        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("articlesInfo").child(userId).child(articleId);



        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    // Username already exists
                    // Handle the case accordinglyint position =
                    Article article1=articles.get(position);
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
    @Override
    public int getItemCount() {
        return articles.size();
    }

    public class ArticleViewHolder extends RecyclerView.ViewHolder {
        public boolean isArticleBookmarked=false;
        ImageView imageViewCoverPic;
        ImageView imageViewBookmark;
        TextView textViewTitle;
        TextView textViewAuthorName;
        TextView textViewTime;

        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewCoverPic = itemView.findViewById(R.id.imageViewCoverPic);
            imageViewBookmark = itemView.findViewById(R.id.imageViewBookmark);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewAuthorName = itemView.findViewById(R.id.textViewAuthorName);
            textViewTime = itemView.findViewById(R.id.textViewTime);



            // Set any click listeners or other functionality here
            imageViewCoverPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Article article = articles.get(position);
                        String articleId = article.getArticleId();
                        String authorId=article.getAuthorId();
                        Intent intent = new Intent(context.getApplicationContext(), ReadNewsActivity.class);
                        intent.putExtra("ArticleId", articleId);
                        intent.putExtra("AuthorId", authorId);
                        Log.i(TAG, "onClick: articleid" + articleId);
                        context.startActivity(intent);
                        updateViewedStatus(articleId,position);
                    }
                }
            });

        }
        private void updateViewedStatus(String articleId,int position) {
            // Get the authenticated user's ID
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();

// Get a reference to the "articles" node in the Firebase Database
            DatabaseReference articlesRef = FirebaseDatabase.getInstance().getReference().child("articlesInfo");

// Get the specific article ID you want to update the bookmark status for
            Article article=articles.get(position);
            // Replace this with the actual article ID

// Get a reference to the specific article node
            DatabaseReference articleRef = articlesRef.child(userId).child(articleId);
// Assume you have a boolean variable indicating the bookmark status
            boolean isViewed = true; // Replace this with the actual bookmark status

// Create a HashMap to update the bookmark status field in the database
            HashMap<String, Object> updateData = new HashMap<>();
            updateData.put("isViewed", isViewed);

// Use the updateChildren() method to update the bookmark status field for the specific article
            articleRef.updateChildren(updateData);
        }
    }
}
