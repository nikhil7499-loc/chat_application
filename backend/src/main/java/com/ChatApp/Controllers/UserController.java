package com.ChatApp.Controllers;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import com.ChatApp.BusinessAccess.UserBal;
import com.ChatApp.Entities.User;
import com.ChatApp.Models.Responses.UserResponses;
import com.ChatApp.Security.CurrentUser;
import com.ChatApp.Security.IsAuthenticatedUser;
import com.ChatApp.Utils.UploadDocument;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserBal userBal;
    private final UploadDocument uploadDocument;

    // ✅ Base upload directory
    private static final Path UPLOAD_DIR = Paths.get("uploads").toAbsolutePath().normalize();

    @Autowired
    public UserController(UserBal userBal, UploadDocument uploadDocument) {
        this.userBal = userBal;
        this.uploadDocument = uploadDocument;
    }

    // 🔹 Update Profile
    @PostMapping(value = "/update-profile", consumes = "multipart/form-data")
    @IsAuthenticatedUser
    public ResponseEntity<?> updateProfile(
            @CurrentUser User user,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            // ✅ Update username
            if (username != null && !username.trim().isEmpty()) {
                if (!username.matches("^[A-Za-z][A-Za-z0-9_]{2,29}$")) {
                    return ResponseEntity.badRequest().body("Invalid username. Must start with a letter and contain only letters, numbers, or underscores.");
                }
                user.setUsername(username.trim());
            }

            // ✅ Update name
            if (name != null && !name.trim().isEmpty()) {
                user.setName(name.trim());
            }

            // ✅ Update profile picture
            if (file != null && !file.isEmpty()) {
                long maxFileSize = 10 * 1024 * 1024; // 10 MB
                if (file.getSize() > maxFileSize) {
                    return ResponseEntity.badRequest().body("File size exceeds 10 MB limit.");
                }

                String contentType = file.getContentType();
                if (contentType == null ||
                        !(contentType.equalsIgnoreCase("image/png")
                                || contentType.equalsIgnoreCase("image/jpeg")
                                || contentType.equalsIgnoreCase("image/jpg")
                                || contentType.equalsIgnoreCase("image/webp"))) {
                    return ResponseEntity.badRequest().body("Invalid file type. Only PNG, JPG, JPEG, or WEBP images are allowed.");
                }

                // ✅ Try deleting old profile picture (if exists)
                try {
                    if (user.getProfile_picture() != null && !user.getProfile_picture().isEmpty()) {
                        uploadDocument.deleteFile(user.getProfile_picture());
                    }
                } catch (Exception ex) {
                    // Silently ignore if file not found or already deleted
                    System.out.println("⚠️ Old profile picture not found or could not be deleted: " + ex.getMessage());
                }

                // ✅ Upload new picture
                String filePath = uploadDocument.uploadFile(file);
                user.setProfile_picture(filePath);
            }


            User updatedUser = userBal.updateUser(user);
            return ResponseEntity.ok(new UserResponses.UserResponse(updatedUser));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating profile: " + e.getMessage());
        }
    }

    @GetMapping("/profile-picture/{fileName}")
    public ResponseEntity<?> getProfilePictureByFileName(@PathVariable String fileName) {
        try {
            Path filePath = UPLOAD_DIR.resolve(fileName).normalize();

            // Prevent path traversal
            if (!filePath.startsWith(UPLOAD_DIR)) {
                return ResponseEntity.status(403).body("Invalid file path.");
            }

            File file = filePath.toFile();
            if (!file.exists()) {
                return ResponseEntity.notFound().build();
            }

            Resource resource = new UrlResource(file.toURI());
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getName() + "\"")
                    .body(resource);

        } catch (MalformedURLException e) {
            return ResponseEntity.internalServerError().body("Error loading file: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Unexpected error: " + e.getMessage());
        }
    }
}
