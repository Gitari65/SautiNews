package com.example.sautinews;

import android.content.Context;
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

        // Bind the data to the views in the ViewHolder
        holder.textViewTitle.setText(article.getArticleTitle());
        holder.textViewAuthorName.setText(article.getAuthor());
        String timestamp= String.valueOf(article.getTimestamp());

        holder.textViewTime.setText(getFormattedTimestamp(timestamp));
       String imageUrl=article.getCoverPicUrl();

        Picasso.get()
                .load(imageUrl).placeholder(R.drawable.news_icon3)
                .error(R.drawable.news_icon3)  // Set the error placeholder image resource
                .into(holder.imageViewCoverPic);



        // Set the images using appropriate methods based on your implementation
        // holder.imageViewCoverPic.setImageURI(article.getCoverPicUri());
        // holder.imageViewBookmark.setImageResource(article.isBookmarked() ? R.drawable.bookmark_selected : R.drawable.bookmark_unselected);
        // holder.imageViewAuthorPic.setImageResource(article.getAuthorPicResId());
    }
    private String getFormattedTimestamp(String timestamp) {
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
        if (days > 0) {
            // More than a day ago
            return days + " days ago";
        } else if (hours > 0) {
            // More than an hour ago
            return hours + " hrs ago";
        } else if (minutes > 0) {
            // More than a minute ago
            return minutes + " mins ago";
        } else {
            // Less than a minute ago or just a few seconds ago
            return "last few seconds ago";
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
        }
    }
}
