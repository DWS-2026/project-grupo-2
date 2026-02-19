package com.example.MusicForum.Controller;


import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.MusicForum.Model.Comment;
import com.example.MusicForum.Model.Post;
import com.example.MusicForum.Repository.PostRepository;
import com.example.MusicForum.Service.PostService;

import jakarta.annotation.PostConstruct;


@Controller

public class PostController {

    @Autowired
    private PostRepository postRepository;

    @PostConstruct
    public void init() {
        // Aquí puedes inicializar datos de prueba o realizar cualquier configuración necesaria
        Post post1 = new Post("Post1", "2024-01-01", "Descripción del post 1", "/images/rosalia-lux.jpg", null);  //String title,String date, String description, String image, User user
        Post post2 = new Post("Post2", "2024-01-02", "Descripción del post 2", "/images/Geese-Getting-Killed.jpg", null);
        Post post3 = new Post("Post3", "2024-01-03", "Descripción del post 3", "image3.jpg", null);
        post1.addComment(new Comment("Comentario 1"));
        post2.addComment(new Comment("Comentario 2"));
        post3.addComment(new Comment("Comentario 3"));
       
        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);

    
    }

    @GetMapping("/post_listing")
        public String getPosts(Model model){
            model.addAttribute("posts",postRepository.findAll());
            return "post_listing";
        }
    @GetMapping("/post/{id}")
	public String getPost(Model model, @PathVariable long id) {
	Optional<Post> post = postRepository.findById(id);
	if (post.isPresent()) {
		model.addAttribute("post", post.get());
		return "post_view";
	} else {
		return "post_not_found";
	}
	}




}