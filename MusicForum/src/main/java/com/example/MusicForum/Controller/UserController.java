package com.example.MusicForum.Controller;

import com.example.MusicForum.Model.User;
import com.example.MusicForum.Repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder; // Necessary for registration
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // To encrypt the password during registration



    @GetMapping("/edit_profile")
    public String showEditProfileForm() {
        return "edit_profile";
    }

    @GetMapping("/admin_panel")
    public String showAdminPanel() {
        return "admin_panel";
    }

    @GetMapping("/user_profile/{id}")
    public String showUserProfile(@PathVariable Long id, Model model) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            return "user_profile";
        }
        return "redirect:/";
    }
    @PostMapping("/edit_profile")
    public String updateProfile(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam(required = false) MultipartFile avatarFile,
            HttpSession session,
            Model model) {

        // Get the logged user from the session
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            return "redirect:/login";
        }

        Optional<User> userOpt = userRepository.findById(loggedUser.getId());
        if (userOpt.isPresent()) {
            User userToUpdate = userOpt.get();

            // Check uniqueness if username changed
            if (!userToUpdate.getUsername().equals(username) && userRepository.findByUsername(username).isPresent()) {
                model.addAttribute("error", "Username already exists");
                return "edit_profile";
            }
            
            // Check uniqueness if email changed
            if (!userToUpdate.getEmail().equals(email) && userRepository.existsByEmail(email)) {
                model.addAttribute("error", "Email is already registered");
                return "edit_profile";
            }

            userToUpdate.setUsername(username);
            userToUpdate.setEmail(email);

            // Handle avatar update
            if (avatarFile != null && !avatarFile.isEmpty()) {
                try {
                    String base64Image = Base64.getEncoder().encodeToString(avatarFile.getBytes());
                    userToUpdate.setAvatar(base64Image);
                } catch (IOException e) {
                    e.printStackTrace();
                    model.addAttribute("error", "Error processing the avatar image");
                    return "edit_profile";
                }
            }

            userRepository.save(userToUpdate);
            // Update session with new user data
            session.setAttribute("loggedUser", userToUpdate);

            return "redirect:/user_profile/" + userToUpdate.getId();
        }

        return "redirect:/";
    }

    @GetMapping("/user_posts")
        public String showUserPosts(Principal principal, Model model) {
        userRepository.findByUsername(principal.getName())
        .ifPresent(user -> model.addAttribute("posts", user.getPosts()));
        return "user_posts";
}
}
