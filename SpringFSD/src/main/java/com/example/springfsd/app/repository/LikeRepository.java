package com.example.springfsd.app.repository;

import com.example.springfsd.app.models.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByPostIdAndUserId(Long postId, Long userId);

    @Query("SELECT l.userId FROM Like l WHERE l.postId = :postId")
    List<Long> findUsersByPostId(Long postId);

    List<Like> findAllByPostId(Long postId);
}
