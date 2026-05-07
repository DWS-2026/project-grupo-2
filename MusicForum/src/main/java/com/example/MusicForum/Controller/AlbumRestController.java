package com.example.MusicForum.Controller;

import java.io.IOException;
import java.net.URI;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.MusicForum.Model.Album;
import com.example.MusicForum.Model.AlbumDTO;
import com.example.MusicForum.Model.Post;
import com.example.MusicForum.Repository.AlbumRepository;
import com.example.MusicForum.Service.AlbumService;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/api/v1/albums")
public class AlbumRestController {
    @Autowired
    private AlbumRepository albumRepository;
    @Autowired
    private AlbumService albumService;


    
    @GetMapping("/")
    public Collection<Album> getAlbums() {
        return albumRepository.findAll();
    }

    private AlbumDTO toDTO(Album album){
        return new AlbumDTO(album.getId(), album.getTitle(), album.getDate(), album.getArtist(), album.getSongs());

    }

    private List<AlbumDTO> toDTOs(Collection<Album> albums){
        return albums.stream().map(this::toDTO).toList();
    }
    private Album toDomain(AlbumDTO albumDTO){
        return new Album(albumDTO.title(), albumDTO.songs(),  albumDTO.date(), albumDTO.artist());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Album> deleteAlbum(@PathVariable long id){
        Optional<Album> op = albumRepository.findById(id);
        if(op.isPresent()){
            Album album = op.get();

            // Remove album from all posts before deleting to avoid breaking them
            for (Post post : album.getPosts()) {
                post.getAlbums().remove(album);
            }

            album.getPosts().clear();
            albumRepository.deleteById(id);
            return ResponseEntity.accepted().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Album> getAlbum(@PathVariable long id) {
        Optional<Album> op = albumRepository.findById(id);
        if(op.isPresent()){
            Album album = op.get();
            return ResponseEntity.ok(album);
        } else {
            return ResponseEntity.notFound().build();
        }   
    }
    @PostMapping("/")
    public ResponseEntity<Album> postAlbum(@RequestBody Album album) {
        albumRepository.save(album);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/{id}/image")
    public ResponseEntity<MultipartFile> postImage(@PathVariable long id, @RequestParam MultipartFile imageFile) throws IOException{
        Optional<Album> op = albumRepository.findById(id);

        if(op.isPresent()){
            Album album = op.get();
            albumService.save(album, imageFile);
            return ResponseEntity.ok(imageFile);
        } else {
            return ResponseEntity.badRequest().build();
        }
        
        
    }

    @PutMapping("/{id}")
    public ResponseEntity<Album> putAlbum(@PathVariable long id, @RequestBody Album album) {
        if(albumRepository.existsById(id)){
            album.setId(id);
            albumRepository.save(album);
            return ResponseEntity.ok(album);
        } else {
            return ResponseEntity.notFound().build();
        }
        
    }
    
}
