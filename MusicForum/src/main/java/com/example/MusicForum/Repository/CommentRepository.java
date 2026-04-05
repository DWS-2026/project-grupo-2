package com.example.MusicForum.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.MusicForum.Model.*;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Modifying
    @Query(value = "UPDATE comments c JOIN posts p ON c.post_id = p.id SET c.user_id = null WHERE c.user_id = :userId AND p.user_id != :userId", nativeQuery = true)
    void detachUserFromOthersPosts(@Param("userId") Long userId);
    // Methods aviailable:
    // save(), findById(), findAll(), deleteById(), etc.
}
