package com.example.springfsd.app.repository;

import com.example.springfsd.app.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>{
    Optional<Post> findById(Long postId);
    List<Post> findAllByAuthorId(Long userId);
}
