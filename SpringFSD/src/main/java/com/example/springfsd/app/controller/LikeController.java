package com.example.springfsd.app.controller;

import com.example.springfsd.app.services.LikeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
public class LikeController {
    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<String> likePost(@RequestHeader("Authorization") String auth,
                                         @PathVariable Long postId){
        try{
            String token = auth.substring(7);
            String response = likeService.likePost(token, postId);
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{postId}/like")
    public ResponseEntity<String> unlikePost(@RequestHeader("Authorization") String auth,
            @PathVariable Long postId){
        try{
            String token = auth.substring(7);
            String response = likeService.unlikePost(token, postId);
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
