package com.ChatApp.Models.Requests;

import java.util.regex.Pattern;

public class UserRequests {

    public static class SignupRequest {

        private String username;
        private String email;
        private String password;
        private String gender;
        private String dateOfBirth;

        private final static Pattern USERNAME_PATTERN = Pattern.compile("^[A-Za-z][A-Za-z0-9]{2,29}$");

        private final static Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

        private final static Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");

        /**
         * @return String return the username
         */
        public String getUsername() {
            return username;
        }

        /**
         * @param username the username to set
         */
        public void setUsername(String username) {
            if (!USERNAME_PATTERN.matcher(username).matches()) {
                throw new IllegalArgumentException("Invalid username: must start with a letter and contain only alphanumeric characters (3–30 chars).");
            }
            this.username = username;
        }

        /**
         * @return String return the email
         */
        public String getEmail() {
            return email;
        }

        /**
         * @param email the email to set
         */
        public void setEmail(String email) {
            if (!EMAIL_PATTERN.matcher(email).matches()) {
                throw new IllegalArgumentException("Invalid email");
            }
            this.email = email;
        }

        /**
         * @return String return the password
         */
        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            if (!PASSWORD_PATTERN.matcher(password).matches()) {
                throw new IllegalArgumentException("Password must be at least 8 characters, must contain upper and one lower case alphabet, a special character and a number");
            }
            this.password = password;
        }

        /**
         * @return String return the gender
         */
        public String getGender() {
            return gender;
        }

        /**
         * @param gender the gender to set
         */
        public void setGender(String gender) {
            this.gender = gender;
        }

        /**
         * @return String return the dateOfBirth
         */
        public String getDateOfBirth() {
            return dateOfBirth;
        }

        /**
         * @param dateOfBirth the dateOfBirth to set
         */
        public void setDateOfBirth(String dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
        }
    }

    public static class LoginRequest {

        private String emailOrUsername;
        private String password;

        /**
         * @return String return the emailOrUsername
         */
        public String getEmailOrUsername() {
            return emailOrUsername;
        }

        /**
         * @param emailOrUsername the emailOrUsername to set
         */
        public void setEmailOrUsername(String emailOrUsername) {
            this.emailOrUsername = emailOrUsername;
        }

        public String getPassword() {
            return this.password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class ForgotPasswordRequest {

        private String email;

        public String getEmail() {
            return this.email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

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
            if (!SignupRequest.PASSWORD_PATTERN.matcher(newPassword).matches()) {
                throw new IllegalArgumentException(
                        "Password must contain at least 8 characters including one uppercase letter, one lowercase letter, one number, and one special character."
                );
            }
            this.newPassword = newPassword;
        }
    }
}
