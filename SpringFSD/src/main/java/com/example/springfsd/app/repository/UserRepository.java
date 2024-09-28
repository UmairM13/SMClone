package com.example.springfsd.app.repository;

import com.example.springfsd.app.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);
    Optional<User> findBySessionToken(String token);
    Optional<User> findById(Long id);

    @Query("SELECT u FROM User u WHERE " +
            "LOWER(u.firstName) LIKE LOWER(CONCAT('%', :q, '%')) " +
            "OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :q, '%')) " +
            "OR LOWER(u.username) LIKE LOWER(CONCAT('%', :q, '%'))")
    List<User> searchUsersByQuery(String q);

}
