package com.example.springfsd.app.config;

import com.example.springfsd.app.services.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    public JwtUtil(UserService userService) {
        this.userService = userService;
    }

    @PostConstruct
    public void postConstruct() {
        LOGGER.info("JWT Secret Key initialized."); // Avoid logging the key directly in production
    }

    // Logger
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtil.class);
    private final UserService userService;

    public String generateToken(String username) {
        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes(StandardCharsets.UTF_8))
                .compact();
        LOGGER.debug("Generated JWT: {}", token);
        return token;
    }

    private Claims extractAllClaims(String token) {
        try {
            LOGGER.debug("Attempting to parse JWT: {}", token);
            return Jwts.parser()
                    .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            LOGGER.error("Error parsing JWT: {}", e.getMessage());
            throw e;
        }
    }
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, String username) {
        final String currentUsername = extractUsername(token);
        return (username.equals(currentUsername) && !isTokenExpired(token));
    }

    public Long extractUserId(String token) {
        String username = extractUsername(token);
        // Assuming you have a way to convert username to user ID, e.g., querying the User service
        // You may need to adjust this to match your UserService implementation
        return userService.getUserIdByUsername(username); // This requires a UserService method
    }

}
