package com.example.springfsd.app.services;

import com.example.springfsd.app.dto.PostRequestDTO;
import com.example.springfsd.app.dto.PostResponseDTO;
import com.example.springfsd.app.exception.PostNotFoundException;
import com.example.springfsd.app.models.Like;
import com.example.springfsd.app.models.Post;
import com.example.springfsd.app.models.User;
import com.example.springfsd.app.repository.LikeRepository;
import com.example.springfsd.app.repository.PostRepository;
import com.example.springfsd.app.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;

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
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isPresent()) {
            Post p = optionalPost.get();

            // Fetch the author details
            User author = userRepository.findById(p.getAuthorId())
                    .orElseThrow(() -> new IllegalArgumentException("Author not found"));

            // Collect likes for the post
            List<Like> likes = likeRepository.findAllByPostId(p.getId());

            // Map likes to LikeDTO
            List<PostResponseDTO.LikeDTO> likeDTOs = likes.stream()
                    .map(like -> {
                        // Fetch user details for the like
                        User likeUser = userRepository.findById(like.getUserId())
                                .orElseThrow(() -> new IllegalStateException("Liker not found"));
                        return new PostResponseDTO.LikeDTO(
                                likeUser.getId(),
                                likeUser.getFirstName(),
                                likeUser.getLastName(),
                                likeUser.getUsername());
                    })
                    .collect(Collectors.toList());

            // Create AuthorDTO
            PostResponseDTO.AuthorDTO authorDTO = new PostResponseDTO.AuthorDTO(
                    author.getId(),
                    author.getFirstName(),
                    author.getLastName(),
                    author.getUsername());

            // Return the populated PostResponseDTO
            return new PostResponseDTO(
                    p.getId(),
                    p.getDatePublished().toEpochSecond(ZoneOffset.UTC),
                    p.getText(),
                    authorDTO,
                    likeDTOs
            );
        } else {
            throw new IllegalArgumentException("Post not found");
        }
    }

    public List<Post> getAllPosts() {
        // Fetch all posts ordered by date published (newest first)
        return postRepository.findAll();
    }

}
