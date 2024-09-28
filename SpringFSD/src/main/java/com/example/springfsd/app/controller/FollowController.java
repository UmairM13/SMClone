package com.example.springfsd.app.controller;

import com.example.springfsd.app.services.FollowService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class FollowController {

    private final FollowService followService;

    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    @PostMapping("/{userId}/follow")
    public ResponseEntity<String> followUser(@RequestHeader("Authorization") String auth,
                                             @PathVariable Long userId){
        try{
            String token = auth.substring(7);
            String response = followService.followUser(token, userId);
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{userId}/follow")
    public ResponseEntity<String> unfollowUser(@RequestHeader("Authorization") String authorization,
                                               @PathVariable Long userId){
        try{
            String token = authorization.substring(7);
            String response = followService.unfollowUser(token, userId);
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
