package com.example.MusicForum.Model;

import java.util.List;


import jakarta.persistence.*;

@Entity
@Table(name = "Album")

public class Album{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private List<String> songs;
    private String image;
    private String date;
    private String artist;

    protected Album(){}

    public Album(String title, List<String> songs, String image, String date, String artist){
        this.title = title;
        this.date = date;
        this.songs = songs;
        this.image = image;
        this.artist = artist;
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

    public String getImage() { 
        return image; 
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
    public void setImage(String image) {
        this.image = image;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setArtist(String artist) {
        this.artist = artist;
    }
    @ManyToMany (mappedBy = "album")
    private List<Post> posts;
    
}