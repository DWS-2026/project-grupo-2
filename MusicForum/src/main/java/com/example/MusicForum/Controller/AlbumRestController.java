package com.example.MusicForum.Controller;

import java.io.IOException;
import java.net.URI;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

    @GetMapping("")
    public ResponseEntity<Page<AlbumDTO>> getAlbums(Pageable pageable) {
        return ResponseEntity.ok(albumRepository.findAll(pageable).map(this::toDTO));
    }

    private AlbumDTO toDTO(Album album) {
        return new AlbumDTO(album.getId(), album.getTitle(), album.getDate(), album.getArtist(), album.getSongs());

    }

    private List<AlbumDTO> toDTOs(Collection<Album> albums) {
        return albums.stream().map(this::toDTO).toList();
    }

    private Album toDomain(AlbumDTO albumDTO) {
        return new Album(albumDTO.title(), albumDTO.songs(), albumDTO.date(), albumDTO.artist());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Album> deleteAlbum(@PathVariable long id) {
        Optional<Album> op = albumRepository.findById(id);
        if (op.isPresent()) {
            Album album = op.get();

            // Remove album from all posts before deleting to avoid breaking them
            for (Post post : album.getPosts()) {
                post.getAlbums().remove(album);
            }

            album.getPosts().clear();
            albumRepository.deleteById(id);
            return ResponseEntity.noContent().build(); // instead of .accepted()
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public AlbumDTO getAlbum(@PathVariable long id) {
        return toDTO(albumService.findById(id).orElseThrow());
    }

    @PostMapping("/")
    public ResponseEntity<AlbumDTO> postAlbum(@RequestBody AlbumDTO albumDTO) {
        Album album = toDomain(albumDTO);
        albumService.save(album);
        URI location = URI.create("/api/v1/albums/" + album.getId());
        return ResponseEntity.created(location).body(toDTO(album));
    }

    @PostMapping("/{id}/image")
    public ResponseEntity<MultipartFile> postImage(@PathVariable long id, @RequestParam MultipartFile imageFile)
            throws IOException {
        Optional<Album> op = albumService.findById(id);

        if (op.isPresent()) {
            Album album = op.get();
            albumService.save(album, imageFile);
            return ResponseEntity.ok(imageFile);
        } else {
            return ResponseEntity.badRequest().build();
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<AlbumDTO> putAlbum(@PathVariable long id, @RequestBody AlbumDTO albumDTO) {
    if(albumRepository.existsById(id)){
        Album album = toDomain(albumDTO);
        album.setId(id);
        albumRepository.save(album);
        return ResponseEntity.ok(toDTO(album));
    } else {
        return ResponseEntity.notFound().build();
    }
}

}
