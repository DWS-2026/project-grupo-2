package com.example.MusicForum.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.MusicForum.Model.*;

@Repository
public interface UserRepository extends JpaRepository<Post, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByEmail(String email);
}