package com.example.springfsd.app.services;

import com.example.springfsd.app.dto.PostRequestDTO;
import com.example.springfsd.app.models.Post;
import com.example.springfsd.app.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public Long createPost(PostRequestDTO postRequestDTO, Long authorId) {
        Post post = new Post();
        post.setText(postRequestDTO.getText());
        post.setDatePublished(Timestamp.valueOf(LocalDateTime.now()).toLocalDateTime());
        post.setAuthorId(authorId); // Set the author ID

        Post savedPost = postRepository.save(post);
        return savedPost.getId();
    }

    @Transactional
    public void deletePost(Long postId, Long authorId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if(!post.getAuthorId().equals(authorId)){
            throw new RuntimeException("You are not allowed to delete this post");
        }

        postRepository.delete(post);
    }
}
