package com.example.springfsd.app.services;

import com.example.springfsd.app.dto.UserRequestDTO;
import com.example.springfsd.app.exception.UserAlreadyExistsException;
import com.example.springfsd.app.models.User;
import com.example.springfsd.app.repository.UserRepository;
import com.example.springfsd.app.dto.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

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

    public UserResponseDTO getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found"));
        return new UserResponseDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getUsername());
    }

//    public String getToken(Long userId) {
//        return userRepository.findById(userId)
//                .map(User::getSessionToken)
//                .orElse(null);
//    }

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
