package com.autosentry.user_service.controller;

import com.autosentry.user_service.dto.NotificationPreferenceDTO;
import com.autosentry.user_service.dto.UserProfileDTO;
import com.autosentry.user_service.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetUserProfile_Returns200() throws Exception {
        Long userId = 1L;

        // Making use of @AllArgsConstructor
        UserProfileDTO mockProfile = new UserProfileDTO(
                userId,
                "Sujith Kumar",
                "sujith@gmail.com",
                LocalDateTime.now()
        );

        Mockito.when(userService.getUserProfile(userId)).thenReturn(mockProfile);

        mockMvc.perform(get("/api/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.name").value("Sujith Kumar"))
                .andExpect(jsonPath("$.email").value("sujith@gmail.com"))
                .andExpect(jsonPath("$.createdAt").exists()); // Verifies the timestamp payload exists
    }

    @Test
    public void testGetNotificationPreferences_Returns200() throws Exception {
        // Arrange
        Long userId = 1L;

//        Making use of @AllArgsConstructor
        NotificationPreferenceDTO mockPrefs = new NotificationPreferenceDTO(true, false);

        Mockito.when(userService.getNotificationPreferences(userId)).thenReturn(mockPrefs);

        mockMvc.perform(get("/api/users/{id}/preferences", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.emailEnabled").value(true))
                .andExpect(jsonPath("$.smsEnabled").value(false));
    }

    @Test
    public void testUpdateNotificationPreferences_Returns200() throws Exception {
        // Arrange
        Long userId = 1L;

        // Setup payload request using @AllArgsConstructor
        NotificationPreferenceDTO requestDTO = new NotificationPreferenceDTO(false, true);

        Mockito.when(userService.updateNotificationPreferences(eq(userId), any(NotificationPreferenceDTO.class)))
                .thenReturn(requestDTO);

        mockMvc.perform(put("/api/users/{id}/preferences", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.emailEnabled").value(false))
                .andExpect(jsonPath("$.smsEnabled").value(true));
    }
}