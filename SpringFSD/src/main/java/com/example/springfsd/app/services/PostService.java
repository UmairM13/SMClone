package com.example.springfsd.app.services;

import com.example.springfsd.app.dto.PostRequestDTO;
import com.example.springfsd.app.dto.PostResponseDTO;
import com.example.springfsd.app.exception.PostNotFoundException;
import com.example.springfsd.app.models.Post;
import com.example.springfsd.app.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserService userService;

    public Long createPost(PostRequestDTO postRequestDTO, Long authorId) {
        Post post = new Post();
        post.setText(postRequestDTO.text());
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

    @Transactional
    public String updatePost(Long postId, String token, PostRequestDTO postRequestDTO) throws PostNotFoundException{
        Long userId = userService.getUserIdByToken(token);

        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));

        if (!post.getAuthorId().equals(userId)) {
            throw new RuntimeException("You are not allowed to update this post");
        }

        post.setText(postRequestDTO.text());
        post.setDatePublished(LocalDateTime.now());

        postRepository.save(post);
        return post.getText();
    }

    public PostResponseDTO getPost(Long postId) {
        Optional<Post> post = postRepository.findById(postId);
        if(post.isPresent()){
            Post p = post.get();
            return new PostResponseDTO(p.getId(), p.getText(), p.getAuthorId());
        } else {
            throw new IllegalArgumentException("Post not found");
        }
    }
}
