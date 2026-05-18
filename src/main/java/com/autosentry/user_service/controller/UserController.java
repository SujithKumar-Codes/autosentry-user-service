package com.autosentry.user_service.controller;

import com.autosentry.user_service.dto.NotificationPreferenceDTO;
import com.autosentry.user_service.dto.UserProfileDTO;
import com.autosentry.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    public final UserService userService;

//    Endpoint for getting the user profile
    @GetMapping("/{id}")
    public ResponseEntity<UserProfileDTO> getUserProfile(@PathVariable Long id) {
        UserProfileDTO response = userService.getUserProfile(id);
        return ResponseEntity.ok(response);
    }

//  Endpoint to get the user notification preferences
    @GetMapping("/{id}/preferences")
    public ResponseEntity<NotificationPreferenceDTO> getNotificationPreferences(@PathVariable Long id) {
        NotificationPreferenceDTO response = userService.getNotificationPreferences(id);
        return ResponseEntity.ok(response);
    }

//    Endpoint for updating the notification preferences for the user
    @PutMapping("/{id}/preferences")
    public ResponseEntity<NotificationPreferenceDTO> updateNotificationPreferences(
            @PathVariable Long id,
            @RequestBody NotificationPreferenceDTO request) {

        NotificationPreferenceDTO response = userService.updateNotificationPreferences(id, request);
        return ResponseEntity.ok(response);
    }
}
