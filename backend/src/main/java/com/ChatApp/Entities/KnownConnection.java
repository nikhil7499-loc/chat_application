package com.ChatApp.Entities;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;


@Entity
@Table(name="known_connection",
    uniqueConstraints={
        @UniqueConstraint(columnNames={
            "user_id","contact_id"
        })
    }
)
public class KnownConnection {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(nullable=false,updatable=false)
    private String id; 

    @ManyToOne(fetch=FetchType.LAZY,optional=false)
    @JoinColumn(name="user_id",nullable=false)
    private User user;

    @ManyToOne(fetch=FetchType.LAZY,optional=false)
    @JoinColumn(name="contact_id",nullable=false)
    private User contact;

    @Column(name="last_message_at",nullable=false)
    private Instant lastMessageAt;

    @Column(name="is_blocked",nullable=false)
    private Boolean  isBlocked=false;

    @Column(name="blocked_by_user_id")
    private String blockedByUserId;

    @Column(name="blocked_by_user_id")
    private Instant blockedByUser;

    @Column(name="is_favourite",nullable=false)
    private Boolean isFavorite=false;
    
    @Column(name="unread_count",nullable=false)
    private Integer unreadCount=0;

    public KnownConnection(){};

    public KnownConnection(User user, User contact) {
        this.user = user;
        this.contact = contact;
        this.lastMessageAt = Instant.now();
    }

    @PrePersist
    public void prePersist(){

        if(lastMessageAt==null){
            lastMessageAt=Instant.now();
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
    

    /**
     * @return String return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return User return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * @return User return the contact
     */
    public User getContact() {
        return contact;
    }

    /**
     * @param contact the contact to set
     */
    public void setContact(User contact) {
        this.contact = contact;
    }

    /**
     * @return Instant return the lastMessageAt
     */
    public Instant getLastMessageAt() {
        return lastMessageAt;
    }

    /**
     * @param lastMessageAt the lastMessageAt to set
     */
    public void setLastMessageAt(Instant lastMessageAt) {
        this.lastMessageAt = lastMessageAt;
    }

    /**
     * @return Boolean return the isBlocked
     */
    public Boolean isIsBlocked() {
        return isBlocked;
    }

    /**
     * @param isBlocked the isBlocked to set
     */
    public void setIsBlocked(Boolean isBlocked) {
        this.isBlocked = isBlocked;
    }

    /**
     * @return String return the blockedByuserId
     */
    public String getBlockedByUserId() {
        return blockedByUserId;
    }

    /**
     * @param blockedByuserId the blockedByuserId to set
     */
    public void setBlockedByUserId(String blockedByUserId) {
        this.blockedByUserId = blockedByUserId;
    }

    /**
     * @return Instant return the blockedByUser
     */
    public Instant getBlockedByUser() {
        return blockedByUser;
    }

    /**
     * @param blockedByUser the blockedByUser to set
     */
    public void setBlockedByUser(Instant blockedByUser) {
        this.blockedByUser = blockedByUser;
    }

    /**
     * @return Boolean return the isFavorite
     */
    public Boolean isIsFavorite() {
        return isFavorite;
    }

    /**
     * @param isFavorite the isFavorite to set
     */
    public void setIsFavorite(Boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    /**
     * @return Integer return the unreadCount
     */
    public Integer getUnreadCount() {
        return unreadCount;
    }

    /**
     * @param unreadCount the unreadCount to set
     */
    public void setUnreadCount(Integer unreadCount) {
        this.unreadCount = unreadCount;
    }

}
