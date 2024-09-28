package com.example.springfsd.app.controller;

import com.example.springfsd.app.config.JwtUtil;
import com.example.springfsd.app.models.User;
import com.example.springfsd.app.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @PostMapping("/users")
    public ResponseEntity<String> createUser(@Valid @RequestBody UserService.UserRequestDTO userRequestDTO) {
        try {
            Long userId = userService.createUser(userRequestDTO);
            return ResponseEntity.status(201).body("User created successfully with id: " + userId);
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserService.UserRequestDTO loginRequestDTO) {
        User user = userService.getUserByUsername(loginRequestDTO.username());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }

        if (!passwordEncoder.matches(loginRequestDTO.password(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }

        String token = jwtUtil.generateToken(user.getUsername());
        userService.setToken(user.getId(), token);

        UserService.UserResponseDTO userResponseDTO = new UserService.UserResponseDTO(user.getId(), user.getUsername(), token);
        return ResponseEntity.ok().body(userResponseDTO);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            String username = jwtUtil.extractUsername(token);

            if (jwtUtil.validateToken(token, username)) {
                userService.removeToken(token);
                return ResponseEntity.ok().body("Logout successful");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
    }
}
