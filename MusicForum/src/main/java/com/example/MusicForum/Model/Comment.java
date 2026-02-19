package com.example.MusicForum.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(columnDefinition = "TEXT") // Permite comentarios largos
    private String comment;

    @ManyToOne
    @JoinColumn(name = "user_id") // Nombre de la columna en la tabla de la DB
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id") // Relación con el Post
    private Post post;

    // Constructor vacío (Obligatorio para JPA)
    public Comment() {}

    // Constructor con parámetros
    public Comment( String comment) {
        super();
        this.comment = comment;
    }

    // --- GETTERS Y SETTERS ---

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
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
}
