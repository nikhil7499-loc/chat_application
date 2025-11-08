package com.ChatApp.Models.Requests;

import java.util.regex.Pattern;

public class MessageRequests {

    // ========================
    // Send Message
    // ========================
    public static class SendMessageRequest {
        private String content;
        private String type;        // e.g., "text", "image", "video"
        private String receiverId;  // for direct messages
        private String groupId;     // for group messages
        private String caption;     // optional caption for media
        private String replyToMessageId; // optional reply reference

        private static final Pattern MESSAGE_TYPE_PATTERN =
                Pattern.compile("^[a-zA-Z0-9_-]{3,30}$");

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            if (content == null || content.trim().isEmpty()) {
                throw new IllegalArgumentException("Message content cannot be empty.");
            }
            this.content = content.trim();
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            if (type != null && !MESSAGE_TYPE_PATTERN.matcher(type).matches()) {
                throw new IllegalArgumentException("Invalid message type format.");
            }
            this.type = (type == null || type.isBlank()) ? "text" : type.toLowerCase();
        }

        public String getReceiverId() {
            return receiverId;
        }

        public void setReceiverId(String receiverId) {
            this.receiverId = receiverId;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public String getCaption() {
            return caption;
        }

        public void setCaption(String caption) {
            this.caption = caption;
        }

        public String getReplyToMessageId() {
            return replyToMessageId;
        }

        public void setReplyToMessageId(String replyToMessageId) {
            this.replyToMessageId = replyToMessageId;
        }
    }

    // ========================
    // Reply to a Message
    // ========================
    public static class ReplyMessageRequest {
        private String content;
        private String caption;
        private String replyToMessageId;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            if (content == null || content.trim().isEmpty()) {
                throw new IllegalArgumentException("Reply content cannot be empty.");
            }
            this.content = content.trim();
        }

        public String getCaption() {
            return caption;
        }

        public void setCaption(String caption) {
            this.caption = caption;
        }

        public String getReplyToMessageId() {
            return replyToMessageId;
        }

        public void setReplyToMessageId(String replyToMessageId) {
            this.replyToMessageId = replyToMessageId;
        }
    }
}
