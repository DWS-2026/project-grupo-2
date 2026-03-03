package com.example.MusicForum.Controller;


import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.MusicForum.Model.Comment;
import com.example.MusicForum.Model.Post;
import com.example.MusicForum.Repository.CommentRepository;
import com.example.MusicForum.Repository.PostRepository;


@Controller

public class PostController {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;

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
    return "redirect:/post_listing"; //redirije a la lista de posts después de crear uno nuevo
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

    @PostMapping("/post/{id}/delete")
	public String deletePost(@PathVariable long id) {
		Optional<Post> post = postRepository.findById(id);
		if (post.isPresent()) {
			postRepository.deleteById(id);
			return "redirect:/post_listing";
		} else {
			return "post_not_found";
		}
	}

    
	@PostMapping("/post/{postId}/comments/new")
public String newComment(@PathVariable long postId, Comment comment) {
    Post post = postRepository.findById(postId).orElseThrow();
    comment.setPost(post);
    commentRepository.save(comment);
    // CORRECTO: Redirige a la URL /post/{id}
    return "redirect:/post/" + postId; 
}

@PostMapping("/post/{postId}/comments/{commentId}/delete")
public String deleteComment(@PathVariable Long postId, @PathVariable Long commentId) {
    // Es mejor borrar directamente por ID
    commentRepository.deleteById(commentId);
    // CORRECTO: Redirige a la URL /post/{id}
    return "redirect:/post/" + postId;
}




}
	




