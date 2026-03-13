package com.example.MusicForum.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.ui.Model;

@Controller
public class GreetingController {
    @GetMapping("/")
    public String greeting(Model model) {
        return "first";
    }

    /*
     * @GetMapping("/album_listing")
     * public String album_listing(Model model) {
     * return "album_listing";
     * }
     */

}
