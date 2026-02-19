package com.example.MusicForum.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.ui.Model;

@Controller
public class GreetingController{
    @GetMapping("/")
    public String greeting(Model model){
        return "first";
    }

    
    @GetMapping("/album_listing")
    public String album_listing(Model model) {
        return "album_listing"; 
    }
    @GetMapping("/album_view")
    public String viewAlbum(Model model) {
        return "album_view"; 
    }
    @GetMapping("/edit_post")
    public String postEdit(Model model) {
        return "edit_post"; 
    }
    @GetMapping("/login")
    public String login(Model model) {
        return "login"; 
    }
    @GetMapping("/register")
    public String register(Model model) {
        return "register"; 
    }
}
