package com.example.MusicForum.Controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.MusicForum.Model.Album;
import com.example.MusicForum.Model.Post;
import com.example.MusicForum.Repository.AlbumRepository;

@Controller
public class AlbumController {

    @Autowired
    private AlbumRepository albumRepository;

    @GetMapping("/album_listing")
    public String showAlbums(Model model) {
        List<Album> albums = albumRepository.findAll();
        model.addAttribute("albums", albums);

        return "album_listing";
    }

    @GetMapping("/album/{id}")
    public String getAlbum(Model model, @PathVariable Long id) {
        Optional<Album> album = albumRepository.findById(id);
        if (album.isPresent()) {
            model.addAttribute("album", album.get());

            return "album_view";
        } else {
            return "redirect:/album_listing";
        }
    }

    @PostMapping("/album/{id}")
    public String updateAlbum(Model model, @PathVariable Long id, Album modifyAlbum) {
        

        Optional<Album> album = albumRepository.findById(id);
        
        if(album.isPresent()){
            Album albumF = album.get();
            modifyAlbum.setId(albumF.getId());

            albumRepository.save(modifyAlbum);
        }
        

        
        return "redirect:/album/{id}";
    }


    @PostMapping("/albumCreate")
    public String createAlbum(
            @ModelAttribute Album album

    ) throws IOException {

        albumRepository.save(album);
        return "redirect:/album_listing";

    }

    // @AlbumConstruct
    // public void init(){}
}
