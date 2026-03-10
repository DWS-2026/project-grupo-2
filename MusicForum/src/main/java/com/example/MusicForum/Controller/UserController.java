package com.example.MusicForum.Controller;

import com.example.MusicForum.Model.User;
import com.example.MusicForum.Repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import java.util.*;

import com.example.MusicForum.Model.Comment;
import com.example.MusicForum.Model.Post;
import com.example.MusicForum.Repository.PostRepository;

import jakarta.annotation.PostConstruct;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    
    @GetMapping("/user_profile/{id}")
    public String userProfile(@PathVariable Long id, Model model) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            return "user_profile";
        }
        return "redirect:/";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String passwordConfirm,
            Model model) {

        if (userRepository.findByUsername(username).isPresent()) {
            model.addAttribute("error", "El nombre de usuario ya existe");
            return "register";
        }

        if (userRepository.existsByEmail(email)) {
            model.addAttribute("error", "El email ya está registrado");
            return "register";
        }

        if (!password.equals(passwordConfirm)) {
            model.addAttribute("error", "Las contraseñas no coinciden");
            return "register";
        }

        User newUser = new User(username, password, email, User.UserRole.USER);
        userRepository.save(newUser);

        return "redirect:/login";
    }
}
