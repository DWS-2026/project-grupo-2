package com.example.MusicForum.Model;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "Album")

public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;

    private String date;
    private String artist;

    @Lob
    private Blob imageBlob;

    @ElementCollection // <-- Mandatory for List<String>
    private List<String> songs;

    @ManyToMany(mappedBy = "album")
    private List<Post> posts = new ArrayList<>();

    protected Album() {
    }

    public Album(String title, List<String> songs, String date, String artist) {
        this.title = title;
        this.date = date;
        this.songs = songs;

        this.artist = artist;
    }

    public void setImageBlob(Blob image) {
        this.imageBlob = image;
    }

    public Blob getImageBlob() {
        return imageBlob;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public List<String> getSongs() {
        return songs;
    }

    public String getArtist() {
        return artist;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSongs(List<String> songs) {
        this.songs = songs;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

}