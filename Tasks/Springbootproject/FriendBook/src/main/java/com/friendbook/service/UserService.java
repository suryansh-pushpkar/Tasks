package com.friendbook.service;

import com.friendbook.dto.ProfileResponse;
import com.friendbook.dto.ProfileUpdateDTO;
import com.friendbook.dto.UserDTO;
import com.friendbook.entity.User;
import com.friendbook.repository.UserRepository;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UsernameGenerator usernameGenerator;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UsernameGenerator usernameGenerator, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.usernameGenerator = usernameGenerator;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User registerUser(UserDTO userDTO) {
        if (userDTO.getCaptchaToken() == null || userDTO.getCaptchaToken().isBlank()) {
            throw new IllegalArgumentException("Captcha verification is required");
        }
        if (userRepository.existsByEmail(userDTO.getEmail().trim().toLowerCase())) {
            throw new IllegalArgumentException("Email is already registered");
        }

        User user = new User();
        user.setFullName(userDTO.getFullName().trim());
        user.setEmail(userDTO.getEmail().trim().toLowerCase());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setUserName(usernameGenerator.generate(userDTO.getFullName()));
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User getById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    @Transactional(readOnly = true)
    public User getByUsername(String username) {
        return userRepository.findByUserName(username)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    @Transactional(readOnly = true)
    public User getByEmail(String email) {
        return userRepository.findByEmail(email.trim().toLowerCase())
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    @Transactional
    public ProfileResponse updateProfile(String username, ProfileUpdateDTO dto, String profileImage) {
        User user = getByUsername(username);
        String normalizedEmail = dto.getEmail().trim().toLowerCase();
        if (!user.getEmail().equalsIgnoreCase(normalizedEmail) && userRepository.existsByEmail(normalizedEmail)) {
            throw new IllegalArgumentException("Email is already registered");
        }

        user.setFullName(dto.getFullName().trim());
        user.setEmail(normalizedEmail);
        user.setFavSongs(trimToNull(dto.getFavSongs()));
        user.setFavBooks(trimToNull(dto.getFavBooks()));
        user.setFavPlaces(trimToNull(dto.getFavPlaces()));

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        if (profileImage != null) {
            user.setProfileImage(profileImage);
        }

        User saved = userRepository.save(user);
        return new ProfileResponse(
            "Profile updated successfully",
            saved.getUserName(),
            saved.getFullName(),
            saved.getEmail(),
            saved.getFavSongs(),
            saved.getFavBooks(),
            saved.getFavPlaces(),
            saved.getProfileImage()
        );
    }

    @Transactional(readOnly = true)
    public List<User> search(String query) {
        if (query == null || query.isBlank()) {
            return List.of();
        }
        return userRepository.findTop10ByUserNameContainingIgnoreCaseOrFullNameContainingIgnoreCase(query.trim(), query.trim());
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
