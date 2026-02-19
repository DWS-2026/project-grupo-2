package com.example.MusicForum.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.MusicForum.Model.*;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    // Al extender JpaRepository<Comment, Long>, ya tienes métodos como:
    // save(), findById(), findAll(), deleteById(), etc.
}
