package com.example.MusicForum.Model;

import java.util.ArrayList;
import java.util.List;

public class Post {
    private Long ID;
    private String title;
    private User user;
    private String description;
    private String image;
    private List<Comments> comments=new ArrayList<>();

    public Post(){}


    public Post(Long ID,String title,String description,String image,User user){
        this.ID=ID;
        this.title=title;
        this.description=description;
        this.image=image;
        this.user=user;
    }

    public Long getID() { 
        return ID; 
    }
    public Long setID(Long ID) { 
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
}
