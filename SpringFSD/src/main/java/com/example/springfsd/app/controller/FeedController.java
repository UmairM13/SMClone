package com.example.springfsd.app.controller;

import com.example.springfsd.app.dto.PostResponseDTO;
import com.example.springfsd.app.models.Post;
import com.example.springfsd.app.models.User;
import com.example.springfsd.app.repository.LikeRepository;
import com.example.springfsd.app.repository.UserRepository;
import com.example.springfsd.app.services.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/feed")
public class FeedController {

    private final PostService postService;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;

    public FeedController(PostService postService, UserRepository userRepository, LikeRepository likeRepository) {
        this.postService = postService;
        this.userRepository = userRepository;
        this.likeRepository = likeRepository;
    }

    @GetMapping
    public ResponseEntity<List<PostResponseDTO>> getFeed() {
        List<Post> posts = postService.getAllPosts(); // Retrieve all posts

        List<PostResponseDTO> postResponseDTOs = posts.stream()
                .map(this::convertToPostResponseDTO) // Convert to DTO
                .collect(Collectors.toList());

        return ResponseEntity.ok(postResponseDTOs); // Return the DTOs
    }

    private PostResponseDTO convertToPostResponseDTO(Post post) {
        Long authorId = post.getAuthorId();
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Long> likedUserIds = likeRepository.findUsersByPostId(post.getId());
        List<User> likedUsers = userRepository.findAllById(likedUserIds);
        List<PostResponseDTO.LikeDTO> likes = likedUsers.stream()
                .map(user -> new PostResponseDTO.LikeDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getUsername()))
                .collect(Collectors.toList());

        PostResponseDTO.AuthorDTO authorDTO = new PostResponseDTO.AuthorDTO(author.getId(), author.getFirstName(), author.getLastName(), author.getUsername());

        return new PostResponseDTO(post.getId(), post.getDatePublished().toEpochSecond(ZoneOffset.UTC), post.getText(), authorDTO, likes);
    }
}




