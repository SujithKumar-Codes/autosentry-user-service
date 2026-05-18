package com.autosentry.user_service.mapper;

import com.autosentry.user_service.dto.UserProfileDTO;
import com.autosentry.user_service.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    // maps user entity (hiding the password)
    public UserProfileDTO toDTO(User user) {
        if (user == null) {
            return null;
        }

        return new UserProfileDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCreatedAt()
        );
    }
}