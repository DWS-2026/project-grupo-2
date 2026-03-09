package com.example.MusicForum.Model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    private String image;

    private String date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Un Post tiene muchos comentarios
    // mappedBy debe coincidir con el nombre del atributo "post" en la clase Comment
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    // one post can have many albums, and one album can be in many posts
    @ManyToMany
    private List<Album> album = new ArrayList<>();

    // Constructor vacío obligatorio para JPA
    public Post() {}

    // Constructor con parámetros
   public Post(String title, String date, String description, User user) {
        this.title = title;
        this.date = date;
        this.description = description;
        this.user = user;
    }

    // --- GETTERS Y SETTERS ---

    public Long getID() { 
        return id; 
    }

    public void setID(Long id) {
        this.id = id; 
    }

    public String getTitle() { 
        return title; 
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

      public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setPost(this);
    }
 
    public void removeComment(Comment comment) {
        comments.remove(comment);
        comment.setPost(null);
    }

    public List<Album> getAlbums() {
        return album;
    }

    public void setAlbums(List<Album> albums) {
    this.album = albums;
    if (albums != null && !albums.isEmpty()) {
        this.image = albums.get(0).getImage(); //we set the post image to the first album's image
    }
    }
    public String getImage() {
    if (album != null && !album.isEmpty()) {
        return album.get(0).getImage();
    }
    return image; // fallback to stored image if no albums
    }
    
}
