package com.example.MusicForum.Controller;


import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.MusicForum.Model.Post;
import com.example.MusicForum.Repository.PostRepository;


@Controller

public class PostController {

    @Autowired
    private PostRepository postRepository;

    @GetMapping("/post_listing")
        public String getPosts(Model model){
            model.addAttribute("posts",postRepository.findAll());
            return "post_listing";
        }

    @GetMapping("/create/new_post")
    public String newPost(Model model) {
          model.addAttribute("post", new Post());
        return "new_post"; 
    }
   @PostMapping("/post/new_post")
public String newPost(Post post) {
    postRepository.save(post);
    // REDIRECT indica al navegador que vaya a la URL /post_listing (ejecutando el @GetMapping)
    return "redirect:/post_listing";
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
	




