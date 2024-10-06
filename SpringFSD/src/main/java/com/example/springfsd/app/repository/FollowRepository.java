package com.example.springfsd.app.repository;

import com.example.springfsd.app.models.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    boolean existsByFollowerIdAndUserId(Long followerId, Long userId);
    Optional<Follow> findByFollowerIdAndUserId(Long followerId, Long userId);
    List<Follow> findAllByUserId(Long userId); // Fetch users who follow this user
    List<Follow> findAllByFollowerId(Long followerId);
}
