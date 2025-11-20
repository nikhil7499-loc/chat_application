package com.ChatApp.Models.Responses;

import java.time.ZoneOffset;
import com.ChatApp.Entities.User;
import java.time.LocalDateTime;
import java.time.ZoneId;

import com.ChatApp.Entities.User;

public class UserResponses {
    public static class UserResponse{
        private String id;
        private String username;
        private String email;
        private String gender;
        private String profilePicture;
        private LocalDateTime createdAt;

        public UserResponse(User user){
            this.id=user.getId();
            this.username=user.getUsername();
            this.email=user.getEmail();

            // enum(User.Gender)
            this.gender=(user.getGender()!=null)? user.getGender().name():null;
            this.profilePicture=user.getProfile_picture();

            this.createdAt=(user.getCreated_at() !=null)? LocalDateTime.ofInstant(user.getCreated_at(),ZoneId.systemDefault()):null;
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
3
}
