package com.example.MusicForum.Model;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

public record AlbumDTO(
    Long id,
    String title,
    String date,
    String artist,
    
    List<String> songs
) {

}
