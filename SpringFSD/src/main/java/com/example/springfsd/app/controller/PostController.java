package com.example.springfsd.app.controller;

import com.example.springfsd.app.config.JwtUtil;
import com.example.springfsd.app.dto.PostRequestDTO;
import com.example.springfsd.app.dto.PostResponseDTO;
import com.example.springfsd.app.services.PostService;
import com.example.springfsd.app.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    public PostController(PostService postService, JwtUtil jwtUtil, UserService userService) {
        this.postService = postService;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<String> createPost(@RequestHeader("Authorization") String authorizationHeader,
                                             @RequestBody PostRequestDTO postRequestDTO) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        String token = authorizationHeader.substring(7);
//        String username = jwtUtil.extractUsername(token); // Assuming you have a method to extract the username
        Long userId = jwtUtil.extractUserId(token); // Add this method to extract userId from token

        // Optional: Validate userId if needed
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        // Create the post with the author ID
        Long postId = postService.createPost(postRequestDTO, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body("Post created successfully with id: " + postId);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable("postId") Long postId,
                                             @RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            String username = jwtUtil.extractUsername(token);

            if(jwtUtil.validateToken(token, username)) {
                Long authorId = userService.getUserIdByUsername(username);

                try{
                    postService.deletePost(postId, authorId);
                    return ResponseEntity.ok().body("Post deleted successfully");
                } catch (RuntimeException e) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
                }
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<String> updatePost(@PathVariable Long postId,
                                             @RequestHeader("Authorization") String authorizationHeader,
                                             @RequestBody PostRequestDTO postRequestDTO) {
        String token = authorizationHeader.substring(7);
        String updatedText = postService.updatePost(postId, token, postRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(updatedText);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDTO> getPost(@PathVariable Long postId){
        try{
            PostResponseDTO postResponseDTO = postService.getPost(postId);
            return ResponseEntity.ok(postResponseDTO);
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
