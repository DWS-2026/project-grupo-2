package com.example.MusicForum.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.MusicForum.Model.*;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    // JpaRepository<Post, Long>,:
    // save(), findById(), findAll(), deleteById(), etc.

    @Modifying
    @Query(value = "DELETE FROM posts_album WHERE posts_id = :postId", nativeQuery = true)
    void deleteAlbumRelations(@Param("postId") Long postId);

    @Modifying
    @Query(value = "DELETE FROM comments WHERE post_id = :postId", nativeQuery = true)
    void deleteComments(@Param("postId") Long postId);

    @Modifying
    @Query(value = "DELETE FROM posts WHERE id = :postId", nativeQuery = true)
    void deletePostById(@Param("postId") Long postId);

    //Pageable
    Page<Post> findByUserId(Long id, Pageable pageable);
}