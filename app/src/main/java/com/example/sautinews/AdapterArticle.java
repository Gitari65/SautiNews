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

import com.squareup.picasso.Picasso;

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

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        Article article = articles.get(position);
        articleTitle=article.getArticleTitle();
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
                        Intent intent = new Intent(context.getApplicationContext(), ReadNewsActivity.class);
                        intent.putExtra("ArticleId", articleId);
                        Log.i(TAG, "onClick: articleid" + articleId);
                        context.startActivity(intent);
                    }
                }
            });

        }
    }
}
