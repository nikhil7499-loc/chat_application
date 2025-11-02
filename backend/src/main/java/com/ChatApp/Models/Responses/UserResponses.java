package com.ChatApp.Models.Responses;

import com.ChatApp.Entities.User;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class UserResponses {

    public static class UserResponse {
        private String id;
        private String username;
        private String email;
        private String name;
        private String gender;
        private String profilePicture;
        private LocalDateTime createdAt;

        public UserResponse(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.email = user.getEmail();
            this.name = user.getName();

            // ✅ Convert enum (User.Gender) → String
            this.gender = (user.getGender() != null) ? user.getGender().name() : null;

            this.profilePicture = user.getProfile_picture();

            // ✅ Convert Instant → LocalDateTime using system default time zone
            // This makes sure local offset is properly added
            this.createdAt = (user.getCreated_at() != null)
                    ? LocalDateTime.ofInstant(user.getCreated_at(), ZoneId.systemDefault())
                    : null;
        }

        public String getId() {
            return id;
        }

        public String getUsername() {
            return username;
        }

        public String getEmail() {
            return email;
        }

        public String getName() {
            return name;
        }

        public String getGender() {
            return gender;
        }

        public String getProfilePicture() {
            return profilePicture;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }
    }
}
