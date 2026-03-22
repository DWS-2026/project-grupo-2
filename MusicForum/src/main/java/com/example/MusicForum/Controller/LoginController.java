package com.example.MusicForum.Controller;

import com.example.MusicForum.Model.User;
import com.example.MusicForum.Repository.UserRepository;
import com.example.MusicForum.Service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@Controller
public class LoginController {

    @Autowired
    private UserRepository userRepository;

     @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("/logout")                                  //Logs out the user
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
    
    @GetMapping("/loginerror")
    public String loginerror(){
        return "loginerror";
    }

     @GetMapping("/register")
    public String showRegistrationForm() {
        // Return the name of the HTML file (register.html)
        return "register";
    }

    @PostMapping("/register")
    public String processRegistration(User user) {
        // Call the service to handle encryption and persistence
        userService.registerUser(user);
        
        // Redirect to login page after successful registration
        return "redirect:/login";
    }

    @ModelAttribute
    public void addAttributes(Model model, HttpServletRequest request) {
    Principal principal = request.getUserPrincipal();

    if (principal != null) {
        model.addAttribute("loggedUser", true);
        model.addAttribute("userName", principal.getName());
        model.addAttribute("admin", request.isUserInRole("ADMIN"));
        
        // Buscar el usuario en la BD para obtener el id
        userRepository.findByUsername(principal.getName())
            .ifPresent(user -> model.addAttribute("id", user.getId()));
    } else {
        model.addAttribute("loggedUser", false);
    }
}
}
