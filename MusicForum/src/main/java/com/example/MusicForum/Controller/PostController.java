package com.example.MusicForum.Controller;

import java.io.IOException;
import java.security.Principal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.*;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.MusicForum.Model.Album;
import com.example.MusicForum.Service.*;
import com.example.MusicForum.Utils.HtmlSanitizer;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

import com.example.MusicForum.Model.Comment;
import com.example.MusicForum.Model.Post;
import com.example.MusicForum.Model.User;
import com.example.MusicForum.Repository.AlbumRepository;
import com.example.MusicForum.Repository.CommentRepository;
import com.example.MusicForum.Repository.PostRepository;
import com.example.MusicForum.Repository.UserRepository;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class PostController {
    @Autowired
    private HtmlSanitizer htmlSanitizer;

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private AlbumRepository albumRepository;
    @Autowired
    private PostService postService;
    @Autowired
    private UserRepository userRepository;

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

    @PostMapping("/post/new_post")
    public String newPost(Post post, @RequestParam("imageFile") MultipartFile imageFile,
            @RequestParam(required = false) List<Long> albumIds, Principal principal)
            throws IOException {
        if (albumIds != null) {
            List<Album> selectedAlbums = albumRepository.findAllById(albumIds);
            post.setAlbums(selectedAlbums);
        }

        post.setDate(LocalDate.now().toString());
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        post.setUser(user);
        post.setDescription(htmlSanitizer.sanitize(post.getDescription()));
        postService.save(post, imageFile);
        return "redirect:/post_listing";
    }

    @GetMapping("/post/{id}")
    public String getPost(Model model, @PathVariable long id, Principal principal, HttpServletRequest request) {
        Optional<Post> postOp = postRepository.findById(id);

        if (postOp.isPresent()) {
            Post post = postOp.get();
            boolean isAdmin = request.isUserInRole("ADMIN");

            if (principal != null) {
                String currentUsername = principal.getName();
                for (Comment comment : post.getComments()) {
                    // appea¡¡
                    boolean isOwner = comment.getUser().getUsername().equals(currentUsername);
                    comment.setCanDelete(isOwner || isAdmin);
                }
                model.addAttribute("loggedUser", true);
            }

            model.addAttribute("post", post);
            model.addAttribute("isAdmin", isAdmin); // admin can view only
            return "post_view";
        }
        return "post_not_found";
    }

    @Transactional
    @PostMapping("/post/{id}/delete")
    public String deletePost(@PathVariable long id, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        Optional<Post> op = postRepository.findById(id);
        if (op.isPresent()) {
            Post postToDelete = op.get();

            User loggedUser = userRepository.findByUsername(principal.getName()).orElseThrow();

            boolean isAuthor = postToDelete.getUser() != null && postToDelete.getUser().getId().equals(loggedUser.getId());
            boolean isAdmin = loggedUser.getRoles().contains("ADMIN");

            if (!isAuthor && !isAdmin) {
                return "redirect:/access_denied";
            }

            postRepository.deleteAlbumRelations(id);
            postRepository.deleteComments(id);
            postRepository.deletePostById(id);
            return "redirect:/post_listing";
        }
        
        return "error";
    }

    @GetMapping("/editpost/{id}")
    public String editPost(Model model, @PathVariable long id, Principal principal) {
        
        if (principal == null) {
            return "redirect:/login";
        }

        Optional<Post> op = postRepository.findById(id);
        if (op.isPresent()) {
            Post post = op.get();

            User loggedUser = userRepository.findByUsername(principal.getName()).orElseThrow();

            boolean isAuthor = post.getUser() != null && post.getUser().getId().equals(loggedUser.getId());
            boolean isAdmin = loggedUser.getRoles().contains("ADMIN");

            if (!isAuthor && !isAdmin) {
                return "redirect:/access_denied";
            }

            List<Album> availableAlbums = albumRepository.findAll();
            availableAlbums.removeAll(post.getAlbums());
            
            model.addAttribute("post", post);
            model.addAttribute("postId", post.getId());
            model.addAttribute("allAlbums", availableAlbums);
            
            return "edit_post";
        } else {
            return "post_not_found";
        }
    }

    @GetMapping("/posts/{id}/image")
    public ResponseEntity<Object> downloadImage(@PathVariable long id) throws SQLException {
        Optional<Post> op = postRepository.findById(id);
        if (op.isPresent() && op.get().getImageFile() != null) {
            Blob image = op.get().getImageFile();
            Resource imageFile = new InputStreamResource(image.getBinaryStream());
            MediaType mediaType = MediaTypeFactory
                    .getMediaType(imageFile)
                    .orElse(MediaType.IMAGE_JPEG);
            return ResponseEntity
                    .ok()
                    .contentType(mediaType)
                    .body(imageFile);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/editpost/{id}")
    public String editPostPost(@PathVariable long id, Post editedPost,
            @RequestParam("imageFile") MultipartFile imageFile, Principal principal) throws SQLException, IOException {
        
        if (principal == null) {
            return "redirect:/login";
        }

        Optional<Post> op = postRepository.findById(id);
        if (op.isPresent()) {
            Post existing = op.get();

            User loggedUser = userRepository.findByUsername(principal.getName()).orElseThrow();

            boolean isAuthor = existing.getUser() != null && existing.getUser().getId().equals(loggedUser.getId());
            boolean isAdmin = loggedUser.getRoles().contains("ADMIN");

            if (!isAuthor && !isAdmin) {
                return "redirect:/access_denied"; 
            }

            existing.setTitle(editedPost.getTitle());
            existing.setDescription(htmlSanitizer.sanitize(editedPost.getDescription()));
            if (imageFile != null && !imageFile.isEmpty()) {
                postService.save(existing, imageFile);
            } else {
                postRepository.save(existing);
            }

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

    @PostMapping("/post/{postId}/comments/new")
    public String newComment(@PathVariable long postId,
            @RequestParam String comment,
            Principal principal) {
        Post post = postRepository.findById(postId).orElseThrow();
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();

        Comment newComment = new Comment(comment, user);
        newComment.setPost(post);
        commentRepository.save(newComment);

        return "redirect:/post/" + postId;
    }

    @PostMapping("/post/{postId}/comments/{commentId}/delete")
    public String deleteComment(@PathVariable Long postId, @PathVariable Long commentId,
            Principal principal, HttpServletRequest request) {
        Comment comment = commentRepository.findById(commentId).orElseThrow();

        boolean isOwner = comment.getUser().getUsername().equals(principal.getName());
        boolean isAdmin = request.isUserInRole("ADMIN");

        if (isOwner || isAdmin) {
            commentRepository.deleteById(commentId);
        }

        return "redirect:/post/" + postId;
    }
}