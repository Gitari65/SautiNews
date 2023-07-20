package com.example.sautinews;

import java.sql.Time;
import java.sql.Timestamp;
public class Article {
    private String articleId;
    private String articleTitle;
    private String articleContent;
    private String coverPicUrl;
    private String authorId;
    private String authorFullName;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    private String fullName;
    private Long timestamp;
    private int views;
    private int likes;
    private String comments;

    public Article() {
        // Default constructor required for Firebase
    }

    public Article(String articleId, String articleTitle, String articleContent, String coverPicUrl, String authorId, String authorFullName, Long timestamp, int views, int likes, String comments) {
        this.articleId = articleId;
        this.articleTitle = articleTitle;
        this.articleContent = articleContent;
        this.coverPicUrl = coverPicUrl;
        this.authorId = authorId;
        this.authorFullName = authorFullName;
        this.timestamp = timestamp;
        this.views = views;
        this.likes = likes;
        this.comments = comments;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public String getArticleContent() {
        return articleContent;
    }

    public void setArticleContent(String articleContent) {
        this.articleContent = articleContent;
    }

    public String getCoverPicUrl() {
        return coverPicUrl;
    }

    public void setCoverPicUrl(String coverPicUrl) {
        this.coverPicUrl = coverPicUrl;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getAuthorFullName() {
        return authorFullName;
    }

    public void setAuthorFullName(String authorFullName) {
        this.authorFullName = authorFullName;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
