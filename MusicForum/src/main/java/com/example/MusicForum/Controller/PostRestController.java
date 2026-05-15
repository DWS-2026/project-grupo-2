package com.example.MusicForum.Controller;

import com.example.MusicForum.Model.PostDTO;
import com.example.MusicForum.Model.User;
import com.example.MusicForum.Model.CommentDTO;

import com.example.MusicForum.Model.Album;
import com.example.MusicForum.Model.Comment;
import com.example.MusicForum.Model.Post;
import com.example.MusicForum.Repository.CommentRepository;
import com.example.MusicForum.Repository.PostRepository;
import com.example.MusicForum.Service.PostService;
import com.example.MusicForum.Utils.HtmlSanitizer;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.MusicForum.Repository.AlbumRepository;
import com.example.MusicForum.Repository.UserRepository;

import java.security.Principal;
import java.time.LocalDate;
import org.springframework.http.HttpStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/v1/posts")
public class PostRestController {
    @Autowired
    private HtmlSanitizer htmlSanitizer;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostService postService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private UserRepository userRepository;

    private PostDTO toDTO(Post post) {
        PostDTO dto = new PostDTO();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setDescription(htmlSanitizer.sanitize(post.getDescription()));
        dto.setDate(post.getDate());

        // Instead of returning the full User object (which would cause serialization
        // issues and expose private data), we only return the author's username.
        dto.setAuthorUsername(post.getUser().getUsername());

        // We don't return the actual image binary in list responses.
        // We just signal whether an image exists so the client can request it
        // separately.
        dto.setHasImage(post.getImageFile() != null);

        // Return the number of comments instead of the full comment list,
        // since comments are retrieved via a dedicated endpoint.
        dto.setCommentCount(post.getComments().size());

        // Map each associated Album entity to just its title (a plain String),
        // so the client gets the relevant display info without the full Album object.
        dto.setAlbumTitles(
                post.getAlbums().stream()
                        .map(Album::getTitle)
                        .collect(Collectors.toList()));

        return dto;
    }

    private CommentDTO toCommentDTO(Comment comment) {
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setComment(comment.getComment());

        // Same as with posts: only expose the username, not the full User object.
        dto.setAuthorUsername(comment.getUser().getUsername());

        // Include the parent post ID so the client knows which post this comment
        // belongs to.
        dto.setPostId(comment.getPost().getId());

        return dto;
    }

