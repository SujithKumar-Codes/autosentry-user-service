package com.autosentry.user_service.controller;

import com.autosentry.user_service.dto.AuthResponseDTO;
import com.autosentry.user_service.dto.LoginRequestDTO;
import com.autosentry.user_service.dto.RegisterRequestDTO;
import com.autosentry.user_service.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

//    Endpoint for registering a new user
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request) {
        // Hand the DTO to the service layer
        AuthResponseDTO response = authService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

//    Endpoint for logging in an existing user
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        // Hand the credentials to the service layer
        AuthResponseDTO response = authService.login(request);

        return ResponseEntity.ok(response);
    }
}
