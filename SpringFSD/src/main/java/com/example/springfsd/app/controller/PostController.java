package com.example.springfsd.app.controller;

import com.example.springfsd.app.models.Post;
import com.example.springfsd.app.services.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping
    public ResponseEntity<String> addPost(@Valid @RequestBody Post post){
        try{
            Long postId = postService.addNewPost(post.getText(), post.getAuthorId());
            return ResponseEntity.status(201).body("Post created successfully: " + postId);
        }catch (Exception e){
            return ResponseEntity.status(400).body("Error: "+ e.getMessage());
        }
    }
}
