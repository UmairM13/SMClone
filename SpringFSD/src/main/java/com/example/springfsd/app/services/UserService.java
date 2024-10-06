package com.example.springfsd.app.services;

import com.example.springfsd.app.dto.*;
import com.example.springfsd.app.exception.UserAlreadyExistsException;
import com.example.springfsd.app.models.Follow;
import com.example.springfsd.app.models.Like;
import com.example.springfsd.app.models.Post;
import com.example.springfsd.app.models.User;
import com.example.springfsd.app.repository.FollowRepository;
import com.example.springfsd.app.repository.LikeRepository;
import com.example.springfsd.app.repository.PostRepository;
import com.example.springfsd.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;

    public Long createUser(UserRequestDTO userRequestDTO) throws UserAlreadyExistsException {
        if (userRepository.existsByUsername(userRequestDTO.username())) {
            throw new UserAlreadyExistsException("The username already exists");
        }

        String hashedPassword = passwordEncoder.encode(userRequestDTO.password());
        User user = User.builder()
                .firstName(userRequestDTO.firstName())
                .lastName(userRequestDTO.lastName())
                .username(userRequestDTO.username())
                .password(hashedPassword)
                .build();

        User savedUser = userRepository.save(user);
        return savedUser.getId();
    }

    public UserResDTO getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        // Fetch followers of the user (users who follow the specified user)
        List<Follow> followersEntities = followRepository.findAllByUserId(userId);
        List<UserDTO> followers = followersEntities.stream()
                .map(follower -> {
                    User followUser = userRepository.findById(follower.getFollowerId())
                            .orElseThrow(() -> new IllegalStateException("Follower not found"));
                    return new UserDTO(followUser.getId(), followUser.getFirstName(), followUser.getLastName(), followUser.getUsername());
                })
                .collect(Collectors.toList());

        // Fetch users that the specified user is following
        List<Follow> followingEntities = followRepository.findAllByFollowerId(userId);
        List<UserDTO> following = followingEntities.stream()
                .map(followedUser -> {
                    User followingUser = userRepository.findById(followedUser.getUserId())
                            .orElseThrow(() -> new IllegalStateException("Following not found"));
                    return new UserDTO(followingUser.getId(), followingUser.getFirstName(), followingUser.getLastName(), followingUser.getUsername());
                })
                .collect(Collectors.toList());

        // Fetch posts via PostRepository
        List<Post> userPosts = postRepository.findAllByAuthorId(userId);
        List<PostResponseDTO> posts = userPosts.stream()
                .map(post -> {
                    // Fetch the author details
                    User author = userRepository.findById(post.getAuthorId())
                            .orElseThrow(() -> new IllegalStateException("Author not found"));

                    // Collect likes for the post
                    List<Like> likes = likeRepository.findAllByPostId(post.getId());

                    return new PostResponseDTO(
                            post.getId(),
                            post.getDatePublished().toEpochSecond(ZoneOffset.UTC),
                            post.getText(),
                            new PostResponseDTO.AuthorDTO(author.getId(),
                                    author.getFirstName(),
                                    author.getLastName(),
                                    author.getUsername()),
                            likes.stream()
                                    .map(like -> {
                                        // Fetch user details for the like
                                        User likeUser = userRepository.findById(like.getUserId())
                                                .orElseThrow(() -> new IllegalStateException("Liker not found"));
                                        return new PostResponseDTO.LikeDTO(likeUser.getId(),
                                                likeUser.getFirstName(),
                                                likeUser.getLastName(),
                                                likeUser.getUsername());
                                    })
                                    .collect(Collectors.toList())
                    );
                })
                .collect(Collectors.toList());

        return new UserResDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getUsername(), followers, following, posts);
    }




    @Transactional
    public void setToken(Long userId, String token) {
        userRepository.findById(userId).ifPresent(user -> {
            user.setSessionToken(token);
            userRepository.save(user);
        });
    }

    @Transactional
    public void removeToken(String token) {
        userRepository.findBySessionToken(token).ifPresent(user -> {
            user.setSessionToken(null);
            userRepository.save(user);
        });
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public Long getUserIdByUsername(String username) {
        // Get the Optional<User> from the repository
        Optional<User> userOptional = userRepository.findByUsername(username);
        // Check if the user exists and return the ID or handle the error
        return userOptional.map(User::getId)
                .orElseThrow(() -> new RuntimeException("User not found")); // Handle the case where the user is not found
    }

    public Long getUserIdByToken(String token){
        return userRepository.findBySessionToken(token)
                .map(User::getId)
                .orElseThrow(() -> new RuntimeException("Invalid token"));
    }

    public List<UserResponseDTO> searchUsers(String q) {
        List<User> users;

        if (q == null || q.trim().isEmpty()) {
            users = userRepository.findAll();
        } else {
            users = userRepository.searchUsersByQuery(q.trim());
        }

        return users.stream()
                .map(user -> new UserResponseDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getUsername()))
                .collect(Collectors.toList());
    }

}
