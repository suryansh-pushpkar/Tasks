package com.friendbook.service;

import com.friendbook.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class UsernameGenerator {

    private final UserRepository userRepository;

    public UsernameGenerator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String generate(String fullName) {
        String letters = fullName == null ? "" : fullName.replaceAll("[^A-Za-z]", "");
        if (letters.isBlank()) {
            letters = "Friend";
        }
        if (letters.length() < 5) {
            letters = (letters + "Friend").substring(0, 5);
        }

        letters = Character.toUpperCase(letters.charAt(0)) + letters.substring(1, Math.min(letters.length(), 5)).toLowerCase();
        String base = letters.substring(0, 5);

        for (int suffix = 123; suffix < 1000; suffix++) {
            String candidate = base + suffix;
            if (!userRepository.existsByUserName(candidate)) {
                return candidate;
            }
        }

        throw new IllegalStateException("Could not generate a unique username");
    }
}
