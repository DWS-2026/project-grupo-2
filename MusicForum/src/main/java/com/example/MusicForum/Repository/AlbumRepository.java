package com.example.MusicForum.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.MusicForum.Model.Album;

public interface AlbumRepository extends JpaRepository<Album, Long>{
    
}
