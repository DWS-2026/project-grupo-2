package com.example.MusicForum.Service;

import com.example.MusicForum.Model.Album;
import com.example.MusicForum.Model.Comment;
import com.example.MusicForum.Model.Post;
import com.example.MusicForum.Repository.AlbumRepository;
import com.example.MusicForum.Repository.PostRepository;
import jakarta.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataBaseInit {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private AlbumRepository albumRepository;
    @PostConstruct
public void init() {
    List<String> songs1 = Arrays.asList("953", "Speedway", "Reggae", "Near DT, MI", "Western", "Of Schlagenheim", "bmbmbm", "Years Ago", "Ducter");
    List<String> songs2 = Arrays.asList("Berghain","La perla", "La Rumba del Perdón","Memoria","Magnolias");
    Album album1 = new Album("Schlagenheim", songs1, "/images/Geese-Getting-Killed.jpg", "21-06-2019", "black midi");
    Album album2 = new Album("Berghain", songs2, "/images/Berghain.jpg", "15-03-2020", "black midi");

    
    albumRepository.save(album1); 
    albumRepository.save(album2); 

   
    // Creamos los posts (la imagen se asigna sola internamente a "schlagenheim.png")
    Post post1 = new Post("Post1", "2024-01-01", "Descripción 1", null);
    Post post2 = new Post("Post2", "2024-01-02", "Descripción 2", null);
    Post post3 = new Post("Post3", "2024-01-03", "Descripción 3", null);

    post1.addComment(new Comment("Comentario 1"));

    post1.getAlbums().add(album1);
    post1.getAlbums().add(album2);
    post2.getAlbums().add(album2);


    
    // Guardar todo
    postRepository.saveAll(Arrays.asList(post1, post2, post3));
}
}
