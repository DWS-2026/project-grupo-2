package com.example.MusicForum.Controller;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.MusicForum.Model.Album;
import com.example.MusicForum.Repository.AlbumRepository;
import com.example.MusicForum.Service.AlbumService;

@RestController
public class AlbumRestController {
    @Autowired
    private AlbumRepository albumRepository;
    @Autowired
    private AlbumService albumService;

    @GetMapping("/albums/")
    public Collection<Album> getAlbums() {
        return albumRepository.findAll();
    }

    @DeleteMapping("/album/{id}")
    public ResponseEntity<Album> deleteAlbum(@PathVariable long id){
        Optional<Album> op = albumRepository.findById(id);
        if(op.isPresent()){
            Album album = op.get();
            albumRepository.deleteById(id);
            return ResponseEntity.ok(album);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/albums/{id}")
    public ResponseEntity<Album> getAlbum(@PathVariable long id) {
        Optional<Album> op = albumRepository.findById(id);
        if(op.isPresent()){
            Album album = op.get();
            return ResponseEntity.ok(album);
        } else {
            return ResponseEntity.notFound().build();
        }   
    }
}
