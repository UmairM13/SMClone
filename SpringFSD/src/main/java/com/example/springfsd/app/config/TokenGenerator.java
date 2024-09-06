package com.example.springfsd.app.config;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.UUID;

@Component
public class TokenGenerator {

    private static final SecureRandom secureRandom = new SecureRandom();

    public static String generateToken() {
        byte[] tokenBytes = new byte[16];
        secureRandom.nextBytes(tokenBytes);
        return bytesToHex(tokenBytes);
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
