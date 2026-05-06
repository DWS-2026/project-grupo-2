package com.example.MusicForum.Controller;

import com.example.MusicForum.Model.User;
import com.example.MusicForum.Repository.UserRepository;
import com.example.MusicForum.Service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Base64;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @GetMapping("/loginerror")
    public String loginerror(Model model) {
        model.addAttribute("errorl", "Usuario o contraseña incorrectos");
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm() {
      
        return "register";
    }

    @PostMapping("/register")
    public String processRegistration(
            User user, 
            @RequestParam(value = "avatarFile", required = false) MultipartFile avatarFile, 
            Model model) {
        
        try {
            //User sets an avatar
            if (avatarFile != null && !avatarFile.isEmpty()) {
                String base64Image = Base64.getEncoder().encodeToString(avatarFile.getBytes());
                user.setAvatar(base64Image);
            }

            userService.registerUser(user);
            return "redirect:/login"; 
            
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }
}
