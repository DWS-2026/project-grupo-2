package com.example.MusicForum.Service;

import com.example.MusicForum.Model.Comment;
import com.example.MusicForum.Model.Post;
import com.example.MusicForum.Repository.PostRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataBaseInit {

    @Autowired
    private PostRepository postRepository;

    @PostConstruct
    public void init() {
        // Inicialización de datos de prueba
        Post post1 = new Post("Post1", "2024-01-01", "Descripción del post 1", "/images/rosalia-lux.jpg", null);
        Post post2 = new Post("Post2", "2024-01-02", "Descripción del post 2", "/images/Geese-Getting-Killed.jpg", null);
        Post post3 = new Post("Post3", "2024-01-03", "Descripción del post 3", "image3.jpg", null);

        post1.addComment(new Comment("Comentario 1"));
        post2.addComment(new Comment("Comentario 2"));
        post3.addComment(new Comment("Comentario 3"));

        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);
        
        System.out.println("Datos cargados correctamente por DataPopulatorService");
    }
}
