package com.autosentry.user_service.service;

import com.autosentry.user_service.dto.NotificationPreferenceDTO;
import com.autosentry.user_service.dto.UserProfileDTO;
import com.autosentry.user_service.entity.NotificationPreference;
import com.autosentry.user_service.entity.User;
import com.autosentry.user_service.exception.ResourceNotFoundException;
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
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        return userMapper.toDTO(user);
    }

//    This is called to get the user's notification preferences
    public NotificationPreferenceDTO getNotificationPreferences(Long userId) {
        NotificationPreference prefs = preferenceRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification preferences not found for user id: " + userId));

        return preferenceMapper.toDTO(prefs);
    }

    @Transactional
    public NotificationPreferenceDTO updateNotificationPreferences(Long userId, NotificationPreferenceDTO request) {
        NotificationPreference existingPrefs = preferenceRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification preferences not found for user id: " + userId));

        preferenceMapper.updateEntityFromDTO(request, existingPrefs);
        NotificationPreference updatedPrefs = preferenceRepository.save(existingPrefs);

        return preferenceMapper.toDTO(updatedPrefs);
    }
}
