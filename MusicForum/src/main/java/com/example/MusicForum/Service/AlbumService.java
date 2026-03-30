package com.example.MusicForum.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.stereotype.Service;

import com.example.MusicForum.Model.Album;
import com.example.MusicForum.Repository.AlbumRepository;

import java.io.IOException;
import java.util.Optional;

import javax.sql.rowset.serial.SerialBlob;

@Service
public class AlbumService {

    @Autowired
    private AlbumRepository albumRepository;

    public void save(Album album) {
        albumRepository.save(album);
    }

    public void save(Album album, MultipartFile imageBlob) throws IOException {
        if (!imageBlob.isEmpty()) {
            try {
                album.setImageBlob(new SerialBlob(imageBlob.getBytes()));
            } catch (Exception e) {
                throw new IOException("Failed to create image blob", e);
            }

        }
        this.save(album);
    }

    public Optional<Album> findById(long id) {
        Optional<Album> op = albumRepository.findById(id);
        return op;
    }

}
