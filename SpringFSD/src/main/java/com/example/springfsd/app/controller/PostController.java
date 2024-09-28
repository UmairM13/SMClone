package com.example.springfsd.app.controller;

import com.example.springfsd.app.config.JwtUtil;
import com.example.springfsd.app.dto.PostRequestDTO;
import com.example.springfsd.app.services.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final JwtUtil jwtUtil;

    public PostController(PostService postService, JwtUtil jwtUtil) {
        this.postService = postService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public ResponseEntity<String> createPost(@RequestHeader("Authorization") String authorizationHeader,
                                             @RequestBody PostRequestDTO postRequestDTO) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        String token = authorizationHeader.substring(7);
        String username = jwtUtil.extractUsername(token); // Assuming you have a method to extract the username
        Long userId = jwtUtil.extractUserId(token); // Add this method to extract userId from token

        // Optional: Validate userId if needed
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        // Create the post with the author ID
        Long postId = postService.createPost(postRequestDTO, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body("Post created successfully with id: " + postId);
    }
}
