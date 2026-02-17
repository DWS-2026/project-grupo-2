package com.example.MusicForum.Model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    private String image;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Un Post tiene muchos comentarios
    // mappedBy debe coincidir con el nombre del atributo "post" en la clase Comment
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    // Un Post tiene muchos álbumes (asumiendo que en la clase Albums hay un atributo 'post')
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Albums> albums = new ArrayList<>();

    // Constructor vacío obligatorio para JPA
    public Post() {}

    // Constructor con parámetros
    public Post(Long ID, String title, String description, String image, User user) {
        this.ID = ID;
        this.title = title;
        this.description = description;
        this.image = image;
        this.user = user;
    }

    // --- GETTERS Y SETTERS ---

    public Long getID() { 
        return ID; 
    }

    public void setID(Long ID) { // Corregido: antes tenías Long de retorno en lugar de void
        this.ID = ID; 
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

    public String getImage() {
        return image;
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

    public List<Albums> getAlbums() {
        return albums;
    }

    public void setAlbums(List<Albums> albums) {
        this.albums = albums;
    }
}
