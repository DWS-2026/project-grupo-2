package com.example.MusicForum.Controller;

import com.example.MusicForum.Model.PostDTO;
import com.example.MusicForum.Model.CommentDTO;

import com.example.MusicForum.Model.Album;
import com.example.MusicForum.Model.Comment;
import com.example.MusicForum.Model.Post;
import com.example.MusicForum.Repository.CommentRepository;
import com.example.MusicForum.Repository.PostRepository;
import com.example.MusicForum.Service.PostService;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts")
public class PostRestController {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostService postService;

    // Método privado para convertir Post -> PostDTO
    private PostDTO toDTO(Post post) {
        PostDTO dto = new PostDTO();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setDescription(post.getDescription());
        dto.setDate(post.getDate());
        dto.setAuthorUsername(post.getUser().getUsername());
        dto.setHasImage(post.getImageFile() != null);
        dto.setCommentCount(post.getComments().size());
        dto.setAlbumTitles(
            post.getAlbums().stream()
                .map(Album::getTitle)   // ajusta al getter real de Album
                .collect(Collectors.toList())
        );
        return dto;
    }

    // GET /api/posts  → lista todos los posts
    @GetMapping
    public ResponseEntity<List<PostDTO>> getAllPosts() {
        List<PostDTO> posts = postRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(posts);
    }

    // GET /api/posts/{id}  → detalle de un post
    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getPost(@PathVariable long id) {
        Optional<Post> op = postRepository.findById(id);
        return op.map(post -> ResponseEntity.ok(toDTO(post)))
                 .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/posts/{id}
    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable long id) {
        if (!postRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        postRepository.deleteAlbumRelations(id);
        postRepository.deleteComments(id);
        postRepository.deletePostById(id);
        return ResponseEntity.noContent().build();  // 204
    }


    @Autowired
private CommentRepository commentRepository;

// Método privado para convertir Comment -> CommentDTO
private CommentDTO toCommentDTO(Comment comment) {
    CommentDTO dto = new CommentDTO();
    dto.setId(comment.getId());
    dto.setComment(comment.getComment());
    dto.setAuthorUsername(comment.getUser().getUsername());
    dto.setPostId(comment.getPost().getId());
    return dto;
}

// GET /api/posts/1/comments → todos los comentarios de un post
@GetMapping("/{postId}/comments")
public ResponseEntity<List<CommentDTO>> getComments(@PathVariable long postId) {
    Optional<Post> op = postRepository.findById(postId);

    if (op.isEmpty()) {
        return ResponseEntity.notFound().build();
    }

    List<CommentDTO> comments = op.get().getComments()
            .stream()
            .map(this::toCommentDTO)
            .collect(Collectors.toList());

    return ResponseEntity.ok(comments);
}

// GET /api/posts/1/comments/1 → un comentario concreto
@GetMapping("/{postId}/comments/{commentId}")
public ResponseEntity<CommentDTO> getComment(@PathVariable long postId,
                                             @PathVariable long commentId) {
    Optional<Post> postOp = postRepository.findById(postId);

    if (postOp.isEmpty()) {
        return ResponseEntity.notFound().build();
    }

    Optional<Comment> commentOp = postOp.get().getComments()
            .stream()
            .filter(c -> c.getId().equals(commentId))
            .findFirst();

    return commentOp.map(c -> ResponseEntity.ok(toCommentDTO(c)))
                    .orElse(ResponseEntity.notFound().build());
}

// DELETE /api/posts/{postId}/comments/{commentId}
@Transactional
@DeleteMapping("/{postId}/comments/{commentId}")
public ResponseEntity<Void> deleteComment(@PathVariable long postId,
                                          @PathVariable long commentId) {
    // Verificar que el post existe
    if (!postRepository.existsById(postId)) {
        return ResponseEntity.notFound().build();
    }

    // Verificar que el comentario existe y pertenece a ese post
    Optional<Comment> commentOp = commentRepository.findById(commentId);

    if (commentOp.isEmpty()) {
        return ResponseEntity.notFound().build();
    }

    if (!commentOp.get().getPost().getId().equals(postId)) {
        return ResponseEntity.badRequest().build(); // 400 si el comment no es de ese post
    }

    commentRepository.deleteById(commentId);
    return ResponseEntity.noContent().build(); // 204
}




}
