package com.example.springfsd.app.services;

import com.example.springfsd.app.exception.UserAlreadyExistsException;
import com.example.springfsd.app.models.User;
import com.example.springfsd.app.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Long createUser(User user) throws UserAlreadyExistsException {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UserAlreadyExistsException("The username already exists");
        }
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        User savedUser = userRepository.save(user);
        return savedUser.getId();
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public String getToken(Long userId) {
        return userRepository.findById(userId)
                .map(User::getSessionToken)
                .orElse(null);
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
}
