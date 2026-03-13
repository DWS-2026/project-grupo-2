package com.example.MusicForum.Controller;

import com.example.MusicForum.Model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

// Makes the logged in user session available to all templates.

@ControllerAdvice
public class GlobalLogInController {

    @ModelAttribute("loggedUser")
    public User getLoggedUser(HttpSession session) {
        return (User) session.getAttribute("loggedUser");
    }

    @ModelAttribute("admin")
    public boolean isAdmin(HttpSession session) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        return loggedUser != null && loggedUser.getUserRole() == User.UserRole.ADMIN;
    }
}
