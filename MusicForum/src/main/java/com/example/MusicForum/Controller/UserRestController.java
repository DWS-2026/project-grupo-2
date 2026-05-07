package com.example.MusicForum.Controller;

import com.example.MusicForum.Model.Post;
import com.example.MusicForum.Model.PostDTO;
import com.example.MusicForum.Model.User;
import com.example.MusicForum.Model.UserDTO;
import com.example.MusicForum.Model.UserUpdateDTO;
import com.example.MusicForum.Repository.CommentRepository;
import com.example.MusicForum.Repository.PostRepository;
import com.example.MusicForum.Repository.UserRepository;
import com.example.MusicForum.Service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.MusicForum.Utils.FileUtils;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/v1/users")
public class UserRestController {
    
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    //List all users (equal to /user_listing)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("")
    public ResponseEntity<List<UserDTO>> getAllUsers(){
        List<UserDTO> users = userRepository.findAll().stream().map(UserDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }
    
    //Users profile (equal to /user_profile/{id})
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserProfile(@PathVariable Long id, Principal principal){
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Error 401
        }

        Optional<User> userOpt = userRepository.findById(id);

        if(userOpt.isEmpty()){
            return ResponseEntity.notFound().build(); //Error 404
        }

        User user = userOpt.get();
        Optional<User> loggedUserOpt = userRepository.findByUsername(principal.getName());
        if (loggedUserOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User loggedUser = loggedUserOpt.get();

        //Only admin or the user it self
        boolean isAdmin = loggedUser.getRoles().contains("ADMIN");
        boolean isOwnsProfile = loggedUser.getId().equals(id);

        if(!isAdmin && !isOwnsProfile){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();  //Error 403

        }

        return ResponseEntity.ok(new UserDTO(user));  //Everything was ok
    }


    //Edit profile (equal to /edit_profile) using PUT to update
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProfile(@PathVariable Long id, @Valid @RequestBody UserUpdateDTO userUpdateDTO, Principal principal){
        User loggedUser = userRepository.findByUsername(principal.getName()).orElseThrow();

        //Only admin or the user it self
        if(!loggedUser.getId().equals(id) && !loggedUser.getRoles().contains("ADMIN")){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permiso para editar este perfil");
        }

        User userToUpdate = userRepository.findById(id).orElseThrow();

        //Check unique email and username
        if(userUpdateDTO.getUsername() != null && !userToUpdate.getUsername().equals(userUpdateDTO.getUsername())){
            if(userRepository.findByUsername(userUpdateDTO.getUsername()).isPresent()){
                return ResponseEntity.badRequest().body("El nombre de usuario ya existe");
            }
            userToUpdate.setUsername(userUpdateDTO.getUsername());
        }

        if(userUpdateDTO.getEmail() != null && !userToUpdate.getEmail().equals(userUpdateDTO.getEmail())){
            if(userRepository.existsByEmail(userUpdateDTO.getEmail())){
                return ResponseEntity.badRequest().body("El email ya está registrado");
            }
            userToUpdate.setEmail(userUpdateDTO.getEmail());
        }

        //Avatar
        if(userUpdateDTO.getAvatar() != null){
            userToUpdate.setAvatar(userUpdateDTO.getAvatar());
        }

        if (userUpdateDTO.getPassword() != null && !userUpdateDTO.getPassword().isBlank()) {
            userToUpdate.setEncodedPassword(passwordEncoder.encode(userUpdateDTO.getPassword()));
        }

        userRepository.save(userToUpdate);
    
        return ResponseEntity.ok(new UserDTO(userToUpdate));
    }

    //Delete user (equal to /user/{id}/delete)
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deleteUser(@PathVariable Long id, Principal principal){
        Optional<User> optUser = userRepository.findById(id);
        if(optUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User loggedUser = userRepository.findByUsername(principal.getName()).orElseThrow();
        
        //Admin only or the user it self
        if(!loggedUser.getId().equals(id) && !loggedUser.getRoles().contains("ADMIN")){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        User user = optUser.get();

        commentRepository.detachUserFromOthersPosts(id);
        for(Post post : new ArrayList<>(user.getPosts())){
            postRepository.delete(post);
        }

        userRepository.delete(user);
        return ResponseEntity.ok().body("Usuario eliminado correctamente"); //Returns 200 OK
    }

    //Get users posts (equal to /user_posts/{id})
    @GetMapping("/{id}/posts")
    public ResponseEntity<Page<PostDTO>> getUserPosts(@PathVariable Long id, Pageable pageable){
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        Page<Post> postsPage = postRepository.findByUserId(id, pageable);
        //Post->PostDTO manually 
        Page<PostDTO> dtoPage = postsPage.map(post ->{
            PostDTO dto = new PostDTO();
            dto.setId(post.getId());
            dto.setTitle(post.getTitle());
            dto.setDescription(post.getDescription());
            dto.setDate(post.getDate());

            if (post.getUser() != null) {
                dto.setAuthorUsername(post.getUser().getUsername());
            }

            dto.setHasImage(post.getImageFile() != null);

            if (post.getAlbums() != null) {
                List<String> albumTitles = post.getAlbums().stream()
                        .map(album -> album.getTitle()) // Asumo que en la clase Album tienes un getTitle() o getName()
                        .collect(Collectors.toList());
                dto.setAlbumTitles(albumTitles);
            }

            if (post.getComments() != null) {
                dto.setCommentCount(post.getComments().size());
            } else {
                dto.setCommentCount(0);
            }
            
            return dto;
        });

        return ResponseEntity.ok(dtoPage); 
    }

    //Point 15.
    @PutMapping("/{id}/avatar")
    public ResponseEntity<?> updateAvatar(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            String saveFileName = FileUtils.saveFileSafe(file);
            
            //Search for user on data base
            User user = userRepository.findById(id).orElseThrow();
            
            //Keep file name only
            user.setAvatar(saveFileName);
            userRepository.save(user);

            return ResponseEntity.ok("Imagen subida correctamente: " + saveFileName);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error de seguridad: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/avatar/downloads")
    public ResponseEntity<Resource> getAvatar(@PathVariable Long id) {
        try {
            //Search for user's file name
            User user = userRepository.findById(id).orElseThrow();
            String filename = user.getAvatar();

            Path file = Paths.get("uploads").resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
