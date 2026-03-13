package com.example.MusicForum.Controller;

import com.example.MusicForum.Model.User;
import com.example.MusicForum.Repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/register")
    public String register(Model model) {
        return "register";
    }

    @GetMapping("/edit_profile")
    public String edit_profile(Model model) {
        return "edit_profile";
    }

    @GetMapping("/admin_panel")
    public String admin_panel(Model model) {
        return "admin_panel";
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

    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String passwordConfirm,
            @RequestParam(required = false) org.springframework.web.multipart.MultipartFile avatarFile,
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

        if (avatarFile != null && !avatarFile.isEmpty()) {
            try {
                String base64Image = java.util.Base64.getEncoder().encodeToString(avatarFile.getBytes());
                newUser.setAvatar(base64Image);
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }

        userRepository.save(newUser);

        return "redirect:/login";
    }

    @PostMapping("/edit_profile")
    public String updateProfile(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam(required = false) org.springframework.web.multipart.MultipartFile avatarFile,
            HttpSession session,
            Model model) {

        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            return "redirect:/login";
        }

        Optional<User> userOpt = userRepository.findById(loggedUser.getId());
        if (userOpt.isPresent()) {
            User userToUpdate = userOpt.get();

            // Check uniqueness if username changed
            if (!userToUpdate.getUsername().equals(username) && userRepository.findByUsername(username).isPresent()) {
                model.addAttribute("error", "El nombre de usuario ya existe");
                return "edit_profile";
            }
            // Check uniqueness if email changed
            if (!userToUpdate.getEmail().equals(email) && userRepository.existsByEmail(email)) {
                model.addAttribute("error", "El email ya está registrado");
                return "edit_profile";
            }

            userToUpdate.setUsername(username);
            userToUpdate.setEmail(email);

            if (avatarFile != null && !avatarFile.isEmpty()) {
                try {
                    String base64Image = java.util.Base64.getEncoder().encodeToString(avatarFile.getBytes());
                    userToUpdate.setAvatar(base64Image);
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                    model.addAttribute("error", "Error al procesar la imagen del avatar");
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
}
