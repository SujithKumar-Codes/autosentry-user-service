package com.autosentry.user_service.service;

import com.autosentry.user_service.dto.NotificationPreferenceDTO;
import com.autosentry.user_service.dto.UserProfileDTO;
import com.autosentry.user_service.entity.NotificationPreference;
import com.autosentry.user_service.entity.User;
import com.autosentry.user_service.mapper.NotificationPreferenceMapper;
import com.autosentry.user_service.mapper.UserMapper;
import com.autosentry.user_service.repository.NotificationPreferenceRepository;
import com.autosentry.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final NotificationPreferenceRepository preferenceRepository;
    private final UserMapper userMapper;
    private final NotificationPreferenceMapper preferenceMapper;

//    Fetch the user profile(
    public UserProfileDTO getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return userMapper.toDTO(user);
    }

//    This is called by services like compliance service
    public NotificationPreferenceDTO getNotificationPreferences(Long userId) {
        NotificationPreference prefs = preferenceRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Preferences not found for this user"));

        return preferenceMapper.toDTO(prefs);
    }

    @Transactional
    public NotificationPreferenceDTO updateNotificationPreferences(Long userId, NotificationPreferenceDTO request) {
        //Fetch the existing record from the database
        NotificationPreference existingPrefs = preferenceRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Preferences not found for this user"));

        // Use mapper to apply the new true/false values to the existing entity
        preferenceMapper.updateEntityFromDTO(request, existingPrefs);

        // Save the updated entity
        NotificationPreference updatedPrefs = preferenceRepository.save(existingPrefs);

        // Return the new preference
        return preferenceMapper.toDTO(updatedPrefs);
    }
}
