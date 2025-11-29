package com.ChatApp.Entities;


import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name="otp")
public class Otp {
    @Id
    @Column(nullable=false, updatable=false, length=36)
    private String id;

    @Column(nullable=false,updatable=false,length=10)
    private String code;
    
    @ManyToOne(fetch=FetchType.LAZY,optional=false)
    @JoinColumn(
        name="user_id",
        nullable=false,
        referencedColumnName="id",
        foreignKey=@ForeignKey(name="fk_otp_user")
    )
    private User user;

    @Column(nullable=false, name="is_used")
    private boolean isUsed=false;

    @Column(nullable=false, columnDefinition="TIMESTAMP", name="created_at")
    private Instant createdAt;

    @Column(columnDefinition="TIMESTAMP",nullable=false, name="expires_at")
    private Instant expiresAt;

    @PrePersist
    public void onCreate(){
        if(this.id==null){
            this.id=UUID.randomUUID().toString();
        }

        Instant nowUtc=Instant.now();
        this.createdAt=nowUtc;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean GetIsUsed() {
        return isUsed;
    }

    public void setIsUsed(boolean is_used) {
        this.isUsed = is_used;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant created_at) {
        this.createdAt = created_at;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expires_at) {
        this.expiresAt = expires_at;
    }

}
