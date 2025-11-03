package com.ChatApp.Entities;


import java.time.Instant;
import java.util.UUID;

import com.ChatApp.Entities.User;

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

    @Column(nullable=false)
    private boolean is_used=false;

    @Column(nullable=false, columnDefinition="TIMESTAMP")
    private Instant created_at;

    @Column(columnDefinition="TIMESTAMP",nullable=false)
    private Instant expires_at;

    @PrePersist
    public void onCreate(){
        if(this.id==null){
            this.id=UUID.randomUUID().toString();
        }

        Instant nowUtc=Instant.now();
        this.created_at=nowUtc;
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

    public boolean isIs_used() {
        return is_used;
    }

    public void setIs_used(boolean is_used) {
        this.is_used = is_used;
    }

    public Instant getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Instant created_at) {
        this.created_at = created_at;
    }

    public Instant getExpires_at() {
        return expires_at;
    }

    public void setExpires_at(Instant expires_at) {
        this.expires_at = expires_at;
    }

}
