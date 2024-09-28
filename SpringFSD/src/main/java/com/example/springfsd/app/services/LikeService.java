package com.example.springfsd.app.services;

import com.example.springfsd.app.models.Like;
import com.example.springfsd.app.repository.LikeRepository;
import com.example.springfsd.app.repository.PostRepository;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserService userService;
    private final PostRepository postRepository;

    public LikeService(LikeRepository likeRepository, UserService userService, PostRepository postRepository) {
        this.likeRepository = likeRepository;
        this.userService = userService;
        this.postRepository = postRepository;
    }

    public String likePost(String token, Long postId) {
        Long userId = userService.getUserIdByToken(token);

        if (!postRepository.existsById(postId)) {
            throw new IllegalStateException("Post not found");
        }

        if(likeRepository.findByPostIdAndUserId(postId, userId).isPresent()) {
            throw new IllegalStateException("Post already liked");
        }

        Like like = new Like();
        like.setPostId(postId);
        like.setUserId(userId);
        likeRepository.save(like);

        return "Post Liked";
    }

    public String unlikePost(String token, Long postId) {
        Long userId = userService.getUserIdByToken(token);

        Like like = likeRepository.findByPostIdAndUserId(postId, userId)
                .orElseThrow(() -> new IllegalStateException("Post not liked"));

        likeRepository.delete(like);

        return "Post Unliked";
    }
}
