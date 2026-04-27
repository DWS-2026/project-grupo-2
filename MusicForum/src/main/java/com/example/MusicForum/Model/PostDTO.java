package com.example.MusicForum.Model;

import java.util.List;

public class PostDTO {

    private Long id;
    private String title;
    private String description;
    private String date;
    private String authorUsername;
    private boolean hasImage;
    private List<String> albumTitles;
    private int commentCount;

    // Constructor vacío
    public PostDTO() {}

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getAuthorUsername() { return authorUsername; }
    public void setAuthorUsername(String authorUsername) { this.authorUsername = authorUsername; }

    public boolean isHasImage() { return hasImage; }
    public void setHasImage(boolean hasImage) { this.hasImage = hasImage; }

    public List<String> getAlbumTitles() { return albumTitles; }
    public void setAlbumTitles(List<String> albumTitles) { this.albumTitles = albumTitles; }

    public int getCommentCount() { return commentCount; }
    public void setCommentCount(int commentCount) { this.commentCount = commentCount; }
}