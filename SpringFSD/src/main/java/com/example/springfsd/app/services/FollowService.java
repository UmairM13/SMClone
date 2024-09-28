package com.example.springfsd.app.services;

import com.example.springfsd.app.models.Follow;
import com.example.springfsd.app.repository.FollowRepository;
import org.springframework.stereotype.Service;

@Service
public class FollowService {

    private final FollowRepository followRepository;
    private final UserService userService;

    public FollowService(FollowRepository followRepository, UserService userService) {
        this.followRepository = followRepository;
        this.userService = userService;
    }

    public String followUser(String token, Long userId){
        Long followerId = userService.getUserIdByToken(token);

        if(followerId.equals(userId)){
            throw new IllegalStateException("You cannot follow yourself");
        }

        if(followRepository.existsByFollowerIdAndUserId(followerId, userId)){
            throw new IllegalStateException("You are already following this user");
        }

        Follow follow = new Follow();
        follow.setFollowerId(followerId);
        follow.setUserId(userId);
        followRepository.save(follow);

        return "User followed";
    }

    public String unfollowUser(String token, Long userId){
        Long followerId = userService.getUserIdByToken(token);

        Follow follow = followRepository.findByFollowerIdAndUserId(followerId, userId)
                .orElseThrow(() -> new IllegalStateException("You are not following this user"));

        followRepository.delete(follow);

        return "User unfollowed";
    }
}
