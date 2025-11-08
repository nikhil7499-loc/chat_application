package com.ChatApp.Models.Responses;

import com.ChatApp.Entities.*;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class MessageResponses {

    // ========================
    // MessageResponse
    // ========================
    public static class MessageResponse {
        private String id;
        private String content;
        private String caption;
        private String type;
        private SenderInfo sender;
        private ReceiverInfo receiver;
        private GroupInfo group;
        private LocalDateTime sentAt;
        private LocalDateTime deliveredAt;
        private LocalDateTime readAt;

        public MessageResponse(Message message) {
            this.id = message.getId();
            this.content = message.getContent();
            this.caption = message.getCaption();
            this.type = (message.getMessageType() != null && message.getMessageType().getName() != null)
                    ? message.getMessageType().getName()
                    : "text";

            if (message.getSender() != null)
                this.sender = new SenderInfo(message.getSender());

            if (message.getReceiver() != null)
                this.receiver = new ReceiverInfo(message.getReceiver());

            if (message.getGroup() != null)
                this.group = new GroupInfo(message.getGroup());

            this.sentAt = (message.getSentAt() != null)
                    ? LocalDateTime.ofInstant(message.getSentAt(), ZoneId.systemDefault())
                    : null;
            this.deliveredAt = (message.getDeliveredAt() != null)
                    ? LocalDateTime.ofInstant(message.getDeliveredAt(), ZoneId.systemDefault())
                    : null;
            this.readAt = (message.getReadAt() != null)
                    ? LocalDateTime.ofInstant(message.getReadAt(), ZoneId.systemDefault())
                    : null;
        }

        public String getId() { return id; }
        public String getContent() { return content; }
        public String getType() { return type; }
        public SenderInfo getSender() { return sender; }
        public String getCaption() { return caption; }
        public ReceiverInfo getReceiver() { return receiver; }
        public GroupInfo getGroup() { return group; }
        public LocalDateTime getSentAt() { return sentAt; }
        public LocalDateTime getDeliveredAt() { return deliveredAt; }
        public LocalDateTime getReadAt() { return readAt; }
    }

    // ========================
    // Compact sender info
    // ========================
    public static class SenderInfo {
        private String id;
        private String username;
        private String name;
        private String profilePicture;

        public SenderInfo(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.name = user.getName();
            this.profilePicture = user.getProfile_picture();
        }

        public String getId() { return id; }
        public String getUsername() { return username; }
        public String getName() { return name; }
        public String getProfilePicture() { return profilePicture; }
    }

    // ========================
    // Compact receiver info
    // ========================
    public static class ReceiverInfo {
        private String id;
        private String username;
        private String name;
        private String profilePicture;

        public ReceiverInfo(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.name = user.getName();
            this.profilePicture = user.getProfile_picture();
        }

        public String getId() { return id; }
        public String getUsername() { return username; }
        public String getName() { return name; }
        public String getProfilePicture() { return profilePicture; }
    }

    // ========================
    // Compact group info
    // ========================
    public static class GroupInfo {
        private String id;
        private String name;
        private String description;

        public GroupInfo(ChatGroup group) {
            this.id = group.getId();
            this.name = group.getName();
            this.description = group.getDescription();
        }

        public String getId() { return id; }
        public String getName() { return name; }
        public String getDescription() { return description; }
    }

    // ========================
    // ✅ Known Connection DTO
    // ========================
    public static class KnownConnectionResponse {
        private String id;
        private ContactInfo contact;
        private boolean isBlocked;
        private String blockedByUserId;
        private LocalDateTime blockedAt;
        private boolean isFavorite;
        private int unreadCount;
        private LocalDateTime lastMessageAt;

        public KnownConnectionResponse(KnownConnection connection) {
            this.id = connection.getId();

            if (connection.getContact() != null)
                this.contact = new ContactInfo(connection.getContact());

            this.isBlocked = Boolean.TRUE.equals(connection.getIsBlocked());
            this.blockedByUserId = connection.getBlockedByUserId();
            this.blockedAt = (connection.getBlockedAt() != null)
                    ? LocalDateTime.ofInstant(connection.getBlockedAt(), ZoneId.systemDefault())
                    : null;
            this.isFavorite = Boolean.TRUE.equals(connection.getIsFavorite());
            this.unreadCount = connection.getUnreadCount() != null ? connection.getUnreadCount() : 0;
            this.lastMessageAt = (connection.getLastMessageAt() != null)
                    ? LocalDateTime.ofInstant(connection.getLastMessageAt(), ZoneId.systemDefault())
                    : null;
        }

        public String getId() { return id; }
        public ContactInfo getContact() { return contact; }
        public boolean getIsBlocked() { return isBlocked; }
        public String getBlockedByUserId() { return blockedByUserId; }
        public LocalDateTime getBlockedAt() { return blockedAt; }
        public boolean getIsFavorite() { return isFavorite; }
        public int getUnreadCount() { return unreadCount; }
        public LocalDateTime getLastMessageAt() { return lastMessageAt; }
    }

    // ========================
    // Contact info (used inside KnownConnectionResponse)
    // ========================
    public static class ContactInfo {
        private String id;
        private String username;
        private String name;
        private String profilePicture;

        public ContactInfo(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.name = user.getName();
            this.profilePicture = user.getProfile_picture();
        }

        public String getId() { return id; }
        public String getUsername() { return username; }
        public String getName() { return name; }
        public String getProfilePicture() { return profilePicture; }
    }
}
