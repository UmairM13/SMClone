package com.example.springfsd.app.services;

import com.example.springfsd.app.dto.PostRequestDTO;
import com.example.springfsd.app.models.Post;
import com.example.springfsd.app.repository.PostRepository;
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
}
