package com.example.sautinews;

import java.sql.Time;
import java.sql.Timestamp;

public class Article {
    public String getCoverPicUrl() {
        return CoverPicUrl;
    }

    public void setCoverPicUrl(String coverPicUrl) {
        CoverPicUrl = coverPicUrl;
    }

    private String CoverPicUrl;
    private String ArticleTitle;

    public String getArticleTitle() {
        return ArticleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        ArticleTitle = articleTitle;
    }

    public String getArticleContent() {
        return ArticleContent;
    }

    public void setArticleContent(String articleContent) {
        ArticleContent = articleContent;
    }

    private String ArticleContent;
    private String ArticleId;

    public String getArticleId() {
        return ArticleId;
    }

    public void setArticleId(String articleId) {
        ArticleId = articleId;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }



    public int getViews() {
        return Views;
    }

    public void setViews(int views) {
        Views = views;
    }

    public int getLikes() {
        return Likes;
    }

    public void setLikes(int likes) {
        Likes = likes;
    }

    public String getComments() {
        return Comments;
    }

    public void setComments(String comments) {
        Comments = comments;
    }

    private String Author;

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    private Long timestamp;
    private int Views;
    private int Likes;
    private String Comments;


}
