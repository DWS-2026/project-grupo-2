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
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
            @RequestParam String password,
            HttpSession session,
            Model model) {

        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isEmpty() || !userOptional.get().getPassword().equals(password)) {
            model.addAttribute("errorl", "Correo o contraseña incorrectos");
            return "login";
        }

        User user = userOptional.get();
        session.setAttribute("loggedUser", user);

        if (user.getUserRole() == User.UserRole.ADMIN) {
            return "redirect:/admin_panel";
        }

        return "redirect:/user_profile/" + user.getId();
    }

    @GetMapping("/logout")                                  //Logs out the user
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
