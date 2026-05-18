package com.autosentry.user_service.mapper;

import com.autosentry.user_service.dto.NotificationPreferenceDTO;
import com.autosentry.user_service.entity.NotificationPreference;
import org.springframework.stereotype.Component;

@Component
public class NotificationPreferenceMapper {

    // maps NotificationPreference entity.
    public NotificationPreferenceDTO toDTO(NotificationPreference entity) {
        if (entity == null) {
            return null;
        }

        return new NotificationPreferenceDTO(
                entity.isEmailEnabled(),
                entity.isSmsEnabled()
        );
    }

//    Updates an existing database entity with new values from a DTO.
    public void updateEntityFromDTO(NotificationPreferenceDTO dto, NotificationPreference entity) {
        if (dto == null || entity == null) {
            return;
        }

        entity.setEmailEnabled(dto.isEmailEnabled());
        entity.setSmsEnabled(dto.isSmsEnabled());
    }
}