package com.ChatApp.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;

import com.ChatApp.BusinessAccess.*;
import com.ChatApp.Entities.*;
import com.ChatApp.Security.*;
import com.ChatApp.Utils.UploadDocument;
import com.ChatApp.Models.Requests.MessageRequests;
import com.ChatApp.Models.Responses.MessageResponses;
import com.ChatApp.Models.Responses.UserResponses;
import com.ChatApp.Repository.UserRepository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final MessageBal messageBal;
    private final ChatGroupBal chatGroupBal;
    private final GroupMemberBal groupMemberBal;
    private final KnownConnectionBal knownConnectionBal;
    private final UploadDocument uploadDocument;
    private final UserRepository userRepository;

    @Autowired
    public MessageController(
            MessageBal messageBal,
            ChatGroupBal chatGroupBal,
            UploadDocument uploadDocument,
            GroupMemberBal groupMemberBal,
            KnownConnectionBal knownConnectionBal,
            UserRepository userRepository) {
        this.messageBal = messageBal;
        this.chatGroupBal = chatGroupBal;
        this.uploadDocument = uploadDocument;
        this.groupMemberBal = groupMemberBal;
        this.knownConnectionBal = knownConnectionBal;
        this.userRepository = userRepository;
    }

    // =====================================
    // 💬 1. Send a message (group or direct)
    // =====================================
    @PostMapping(value = "/send", consumes = "multipart/form-data")
    @IsAuthenticatedUser
    public ResponseEntity<?> sendMessage(
            @CurrentUser User sender,
            @RequestPart("request") MessageRequests.SendMessageRequest req,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        try {

            // ============================================
            // ✅ Validate message type
            // ============================================
            MessageTypes type = messageBal.getMessageTypeByName(req.getType());
            if (type == null) {
                return ResponseEntity.badRequest().body("Invalid message type.");
            }

            String typeName = type.getName();

            // ❌ Block sending files for text or system
            if ((typeName.equals("text") || typeName.equals("system")) && file != null) {
                return ResponseEntity.badRequest().body("Files are not allowed for message type: " + typeName);
            }

            // ✅ Validate text content
            if (typeName.equals("text")) {
                if (req.getContent() == null || req.getContent().trim().isEmpty()) {
                    return ResponseEntity.badRequest().body("Text message cannot be empty.");
                }
            }

            // ❌ Reject non-text messages without a file
            if (!typeName.equals("text") && file == null) {
                return ResponseEntity.badRequest().body("Media messages require a file.");
            }

            // ============================================
            // ✅ Validate file extension if not text
            // ============================================
            if (!typeName.equals("text")) {

                String allowed = type.getAllowedExtensions();
                if (allowed == null || allowed.isBlank()) {
                    return ResponseEntity.badRequest().body("This message type does not support files.");
                }

                String originalName = file.getOriginalFilename();
                if (originalName == null || !originalName.contains(".")) {
                    return ResponseEntity.badRequest().body("Uploaded file has no extension.");
                }

                String ext = originalName.substring(originalName.lastIndexOf('.') + 1).toLowerCase().trim();

                List<String> allowedList = Arrays.stream(allowed.split(","))
                        .map(String::trim)
                        .toList();

                if (!allowedList.contains(ext)) {
                    return ResponseEntity.badRequest()
                            .body("File type ." + ext + " is not allowed for message type: " + typeName);
                }
            }

            // ============================================
            // ✅ Build Message
            // ============================================
            Message message = new Message();
            message.setSender(sender);
            message.setCaption(req.getCaption());
            message.setReplyToMessage(
                    req.getReplyToMessageId() != null
                            ? messageBal.getMessageById(req.getReplyToMessageId()).orElse(null)
                            : null
            );

            // ✅ Set message type
            message.setMessageType(type);

            // ============================================
            // ✅ Direct message
            // ============================================
            if (req.getReceiverId() != null && !req.getReceiverId().isBlank()) {

                Optional<User> receiverOpt = userRepository.findById(req.getReceiverId());
                if (receiverOpt.isEmpty()) {
                    return ResponseEntity.badRequest().body("Receiver not found.");
                }
                User receiver = receiverOpt.get();

                if (knownConnectionBal.isBlocked(sender, receiver) ||
                    knownConnectionBal.isBlocked(receiver, sender)) {
                    return ResponseEntity.status(403).body("You cannot message this user (blocked).");
                }

                message.setReceiver(receiver);
            }

            // ============================================
            // ✅ Group message
            // ============================================
            if (req.getGroupId() != null && !req.getGroupId().isBlank()) {

                Optional<ChatGroup> groupOpt = chatGroupBal.getGroupById(req.getGroupId());
                if (groupOpt.isEmpty()) {
                    return ResponseEntity.badRequest().body("Invalid group ID.");
                }

                ChatGroup group = groupOpt.get();
                boolean isMember = groupMemberBal
                        .getMembers(group)
                        .stream()
                        .anyMatch(m -> m.getUser().getId().equals(sender.getId()) && m.isIs_active());

                if (!isMember) {
                    return ResponseEntity.status(403).body("You are not a member of this group.");
                }

                message.setGroup(group);
            }

            // ============================================
            // ✅ Handle file message
            // ============================================
            if (!typeName.equals("text")) {
                String filename = uploadDocument.uploadProtectedFile(file);
                message.setContent(filename); // ✅ File stored
            } else {
                message.setContent(req.getContent()); // ✅ Normal text
            }

            // ============================================
            // ✅ Save message
            // ============================================
            Message savedMessage = messageBal.sendMessage(message);

            if (savedMessage.getReceiver() != null) {
                knownConnectionBal.updateConnectionOnMessage(sender, savedMessage.getReceiver());
            }

            return ResponseEntity.ok(new MessageResponses.MessageResponse(savedMessage));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error sending message: " + e.getMessage());
        }
    }


    @GetMapping("/file/{messageId}")
    @IsAuthenticatedUser
    public ResponseEntity<?> getMyFile(
            @CurrentUser User user,
            @PathVariable String messageId) {

        try {
            Optional<Message> msgOpt = messageBal.getMessageById(messageId);
            if (msgOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Message not found.");
            }

            Message message = msgOpt.get();

            // ============================
            // ✅ Authorization Checks
            // ============================

            // ✅ Case 1: Direct message
            if (message.getReceiver() != null) {
                boolean isSender = message.getSender().getId().equals(user.getId());
                boolean isReceiver = message.getReceiver().getId().equals(user.getId());

                if (!isSender && !isReceiver) {
                    return ResponseEntity.status(403)
                            .body("You are not allowed to view this file.");
                }
            }

            // ✅ Case 2: Group message
            if (message.getGroup() != null) {
                boolean isMember = groupMemberBal
                        .getMembers(message.getGroup())
                        .stream()
                        .anyMatch(m -> m.getUser().getId().equals(user.getId()) && m.isIs_active());

                if (!isMember) {
                    return ResponseEntity.status(403)
                            .body("You are not allowed to view this file.");
                }
            }

            // ✅ Must be a protected file message
            if (message.getMessageType() == null ||
                (!message.getMessageType().getName().equals("image")
                    && !message.getMessageType().getName().equals("video")
                    && !message.getMessageType().getName().equals("audio")
                    && !message.getMessageType().getName().equals("file"))) {

                return ResponseEntity.badRequest()
                        .body("This message does not contain a downloadable file.");
            }

            // ✅ Filename stored in message.content
            String filename = message.getContent();

            if (filename == null || filename.isBlank()) {
                return ResponseEntity.badRequest().body("No file found for this message.");
            }

            Path filePath = Paths.get("protected_uploads").resolve(filename).normalize();

            if (!Files.exists(filePath)) {
                return ResponseEntity.badRequest().body("File no longer exists.");
            }

            // ✅ Return file as byte stream
            byte[] fileData = Files.readAllBytes(filePath);

            String mimeType = Files.probeContentType(filePath);
            if (mimeType == null) mimeType = "application/octet-stream";

            return ResponseEntity.ok()
                    .header("Content-Type", mimeType)
                    .header("Content-Disposition", "attachment; filename=\"" + filename + "\"")
                    .body(fileData);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error retrieving file: " + e.getMessage());
        }
    }

    // =====================================
    // 💬 2. Reply to a message
    // =====================================
    @PostMapping("/reply/{messageId}")
    @IsAuthenticatedUser
    public ResponseEntity<?> replyToMessage(
            @CurrentUser User sender,
            @PathVariable String messageId,
            @RequestBody MessageRequests.ReplyMessageRequest req) {

        try {
            Optional<Message> originalOpt = messageBal.getMessageById(messageId);
            if (originalOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Original message not found.");
            }

            if (req.getContent() == null || req.getContent().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Reply content cannot be empty.");
            }

            Message original = originalOpt.get();

            // 🚫 Block check (if direct message)
            if (original.getReceiver() != null &&
                (knownConnectionBal.isBlocked(sender, original.getReceiver()) ||
                 knownConnectionBal.isBlocked(original.getReceiver(), sender))) {
                return ResponseEntity.status(403).body("You cannot reply to this user (blocked).");
            }

            Message reply = new Message();
            reply.setSender(sender);
            reply.setContent(req.getContent());
            reply.setCaption(req.getCaption());
            reply.setReceiver(original.getReceiver());
            reply.setGroup(original.getGroup());
            reply.setReplyToMessage(original);

            Message savedReply = messageBal.replyToMessage(original, reply);

            // ✅ Update known connections if reply is direct
            if (savedReply.getReceiver() != null) {
                knownConnectionBal.updateConnectionOnMessage(sender, savedReply.getReceiver());
            }

            return ResponseEntity.ok(new MessageResponses.MessageResponse(savedReply));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error replying to message: " + e.getMessage());
        }
    }

    // =====================================
    // 💬 3. Get direct messages between two users
    // =====================================
    @GetMapping("/direct/{otherUserId}")
    @IsAuthenticatedUser
    public ResponseEntity<?> getDirectMessages(@CurrentUser User user, @PathVariable String otherUserId) {
        try {
            Optional<User> otherOpt = userRepository.findById(otherUserId);
            if (otherOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("User not found.");
            }
            User other = otherOpt.get();
            if (knownConnectionBal.isBlocked(user, other) || knownConnectionBal.isBlocked(other, user)) {
                return ResponseEntity.status(403).body("You cannot view messages with this user (blocked).");
            }

            // ✅ auto-mark delivered
            messageBal.markDeliveredDirect(otherUserId, user.getId());

            List<Message> conversation = messageBal.getDirectConversation(user.getId(), otherUserId);
            List<MessageResponses.MessageResponse> response = conversation
                    .stream().map(MessageResponses.MessageResponse::new).toList();
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error fetching messages: " + e.getMessage());
        }
    }

    @PostMapping("/mark-delivered/{otherUserId}")
    @IsAuthenticatedUser
    public ResponseEntity<?> markDelivered(@CurrentUser User user, @PathVariable String otherUserId) {
        try {
            Optional<User> otherOpt = userRepository.findById(otherUserId);
            if (otherOpt.isEmpty()) return ResponseEntity.badRequest().body("User not found.");

            int count = messageBal.markDeliveredDirect(otherUserId, user.getId()); // other -> current user
            return ResponseEntity.ok("Marked delivered: " + count);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error marking delivered: " + e.getMessage());
        }
    }

    @PostMapping("/mark-read/{otherUserId}")
    @IsAuthenticatedUser
    public ResponseEntity<?> markRead(@CurrentUser User user, @PathVariable String otherUserId) {
        try {
            Optional<User> otherOpt = userRepository.findById(otherUserId);
            if (otherOpt.isEmpty()) return ResponseEntity.badRequest().body("User not found.");

            // First ensure delivered (optional safeguard)
            messageBal.markDeliveredDirect(otherUserId, user.getId());

            int count = messageBal.markReadDirect(otherUserId, user.getId()); // other -> current user
            return ResponseEntity.ok("Marked read: " + count);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error marking read: " + e.getMessage());
        }
    }

    @GetMapping("/connections")
    @IsAuthenticatedUser
    public ResponseEntity<?> getConnections(@CurrentUser User user) {
        try {
            List<KnownConnection> connections = knownConnectionBal.getKnownConnections(user);

            List<MessageResponses.KnownConnectionResponse> response = connections.stream()
                    .map(MessageResponses.KnownConnectionResponse::new)
                    .toList();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error fetching connections: " + e.getMessage());
        }
    }

    @GetMapping("/search")
    @IsAuthenticatedUser
    public ResponseEntity<?> searchUser(@RequestParam String username) {
        try {
            if (username == null || username.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Search query cannot be empty.");
            }

            List<User> users = userRepository.findByUsernameContainingIgnoreCase(username.trim());
            List<UserResponses.UserResponse> results = users.stream()
                    .map(UserResponses.UserResponse::new)
                    .toList();

            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error searching user: " + e.getMessage());
        }
    }

    // =====================================
    // 🚫 6. Block a user
    // =====================================
    @PostMapping("/block/{contactId}")
    @IsAuthenticatedUser
    public ResponseEntity<?> blockUser(@CurrentUser User user, @PathVariable String contactId) {
        try {
            Optional<User> contactOpt = userRepository.findById(contactId);
            if (contactOpt.isEmpty()) return ResponseEntity.badRequest().body("User not found.");

            knownConnectionBal.blockUser(user, contactOpt.get());
            return ResponseEntity.ok("User blocked successfully.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error blocking user: " + e.getMessage());
        }
    }

    // =====================================
    // ✅ 7. Unblock a user
    // =====================================
    @PostMapping("/unblock/{contactId}")
    @IsAuthenticatedUser
    public ResponseEntity<?> unblockUser(@CurrentUser User user, @PathVariable String contactId) {
        try {
            Optional<User> contactOpt = userRepository.findById(contactId);
            if (contactOpt.isEmpty()) return ResponseEntity.badRequest().body("User not found.");

            knownConnectionBal.unblockUser(user, contactOpt.get());
            return ResponseEntity.ok("User unblocked successfully.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error unblocking user: " + e.getMessage());
        }
    }

    // =====================================
    // 💬 8. Delete message
    // =====================================
    @DeleteMapping("/{messageId}")
    @IsAuthenticatedUser
    public ResponseEntity<?> deleteMessage(@CurrentUser User user, @PathVariable String messageId) {
        try {
            Optional<Message> msgOpt = messageBal.getMessageById(messageId);
            if (msgOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Message not found.");
            }

            Message message = msgOpt.get();
            if (!message.getSender().getId().equals(user.getId())) {
                return ResponseEntity.status(403).body("You can only delete your own messages.");
            }

            messageBal.deleteMessage(messageId);
            return ResponseEntity.ok("Message deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error deleting message: " + e.getMessage());
        }
    }
}
