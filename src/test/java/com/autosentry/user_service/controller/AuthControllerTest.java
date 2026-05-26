package com.autosentry.user_service.controller;

import com.autosentry.user_service.dto.AuthResponseDTO;
import com.autosentry.user_service.dto.LoginRequestDTO;
import com.autosentry.user_service.dto.RegisterRequestDTO;
import com.autosentry.user_service.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false) // Bypasses Spring Security
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testRegister() throws Exception {
        // Setting up mock data
        RegisterRequestDTO requestDTO = new RegisterRequestDTO();
        requestDTO.setName("Test User");
        requestDTO.setEmail("test@example.com");
        requestDTO.setPassword("password123");

        AuthResponseDTO mockResponse = new AuthResponseDTO("mock-jwt-token", 1L, "Test User");

        Mockito.when(authService.register(any(RegisterRequestDTO.class))).thenReturn(mockResponse);

        // Perform the POST request and check results
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").value("mock-jwt-token"))
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.name").value("Test User"));
    }

    @Test
    public void testLogin() throws Exception {
//        Setting dummy data in the dto
        LoginRequestDTO requestDTO = new LoginRequestDTO();
        requestDTO.setEmail("test@example.com");
        requestDTO.setPassword("password123");

        AuthResponseDTO mockResponse = new AuthResponseDTO("mock-jwt-token", 1L, "Test User");

        Mockito.when(authService.login(any(LoginRequestDTO.class))).thenReturn(mockResponse);

        // performing and verifying
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mock-jwt-token"))
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.name").value("Test User"));
    }
}