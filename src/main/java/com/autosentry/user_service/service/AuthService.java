package com.autosentry.user_service.service;

import com.autosentry.user_service.dto.AuthResponseDTO;
import com.autosentry.user_service.dto.LoginRequestDTO;
import com.autosentry.user_service.dto.RegisterRequestDTO;
import com.autosentry.user_service.entity.NotificationPreference;
import com.autosentry.user_service.entity.User;
import com.autosentry.user_service.repository.NotificationPreferenceRepository;
import com.autosentry.user_service.repository.UserRepository;
import com.autosentry.user_service.security.JwtUtil;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final NotificationPreferenceRepository preferenceRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

//    Handles new user registration, sets default preferences and issues a token.
    @Transactional //user is either completely saved or not at all saved
    public AuthResponseDTO register(RegisterRequestDTO request) {
        // Duplicate Email Check
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email is already in use");
        }

        // Create User, Hash Password, and Save
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        User savedUser = userRepository.save(user);

        // Set Default Notification Preferences
        NotificationPreference prefs = new NotificationPreference();
        prefs.setUser(savedUser);
        prefs.setEmailEnabled(true);
        prefs.setSmsEnabled(true);

        preferenceRepository.save(prefs);

        // Generate 24-Hour Token
        String token = jwtUtil.generateToken(savedUser.getEmail());

        // Return the payload to the Controller
        return new AuthResponseDTO(token, savedUser.getId(), savedUser.getName());
    }

//  Handles login request of the existing user
    public AuthResponseDTO login(LoginRequestDTO request) {

        // If the password hash doesn't match, this will instantly throw an Exception and stop execution.
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // Fetch the verified user from the database
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Generate a fresh 24-Hour Token
        String token = jwtUtil.generateToken(user.getEmail());

        // Return the payload
        return new AuthResponseDTO(token, user.getId(), user.getName());
    }

}
