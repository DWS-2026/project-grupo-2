package com.example.MusicForum.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.MusicForum.Model.Album;


public interface AlbumRepository extends JpaRepository<Album, Long>{
    Optional<Album> findById(Long id);

}
