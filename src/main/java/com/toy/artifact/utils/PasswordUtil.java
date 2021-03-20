package com.toy.artifact.utils;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordUtil {

    public PasswordUtil() {
        encoder = new BCryptPasswordEncoder(13);
    }

    private final PasswordEncoder encoder;

    public String encode(String password) {
        return encoder.encode(password);
    }

    public boolean verify(String password, String encrypted) {
        return encoder.matches(password, encrypted);
    }
}
