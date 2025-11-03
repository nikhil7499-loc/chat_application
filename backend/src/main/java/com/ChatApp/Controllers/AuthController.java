package com.ChatApp.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.ChatApp.BusinessAccess.AuthBal;
import com.ChatApp.Entities.User;
import com.ChatApp.Exceptions.UnauthorizedException;
import com.ChatApp.Models.Requests.UserRequests;
import com.ChatApp.Models.Responses.UserResponses;
import com.ChatApp.Security.IsAuthenticatedUser;
import com.ChatApp.Security.CurrentUser;

import java.util.Map;

import jakarta.servlet.http.HttpServletResponse;

import java.time.LocalDate;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthBal authBal;

    @Autowired
    public AuthController(AuthBal authBal) {
        this.authBal = authBal;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserRequests.SignupRequest req) {
        try {
            User user = new User();
            user.setUsername(req.getUsername());
            user.setEmail(req.getEmail());
            user.setPassword(req.getPassword());
            user.setName(req.getName());

            if (req.getGender() != null) {
                try {
                    user.setGender(User.Gender.valueOf(req.getGender().toLowerCase()));
                } catch (IllegalArgumentException e) {
                    return ResponseEntity.badRequest().body("Invalid gender value. Allowed: male, female, other");
                }
            }

            if (req.getDateOfBirth() != null && !req.getDateOfBirth().isEmpty()) {
                try {
                    user.setDate_of_birth(LocalDate.parse(req.getDateOfBirth()));
                } catch (Exception e) {
                    return ResponseEntity.badRequest().body("Invalid date format. Use ISO 8601 (e.g. 2000-05-25)");
                }
            }

            User createdUser = authBal.signup(user);
            return ResponseEntity.ok(new UserResponses.UserResponse(createdUser));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequests.LoginRequest req, HttpServletResponse response) {
        try {
            String token = authBal.login(req.getEmailOrUsername(), req.getPassword(), response);
            return ResponseEntity.ok(Map.of(
                "message", "Login successful",
                "token", token
            ));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        authBal.logout(response);
        return ResponseEntity.ok("Logged out successfully.");
    }

    @GetMapping("/me")
    @IsAuthenticatedUser
    public ResponseEntity<?> getAuthenticatedUser(@CurrentUser User user) {
        return ResponseEntity.ok(new UserResponses.UserResponse(user));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody UserRequests.ForgotPasswordRequest request) {
        String otp = authBal.forgotPassword(request.getEmail());
        return ResponseEntity.ok("OTP sent to your email: ");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody UserRequests.ResetPasswordRequest request) {
        authBal.resetPassword(request.getEmail(), request.getOtp(), request.getNewPassword());
        return ResponseEntity.ok("Password reset successfully.");
    }
}
