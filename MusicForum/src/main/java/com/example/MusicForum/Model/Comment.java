package com.example.MusicForum.Model;




public class Comment{
    private Long ID;
    private String comment;
    private User user;
    private Post post;

    public Comment(){}

    public Comment(Long ID,String comment,User user, Post post){
        this.ID=ID;
        this.comment=comment;
        this.user=user;
        this.post=post;
    }

}