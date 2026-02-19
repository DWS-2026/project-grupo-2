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

    // Un Post tiene muchos álbumes (asumiendo que en la clase Albums hay un atributo 'post')
    // Cambia el @OneToMany por @ManyToMany
    
    
    /*@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "post_albums", // Nombre de la tabla intermedia
        joinColumns = @JoinColumn(name = "post_id"),
        inverseJoinColumns = @JoinColumn(name = "album_id")
    )
    private List<Albums> albums = new ArrayList<>();*/

    // Constructor vacío obligatorio para JPA
    public Post() {}

    // Constructor con parámetros
    public Post(String title,String date, String description, String image, User user) {
        this.title = title;
        this.date = date;
        this.description = description;
        this.image = image;
        this.user = user;
    }

    // --- GETTERS Y SETTERS ---

    public Long getID() { 
        return id; 
    }

    public void setID(Long ID) { // Corregido: antes tenías Long de retorno en lugar de void
        this.id = ID; 
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

    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setPost(this);
    }
 
    public void removeComment(Comment comment) {
        comments.remove(comment);
        comment.setPost(null);
    }

    /*public List<Albums> getAlbums() {
        return albums;
    }

    public void setAlbums(List<Albums> albums) {
        this.albums = albums;
    }*/
}
