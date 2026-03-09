package com.example.MusicForum.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.MusicForum.Model.Post;
import com.example.MusicForum.Repository.AlbumRepository;

@Controller
public class AlbumController {
    @Autowired
    private AlbumRepository repository;

    @Autowired
    private AlbumRepository albumRepository;




    //@AlbumConstruct
    //public void init(){}
}
