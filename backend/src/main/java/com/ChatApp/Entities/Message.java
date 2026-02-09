package com.ChatApp.Entities;


import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.ManyToAny;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name="messages")
public class Message{
    @Id 
    @Column(length=36,nullable=false,updatable=false)
    private String id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="sender_id",nullable=false)
    private User sender;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="receiver_id")
    private User receiver;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="group_id")
    private User group;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="message_type_id",nullable=false)
    private String messageType;

    @Column(nullable=false, columnDefinition="text")
    private String content;

    @Column(length=500)
    private String caption;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="reply_to_message_id")
    private Message replyToMessage;

    @ManyToOne (fetch=FetchType.LAZY)
    @JoinColumn(name="mentioned_user_id")
    private User mentionUser;

    @Column(name="sent_at",nullable=false,columnDefinition="timestamp")
    private Instant sentAt;

    @Column(name="read_at",columnDefinition="timestamp")
    private Instant readAt;

    @Column(name="delivered_at",columnDefinition="timestamp")
    private Instant deliveredAt;

    @PrePersist
    protected void onCreate(){
        if(this.id==null){
            this.id=UUID.randomUUID().toString();
        }
        this.sentAt=Instant.now();
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
     * @return User return the sender
     */
    public User getSender() {
        return sender;
    }

    /**
     * @param sender the sender to set
     */
    public void setSender(User sender) {
        this.sender = sender;
    }

    /**
     * @return User return the receiver
     */
    public User getReceiver() {
        return receiver;
    }

    /**
     * @param receiver the receiver to set
     */
    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    /**
     * @return User return the group
     */
    public User getGroup() {
        return group;
    }

    /**
     * @param group the group to set
     */
    public void setGroup(User group) {
        this.group = group;
    }

    /**
     * @return String return the messageType
     */
    public String getMessageType() {
        return messageType;
    }

    /**
     * @param messageType the messageType to set
     */
    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    /**
     * @return String return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return string return the caption
     */
    public String getCaption() {
        return caption;
    }

    /**
     * @param caption the caption to set
     */
    public void setCaption(String caption) {
        this.caption = caption;
    }

    /**
     * @return Message return the replyToMessage
     */
    public Message getReplyToMessage() {
        return replyToMessage;
    }

    /**
     * @param replyToMessage the replyToMessage to set
     */
    public void setReplyToMessage(Message replyToMessage) {
        this.replyToMessage = replyToMessage;
    }

    /**
     * @return User return the mentioUser
     */
    public User getMentioUser() {
        return mentionUser;
    }

    /**
     * @param mentioUser the mentioUser to set
     */
    public void setMentionUser(User mentionUser) {
        this.mentionUser = mentionUser;
    }

    /**
     * @return Instant return the sentAt
     */
    public Instant getSentAt() {
        return sentAt;
    }

    /**
     * @param sentAt the sentAt to set
     */
    public void setSentAt(Instant sentAt) {
        this.sentAt = sentAt;
    }

    /**
     * @return Instant return the readAt
     */
    public Instant getReadAt() {
        return readAt;
    }

    /**
     * @param readAt the readAt to set
     */
    public void setReadAt(Instant readAt) {
        this.readAt = readAt;
    }

    /**
     * @return Instant return the deliveredAt
     */
    public Instant getDeliveredAt() {
        return deliveredAt;
    }

    /**
     * @param deliveredAt the deliveredAt to set
     */
    public void setDeliveredAt(Instant deliveredAt) {
        this.deliveredAt = deliveredAt;
    }

}