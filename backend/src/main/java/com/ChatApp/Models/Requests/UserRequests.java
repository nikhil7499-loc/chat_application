package com.ChatApp.Models.Requests;

import java.util.regex.Pattern;

public class UserRequests {

    public static class SignupRequest {
        private String username;
        private String email;
        private String password;
        private String name;
        private String gender;
        private String dateOfBirth;

        private static final Pattern USERNAME_PATTERN =
                Pattern.compile("^[A-Za-z][A-Za-z0-9]{2,29}$"); 

        private static final Pattern EMAIL_PATTERN =
                Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

        private static final Pattern PASSWORD_PATTERN =
                Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");


        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            if (username == null || !USERNAME_PATTERN.matcher(username).matches()) {
                throw new IllegalArgumentException(
                    "Invalid username: must start with a letter and contain only alphanumeric characters (3–30 chars)."
                );
            }
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
                throw new IllegalArgumentException("Invalid email format.");
            }
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            if (password == null || !PASSWORD_PATTERN.matcher(password).matches()) {
                throw new IllegalArgumentException(
                    "Password must contain at least 8 characters including one uppercase letter, one lowercase letter, one number, and one special character."
                );
            }
            this.password = password;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getDateOfBirth() {
            return dateOfBirth;
        }

        public void setDateOfBirth(String dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
        }
    }

    // ========================
    // Login Request
    // ========================
    public static class LoginRequest {
        private String emailOrUsername;
        private String password;

        public String getEmailOrUsername() {
            return emailOrUsername;
        }

        public void setEmailOrUsername(String emailOrUsername) {
            this.emailOrUsername = emailOrUsername;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    // ========================
    // Forgot Password
    // ========================
    public static class ForgotPasswordRequest {
        private String email;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            if (email == null || !SignupRequest.EMAIL_PATTERN.matcher(email).matches()) {
                throw new IllegalArgumentException("Invalid email format.");
            }
            this.email = email;
        }
    }

    // ========================
    // Reset Password
    // ========================
    public static class ResetPasswordRequest {
        private String email;
        private String otp;
        private String newPassword;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            if (email == null || !SignupRequest.EMAIL_PATTERN.matcher(email).matches()) {
                throw new IllegalArgumentException("Invalid email format.");
            }
            this.email = email;
        }

        public String getOtp() {
            return otp;
        }

        public void setOtp(String otp) {
            this.otp = otp;
        }

        public String getNewPassword() {
            return newPassword;
        }

        public void setNewPassword(String newPassword) {
            if (newPassword == null || !SignupRequest.PASSWORD_PATTERN.matcher(newPassword).matches()) {
                throw new IllegalArgumentException(
                    "Password must contain at least 8 characters including one uppercase letter, one lowercase letter, one number, and one special character."
                );
            }
            this.newPassword = newPassword;
        }
    }
}
