package com.example.springfsd.app.repository;

import com.example.springfsd.app.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);
    Optional<User> findBySessionToken(String token);

    @Modifying
    @Query("UPDATE User u SET u.sessionToken = :token WHERE u.id = :userId")
    void setSessionToken(@Param("userId") Long userId, @Param("token") String token);

    @Modifying
    @Query("UPDATE User u SET u.sessionToken = null WHERE u.sessionToken = :token")
    void removeSessionToken(@Param("token") String token);
}
