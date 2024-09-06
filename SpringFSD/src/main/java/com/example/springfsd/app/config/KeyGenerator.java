package com.example.springfsd.app.config;

import java.security.SecureRandom;
import java.util.Base64;

public class KeyGenerator {

    public static String generateSecretKey() {
        SecureRandom random = new SecureRandom();
        byte[] keyBytes = new byte[32];  // 32 bytes = 256 bits
        random.nextBytes(keyBytes);
        return Base64.getEncoder().encodeToString(keyBytes);
    }

    public static void main(String[] args) {
        String secretKey = generateSecretKey();
        System.out.println("Secret Key: " + secretKey);
    }
}