    @GetMapping
    public ResponseEntity<Page<PostDTO>> getAllPosts(Pageable pageable) {
        // findAll(Pageable) is inherited from JpaRepository and runs a paginated
        // SELECT query. We then map each Post entity to a PostDTO using our helper
        // method.
        Page<PostDTO> posts = postRepository.findAll(pageable).map(this::toDTO);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getPost(@PathVariable long id) {
        // findById returns an Optional, which is empty if no post has that ID.
        Optional<Post> op = postRepository.findById(id);

        // If the Optional contains a post, convert it to DTO and return 200.
        // If it is empty, return 404 without a body.
        return op.map(post -> ResponseEntity.ok(toDTO(post)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable long id, Principal principal) {

        Optional<Post> op = postRepository.findById(id);
        if (op.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User loggedUser = userRepository.findByUsername(principal.getName()).orElseThrow();
        boolean isAdmin = loggedUser.getRoles().contains("ADMIN");
        boolean isOwner = op.get().getUser().getId().equals(loggedUser.getId());

        if (!isAdmin && !isOwner) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        postRepository.deleteAlbumRelations(id);
        postRepository.deleteComments(id);
        postRepository.deletePostById(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<CommentDTO>> getComments(@PathVariable long postId) {
        // First verify the post exists. If it does not, return 404 immediately.
        Optional<Post> op = postRepository.findById(postId);
        if (op.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Retrieve the post's comment list and map each Comment entity to a CommentDTO.
        List<CommentDTO> comments = op.get().getComments()
                .stream()
                .map(this::toCommentDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(comments);
    }

    @GetMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDTO> getComment(@PathVariable long postId,
            @PathVariable long commentId) {
        // Verify the parent post exists before looking for its comments.
        Optional<Post> postOp = postRepository.findById(postId);
        if (postOp.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Search for the comment within the post's comment list.
        // This ensures the comment actually belongs to this post and not another one.
        Optional<Comment> commentOp = postOp.get().getComments()
                .stream()
                .filter(c -> c.getId().equals(commentId))
                .findFirst();

        // Return the CommentDTO if found, or 404 if no matching comment exists.
        return commentOp.map(c -> ResponseEntity.ok(toCommentDTO(c)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Transactional
    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable long postId,
            @PathVariable long commentId, Principal principal) {

        if (!postRepository.existsById(postId)) {
            return ResponseEntity.notFound().build();
        }

        Optional<Comment> commentOp = commentRepository.findById(commentId);
        if (commentOp.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (!commentOp.get().getPost().getId().equals(postId)) {
            return ResponseEntity.badRequest().build();
        }

        // Check owner or admin
        User loggedUser = userRepository.findByUsername(principal.getName()).orElseThrow();
        boolean isAdmin = loggedUser.getRoles().contains("ROLE_ADMIN") ||
                loggedUser.getRoles().contains("ADMIN");
        boolean isOwner = commentOp.get().getUser().getId().equals(loggedUser.getId());

        if (!isAdmin && !isOwner) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        commentRepository.deleteById(commentId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<PostDTO> createPost(@RequestBody PostDTO postDTO, Principal principal) {

        // Validate that the required fields are present and not blank.
        // We do this manually here because PostDTO is also used for responses,
        // so we cannot add @NotBlank constraints directly on the DTO fields
        // without affecting other endpoints.
        if (postDTO.getTitle() == null || postDTO.getTitle().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        if (postDTO.getDescription() == null || postDTO.getDescription().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        // Look up the currently authenticated user from the database.
        // principal.getName() returns the username stored in the JWT token.
        User author = userRepository.findByUsername(principal.getName()).orElseThrow();

        // Build the new Post entity from the DTO fields.
        Post post = new Post();
        post.setTitle(postDTO.getTitle());
        post.setDescription(htmlSanitizer.sanitize(postDTO.getDescription()));
        post.setUser(author);

        // Set the creation date automatically on the server side.
        // We never trust the client to send the correct date.
        post.setDate(LocalDate.now().toString());

        // If the client sent a list of album IDs, fetch those albums from the database
        // and associate them with the post. We use findAllById which returns only
        // the albums that actually exist, silently ignoring invalid IDs.
        if (postDTO.getAlbumIds() != null && !postDTO.getAlbumIds().isEmpty()) {
            List<Album> albums = albumRepository.findAllById(postDTO.getAlbumIds());
            post.setAlbums(albums);
        }

        // Persist the post to the database.
        postRepository.save(post);

        // Return 201 Created with the new post as a DTO in the response body.
        // 201 is the correct HTTP status for a resource that has just been created,
        // as opposed to 200 OK which is used for successful reads or updates.
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(post));
    }

    @PostMapping("/{postId}/comments")
    public ResponseEntity<CommentDTO> createComment(@PathVariable long postId,
            @RequestBody CommentDTO commentDTO,
            Principal principal) {
        // Validate that the comment text is present and not blank.
        if (commentDTO.getComment() == null || commentDTO.getComment().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        // Verify the parent post exists before trying to attach a comment to it.
        Optional<Post> postOp = postRepository.findById(postId);
        if (postOp.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Look up the currently authenticated user from the database.
        User author = userRepository.findByUsername(principal.getName()).orElseThrow();

        // Build the Comment entity and link it to both the post and the author.
        Comment comment = new Comment(commentDTO.getComment(), author);
        comment.setPost(postOp.get());

        // Persist the comment to the database.
        commentRepository.save(comment);

        // Return 201 Created with the new comment as a DTO in the response body.
        return ResponseEntity.status(HttpStatus.CREATED).body(toCommentDTO(comment));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostDTO> updatePost(@PathVariable long id,
            @RequestBody PostDTO postDTO,
            Principal principal) {

        // Check the post exists
        Optional<Post> op = postRepository.findById(id);
        if (op.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Post post = op.get();
        User loggedUser = userRepository.findByUsername(principal.getName()).orElseThrow();

        // Only the owner or an admin can edit
        boolean isAdmin = loggedUser.getRoles().contains("ADMIN");
        boolean isOwner = post.getUser().getId().equals(loggedUser.getId());

        if (!isAdmin && !isOwner) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // Validate required fields
        if (postDTO.getTitle() == null || postDTO.getTitle().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        if (postDTO.getDescription() == null || postDTO.getDescription().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        // Update fields
        post.setTitle(postDTO.getTitle());
        //post.setDescription(postDTO.getDescription()); <- wasnt sanitized
        post.setDescription(htmlSanitizer.sanitize(postDTO.getDescription())); //correct


        // Update albums if albumIds are provided
        if (postDTO.getAlbumIds() != null) {
            List<Album> albums = albumRepository.findAllById(postDTO.getAlbumIds());
            post.setAlbums(albums);
        }

        postRepository.save(post);

        return ResponseEntity.ok(toDTO(post));
    }

}
