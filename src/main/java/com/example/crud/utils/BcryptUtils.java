package com.example.crud.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BcryptUtils {
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public static String encryptPassword(String password) {
        return encoder.encode(password);
    }

    public static boolean comparePasswords(String password, String hashedPassword) {
        return encoder.matches(password, hashedPassword);
    }
}
