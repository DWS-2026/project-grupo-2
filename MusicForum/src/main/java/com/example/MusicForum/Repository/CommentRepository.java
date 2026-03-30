package com.example.MusicForum.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.MusicForum.Model.*;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    // Methods aviailable:
    // save(), findById(), findAll(), deleteById(), etc.
}
