package com.example.MusicForum.Service;

import com.example.MusicForum.Model.Album;
import com.example.MusicForum.Model.Comment;
import com.example.MusicForum.Model.Post;
import com.example.MusicForum.Model.User;
import com.example.MusicForum.Repository.*;

import jakarta.annotation.PostConstruct;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DataBaseInit {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private AlbumService albumService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {

        User user1 = new User("admin", passwordEncoder.encode("admin123"), "admin@musicforum.com", "ADMIN", "USER");
        User user2 = new User("user", passwordEncoder.encode("user123"), "user@gmail.com", "USER");
        User user3 = new User("user2", passwordEncoder.encode("user1234"), "invitado@gmail.com", "USER");
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        List<String> songs1 = Arrays.asList("953", "Speedway", "Reggae", "Near DT, MI", "Western", "Of Schlagenheim",
                "bmbmbm", "Years Ago", "Ducter");
        List<String> songs2 = Arrays.asList("Berghain", "La perla", "La Rumba del Perdón", "Memoria", "Magnolias");
        Album album1 = new Album("Schlagenheim", songs1, "21-06-2019", "black midi");
        Album album2 = new Album("Lux", songs2, "15-03-2020", "Rosalía");

        try (InputStream is = getClass().getResourceAsStream("/static/images/Schlagenheim.jpg")) {
            byte[] imageBytes = is.readAllBytes();
            album1.setImageBlob(new SerialBlob(imageBytes));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read image file", e);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create image blob", e);
        }
        albumService.save(album1);
        // weird thingy for saving images as blobs getting the image from the path and
        // turning it to bytes
        // it's for saving some entries as an initiation of the database
        // now that this works and its clear there is no need for the string imagepath
        // field in album, it's deletion elsewhere will follow

        try (InputStream is = getClass().getResourceAsStream("/static/images/rosalia-lux.jpg")) {
            byte[] imageBytes = is.readAllBytes();
            album2.setImageBlob(new SerialBlob(imageBytes));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read image file", e);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create image blob", e);
        }
        albumService.save(album2);

        Post post1 = new Post("Getting killed - Geese", "2024-01-01", null,
                "El tercer álbum de Geese le planta un sensacional cruce de mangas a cualquier normatividad previsible. \r\n"
                        +
                        "Es el gran salto adelante que esperaban sus devotos.",
                user2);
        Post post2 = new Post("El día que me olvides - Walls", "2024-01-02", null, "Su tercer disco de estudio llega avalado no solo por sus anteriores referencias, sino también por el gran éxito de ?Haz lo que quieras conmigo?, una balada que iba creciendo hacia el rock y que funcionó como preludio de estas doce composiciones.\r\n" + //
                        "\r\n" + //
                        "", user2);
        Post post3 = new Post("Post3", "2024-01-03", null, "Descripción 3", user1);

        try (InputStream is = getClass().getResourceAsStream("/static/images/Geese-Getting-Killed.jpg")) {
            byte[] imageBytes = is.readAllBytes();
            post1.setImage(new SerialBlob(imageBytes));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read image file", e);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create image blob", e);
        }

        try (InputStream is = getClass().getResourceAsStream("/static/images/Walls-El-dia-que-me-olvides.jpg")) {
            byte[] imageBytes = is.readAllBytes();
            post2.setImage(new SerialBlob(imageBytes));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read image file", e);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create image blob", e);
        }

        post1.addComment(new Comment("Comentario 1", user1));
        post1.getAlbums().add(album1);
        post1.getAlbums().add(album2);
        post2.getAlbums().add(album2);

        postRepository.saveAll(Arrays.asList(post1, post2, post3));
    }
}
