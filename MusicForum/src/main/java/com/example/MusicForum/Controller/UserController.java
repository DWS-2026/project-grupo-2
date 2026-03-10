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

    @PostConstruct
    public void init() {
        if (userRepository.count() == 0) {
            userRepository.save(new User("admin", "admin123", "admin@musicforum.com", User.UserRole.ADMIN));
            userRepository.save(new User("user", "user123", "user@gmail.com", User.UserRole.USER));
            userRepository.save(new User("invitado", "guest123", "invitado@gmail.com", User.UserRole.INVITADO));
        }
    }

    @GetMapping("/user_profile/{id}")
    public String userProfile(@PathVariable Long id, Model model) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            return "user_profile";
        }
        return "redirect:/";
    }
}
