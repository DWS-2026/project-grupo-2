package com.example.MusicForum.Controller;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;

import com.example.MusicForum.Model.Album;
import com.example.MusicForum.Model.Post;
import com.example.MusicForum.Repository.AlbumRepository;
import com.example.MusicForum.Service.AlbumService;
import com.example.MusicForum.Service.PostService;

@Controller
public class AlbumController {

    @Autowired
    private AlbumRepository albumRepository;
    @Autowired
    private AlbumService albumService;

    @GetMapping("/album_listing")
    public String showAlbums(Model model) {
        List<Album> albums = albumRepository.findAll();
        model.addAttribute("albums", albums);

        return "album_listing";
    }

    @GetMapping("/album/{id}")
    public String showAlbum(Model model, @PathVariable Long id) {
        Optional<Album> album = albumRepository.findById(id);
        if (album.isPresent()) {
            model.addAttribute("album", album.get());
            Album auxAlbum = album.get();
            model.addAttribute("songsJoined", String.join(",", auxAlbum.getSongs()));

            return "album_view";
        } else {
            return "redirect:/album_listing";
        }
    }

    @PostMapping("/album/{id}/delete")
    public String AlbumDelete(@PathVariable long id) {
        Optional<Album> album = albumRepository.findById(id);

        if (album.isPresent()) {
            Album a = album.get();

            // Remove album from all posts before deleting to avoid breaking them
            for (Post post : a.getPosts()) {
                post.getAlbums().remove(a);
            }
            a.getPosts().clear();

            albumRepository.deleteById(id);
            return "redirect:/album_listing";
        } else {
            return "error";
        }
    }

    @PostMapping("/album/{id}")
    public String updateAlbum(Model model, @PathVariable Long id, Album modifyAlbum,
            @RequestParam("imageFile") MultipartFile imageFile) throws SQLException, IOException {

        Optional<Album> album = albumRepository.findById(id);

        if (album.isPresent()) {
            Album albumF = album.get();
            modifyAlbum.setId(albumF.getId());
            if (imageFile.isEmpty()) {
                modifyAlbum.setImageBlob(albumF.getImageBlob());
            }

            albumService.save(modifyAlbum, imageFile);
        }

        return "redirect:/album/{id}";
    }

    @GetMapping("/album/{id}/image")
    public ResponseEntity<Object> downloadImage(@PathVariable long id) throws SQLException {
        Optional<Album> op = albumRepository.findById(id);
        if (op.isPresent() && op.get().getImageBlob() != null) {
            Blob image = op.get().getImageBlob();
            Resource imageFile = new InputStreamResource(image.getBinaryStream());
            MediaType mediaType = MediaTypeFactory
                    .getMediaType(imageFile)
                    .orElse(MediaType.IMAGE_JPEG);
            return ResponseEntity
                    .ok()
                    .contentType(mediaType)
                    .body(imageFile);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/albumCreate")
    public String createAlbum(@ModelAttribute Album album,
            @RequestParam("imageFile") MultipartFile imageFile) throws IOException {

        albumService.save(album, imageFile);
        return "redirect:/album_listing";

    }

    

    // @AlbumConstruct
    // public void init(){}
}
