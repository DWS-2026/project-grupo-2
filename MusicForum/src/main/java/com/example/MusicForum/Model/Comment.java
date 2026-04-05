package com.example.MusicForum.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT") // Large comments
    private String comment;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = true) // Name of the table that is connected
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id") // Relation with posts
    private Post post;
    @Transient
    private boolean isCurrentUser;
    @Transient
    private boolean canDelete;

    // Void Constrtuct (Mandatory for JPA)
    public Comment() {
    }

    // Constructor with parameters
    public Comment(String comment, User user) {
        super();
        this.user = user;
        this.comment = comment;
    }

    // --- GETTERS Y SETTERS ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public boolean isIsCurrentUser() { // The user now can delete their comments
        return isCurrentUser;
    }

    public void setIsCurrentUser(boolean isCurrentUser) {
        this.isCurrentUser = isCurrentUser;
    }

    public boolean isCanDelete() {
        return canDelete;
    }

    public void setCanDelete(boolean canDelete) {
        this.canDelete = canDelete;
    }

}
