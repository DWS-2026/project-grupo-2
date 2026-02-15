package com.example.MusicForum;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.ui.Model;

@Controller
public class GreetingController{
    @GetMapping("/")
    public String greeting(Model model){
        return "first";
    }
}
