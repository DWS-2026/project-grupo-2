package com.example.MusicForum.Controller;


import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.MusicForum.Model.Album;
import com.example.MusicForum.Model.Comment;
import com.example.MusicForum.Model.Post;
import com.example.MusicForum.Model.User;
import com.example.MusicForum.Repository.AlbumRepository;
import com.example.MusicForum.Repository.CommentRepository;
import com.example.MusicForum.Repository.PostRepository;


@Controller

public class PostController {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private AlbumRepository albumRepository;

    @GetMapping("/post_listing")
        public String getPosts(Model model){
            model.addAttribute("posts",postRepository.findAll());
            return "post_listing";
        }

    @GetMapping("/create/new_post")
    public String newPost(Model model) {
          model.addAttribute("post", new Post());
          model.addAttribute("albums", albumRepository.findAll());
        return "new_post"; 
    }
   @PostMapping("/post/new_post")
    public String newPost(Post post, @RequestParam(required = false) List<Long> albumIds) {

    if (albumIds != null) {
        List<Album> selectedAlbums = albumRepository.findAllById(albumIds);
        post.setAlbums(selectedAlbums); // this also sets the image automatically
    }
    postRepository.save(post);
    return "redirect:/post_listing"; 
    }

    @GetMapping("/post/{id}")
	public String getPost(Model model, @PathVariable long id) {
	Optional<Post> post = postRepository.findById(id);
	if (post.isPresent()) {
		model.addAttribute("post", post.get());
        model.addAttribute("albums", post.get().getAlbums());
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

    // Buscas el usuario con ID 1 (debes haberlo creado previamente)
    //User defaultUser = userRepository.findById(1L).orElse(null); 

    comment.setPost(post);
    //comment.setUser(defaultUser); // Asignas el usuario manualmente

    commentRepository.save(comment);
    return "redirect:/post/" + postId; 
}

@PostMapping("/post/{postId}/comments/{commentId}/delete")
public String deleteComment(@PathVariable Long postId, @PathVariable Long commentId) {
    commentRepository.deleteById(commentId);
    return "redirect:/post/" + postId;
}




}
	




