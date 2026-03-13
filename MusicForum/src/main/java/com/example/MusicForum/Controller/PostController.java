package com.example.MusicForum.Controller;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.MusicForum.Model.Album;
import com.example.MusicForum.Model.Comment;
import com.example.MusicForum.Model.Post;
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
    public String getPosts(Model model) {
        model.addAttribute("posts", postRepository.findAll());
        return "post_listing";
    }

    @GetMapping("/create/new_post")
    public String newPost(Model model) {
        model.addAttribute("post", new Post());
        model.addAttribute("albums", albumRepository.findAll());
        return "new_post";
    }

    @GetMapping("/editpost/{id}")
    public String editPost(Model model, @PathVariable long id) {
        Optional<Post> op = postRepository.findById(id);
        if (op.isPresent()) {
            Post post = op.get();
            List<Album> availableAlbums = albumRepository.findAll();
            availableAlbums.removeAll(post.getAlbums());
            model.addAttribute("post", post);
            model.addAttribute("postId", post.getID());
            model.addAttribute("allAlbums", availableAlbums);
            return "edit_post";
        } else {
            return "post_not_found";
        }
    }

    @PostMapping("/editpost/{id}")
    public String editPostPost(@PathVariable long id, Post editedPost) {
        Optional<Post> op = postRepository.findById(id);
        if (op.isPresent()) {
            Post existing = op.get();
            existing.setTitle(editedPost.getTitle());
            existing.setDescription(editedPost.getDescription());
            postRepository.save(existing);
            return "redirect:/post_listing";
        } else {
            return "post_not_found";
        }
    }

    @PostMapping("/editpost/{id}/addAlbum/{albumId}")
    public String addAlbum(@PathVariable long id, @PathVariable long albumId) {
        Optional<Post> op = postRepository.findById(id);
        Optional<Album> albumOp = albumRepository.findById(albumId);
        if (op.isPresent() && albumOp.isPresent()) {
            Post post = op.get();
            if (!post.getAlbums().contains(albumOp.get())) {
                post.getAlbums().add(albumOp.get());
                postRepository.save(post);
            }
        }
        return "redirect:/editpost/" + id;
    }

    @PostMapping("/editpost/{id}/removeAlbum/{albumId}")
    public String removeAlbum(@PathVariable long id, @PathVariable long albumId) {
        Optional<Post> op = postRepository.findById(id);
        Optional<Album> albumOp = albumRepository.findById(albumId);
        if (op.isPresent() && albumOp.isPresent()) {
            Post post = op.get();
            post.getAlbums().remove(albumOp.get());
            postRepository.save(post);
        }
        return "redirect:/editpost/" + id;
    }

    @PostMapping("/post/new_post")
    public String newPost(Post post, @RequestParam(required = false) List<Long> albumIds) {
        if (albumIds != null) {
            List<Album> selectedAlbums = albumRepository.findAllById(albumIds);
            post.setAlbums(selectedAlbums);
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
        comment.setPost(post);
        commentRepository.save(comment);
        return "redirect:/post/" + postId;
    }

    @PostMapping("/post/{postId}/comments/{commentId}/delete")
    public String deleteComment(@PathVariable Long postId, @PathVariable Long commentId) {
        commentRepository.deleteById(commentId);
        return "redirect:/post/" + postId;
    }
}