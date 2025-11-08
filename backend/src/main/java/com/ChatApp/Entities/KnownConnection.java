package com.ChatApp.Entities;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(
    name = "known_connections",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "contact_id"})
    }
)
public class KnownConnection {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    // The owner of this connection (the one whose known list this belongs to)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // The person or contact they have interacted with
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "contact_id", nullable = false)
    private User contact;

    // The timestamp of the most recent message exchanged
    @Column(name = "last_message_at", nullable = false)
    private Instant lastMessageAt;

    // Whether the contact is blocked and by whom
    @Column(name = "is_blocked", nullable = false)
    private Boolean isBlocked = false;

    @Column(name = "blocked_by_user_id")
    private String blockedByUserId;

    @Column(name = "blocked_at")
    private Instant blockedAt;

    // Whether this contact is marked as favorite
    @Column(name = "is_favorite", nullable = false)
    private Boolean isFavorite = false;

    // Optional: unread count for convenience
    @Column(name = "unread_count", nullable = false)
    private Integer unreadCount = 0;

    // ================================
    // Constructors
    // ================================
    public KnownConnection() {}

    public KnownConnection(User user, User contact) {
        this.user = user;
        this.contact = contact;
        this.lastMessageAt = Instant.now();
    }

    // ================================
    // Lifecycle hooks
    // ================================
    @PrePersist
    public void prePersist() {
        if (lastMessageAt == null) {
            lastMessageAt = Instant.now();
        }
        if (isFavorite == null) {
            isFavorite = false;
        }
        if (unreadCount == null) {
            unreadCount = 0;
        }
        if (isBlocked == null) {
            isBlocked = false;
        }
    }

    // ================================
    // Getters and Setters
    // ================================
    public String getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getContact() {
        return contact;
    }

    public void setContact(User contact) {
        this.contact = contact;
    }

    public Instant getLastMessageAt() {
        return lastMessageAt;
    }

    public void setLastMessageAt(Instant lastMessageAt) {
        this.lastMessageAt = lastMessageAt;
    }

    public Boolean getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(Boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public Integer getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(Integer unreadCount) {
        this.unreadCount = unreadCount;
    }

    public Boolean getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(Boolean isBlocked) {
        this.isBlocked = isBlocked;
    }

    public String getBlockedByUserId() {
        return blockedByUserId;
    }

    public void setBlockedByUserId(String blockedByUserId) {
        this.blockedByUserId = blockedByUserId;
    }

    public Instant getBlockedAt() {
        return blockedAt;
    }

    public void setBlockedAt(Instant blockedAt) {
        this.blockedAt = blockedAt;
    }
}
