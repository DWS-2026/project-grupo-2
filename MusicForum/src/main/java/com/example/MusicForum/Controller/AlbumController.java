package com.example.MusicForum.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.example.MusicForum.Repository.AlbumRepository;

@Controller
public class AlbumController {
    @Autowired
    private AlbumRepository repository;

    @AlbumConstruct
    public void init(){}
}
