package com.example.MusicForum.Controller;

import com.example.MusicForum.Model.Comment;
import com.example.MusicForum.Model.Post;
import com.example.MusicForum.Model.User;
import com.example.MusicForum.Repository.CommentRepository;
import com.example.MusicForum.Repository.PostRepository;
import com.example.MusicForum.Repository.UserRepository;
import com.example.MusicForum.Service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder; // Necessary for registration
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // To encrypt the password during registration

    @GetMapping("/edit_profile")
    public String showEditProfile(Model model, Principal principal) {
        if (principal == null)
            return "redirect:/login";

        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        model.addAttribute("user", user);
        return "edit_profile";
    }

    @GetMapping("/admin_panel")
    public String showAdminPanel() {
        return "admin_panel";
    }

    //NEW
    @GetMapping("/user_profile") 
    public String showMyProfile(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        // Buscamos al usuario logueado
        // ✅ PON ESTO:
        Optional<User> optLoggedUser = userRepository.findByUsername(principal.getName());
        if (!optLoggedUser.isPresent()) {
            // Si el usuario de la sesión ya no existe (porque lo acaba de cambiar), le mandamos al login
            return "redirect:/login"; 
        }
        User loggedUser = optLoggedUser.get();
        
        model.addAttribute("user", loggedUser);
        
        return "user_profile";
    }

    @GetMapping("/user_profile/{id}")
    public String showUserProfile(@PathVariable Long id, Model model, Principal principal) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            // Only the own user or an admin can view the profile
           // ✅ PON ESTO:
            Optional<User> optLoggedUser = userRepository.findByUsername(principal.getName());
            if (!optLoggedUser.isPresent()) {
                // Si el usuario de la sesión ya no existe (porque lo acaba de cambiar), le mandamos al login
                return "redirect:/login"; 
            }
            User loggedUser = optLoggedUser.get();

            boolean isAdmin = loggedUser.getRoles().contains("ADMIN");
            boolean isOwnProfile = loggedUser.getId().equals(id);

            if (!isAdmin && !isOwnProfile) {
                return "redirect:/access_denied";
            }

            model.addAttribute("user", user.get());
            return "user_profile";
        }
        return "redirect:/";
    }

    @PostMapping("/user/{id}/delete")
    @Transactional
    public String deleteUser(@PathVariable Long id) {
        Optional<User> optUser = userRepository.findById(id);
        if (!optUser.isPresent())
            return "redirect:/user_listing";

        User user = optUser.get();

        // Set user_id to null on comments made on other people's posts
        commentRepository.detachUserFromOthersPosts(id);

        // Delete the user's own posts (cascade handles their comments)
        for (Post post : new ArrayList<>(user.getPosts())) {
            postRepository.delete(post);
        }

        userRepository.delete(user);
        return "redirect:/user_listing";
    }

    @PostMapping("/edit_profile")
    public String updateProfile(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam(required = false) String newPassword,
            @RequestParam(required = false) MultipartFile avatarFile,
            Principal principal,
            Model model, HttpServletRequest request) {

        if (principal == null) {
            return "redirect:/login";
        }

        User userToUpdate = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Check uniqueness if username changed
        if (!userToUpdate.getUsername().equals(username) && userRepository.findByUsername(username).isPresent()) {
            model.addAttribute("error", "El nombre de usuario ya existe");
            model.addAttribute("user", userToUpdate);
            return "edit_profile";
        }

        // Check uniqueness if email changed
        if (!userToUpdate.getEmail().equals(email) && userRepository.existsByEmail(email)) {
            model.addAttribute("error", "El email ya está registrado");
            model.addAttribute("user", userToUpdate);
            return "edit_profile";
        }

        userToUpdate.setUsername(username);
        userToUpdate.setEmail(email);

        // Avatar update
        if (avatarFile != null && !avatarFile.isEmpty()) {
            try {
                String base64Image = Base64.getEncoder().encodeToString(avatarFile.getBytes());
                userToUpdate.setAvatar(base64Image);
            } catch (IOException e) {
                e.printStackTrace();
                model.addAttribute("error", "Error al procesar la imagen del avatar");
                model.addAttribute("user", userToUpdate);
                return "edit_profile";
            }
        }

        boolean credentialsChanged = !userToUpdate.getUsername().equals(username) || 
                                     (newPassword != null && !newPassword.isEmpty());

        userService.updateUser(userToUpdate, username, email, newPassword);

        //User has to re-login
        if (credentialsChanged) {
            try {
                request.logout();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "redirect:/login"; 
        }

        return "redirect:/user_profile/" + userToUpdate.getId();
    
    }

    @GetMapping("/user_posts")
    public String showUserPosts(Principal principal, Model model) {
        userRepository.findByUsername(principal.getName())
                .ifPresent(user -> model.addAttribute("posts", user.getPosts()));
        return "user_posts";
    }

    // In panel admin. User listing
    @GetMapping("/user_listing")
    public String showUserListing(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "user_listing";
    }

    @GetMapping("/user_posts/{id}")
    public String showUserPosts(@PathVariable Long id, Model model) {
        userRepository.findById(id)
                .ifPresent(user -> model.addAttribute("posts", user.getPosts()));
        return "user_posts";
    }
}
