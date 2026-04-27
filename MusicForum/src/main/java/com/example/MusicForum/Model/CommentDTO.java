package com.example.MusicForum.Model;

public class CommentDTO {

    private Long id;
    private String comment;
    private String authorUsername;
    private Long postId;

    // Constructor vacío
    public CommentDTO() {}

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public String getAuthorUsername() { return authorUsername; }
    public void setAuthorUsername(String authorUsername) { this.authorUsername = authorUsername; }

    public Long getPostId() { return postId; }
    public void setPostId(Long postId) { this.postId = postId; }
}