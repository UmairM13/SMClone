package com.example.springfsd;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtTest {
    private static final String SECRET_KEY = "AXpTmVwEb7ethI+9oZQ0ePeaQyEB7dlNm9D5Coi/6Pw=";

    public static void main(String[] args) {
        String token = Jwts.builder()
                .setSubject("user")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours validity
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes())
                .compact();

        // Now verify
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY.getBytes())
                .parseClaimsJws(token)
                .getBody();

        System.out.println("Claims: " + claims.getSubject());
    }
}
