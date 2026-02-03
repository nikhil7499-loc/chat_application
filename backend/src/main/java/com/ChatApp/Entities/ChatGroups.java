package com.ChatApp.Entities;
import java.util.UUID;
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














@Entity
@Table(name="chat_groups")
public class ChatGroups{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id",nullable=false,length=36,updatable=false)
    private Short id;

    @Column(nullable=false,length=255)
    private String name;

    @Column(name="description",length=500)
    private String description;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="created_by",nullable=false,)
    private User createdBy;

    @Column(name="created_at",nullable=false,columnDefinition="timestamp")
    private Instant createdAt;
    @Column(name="updated_at",nullable=false,columnDefination="timestamp")
    private Instant updatedAt;
    
    
    @PrePersist
    protected void oncreate(){
        if(this.id==null){
            this.id=UUID.randomUUID().toString();
        }
        Instant nowUtc=Instant.now();
        this.createdAt=nowUtc;
        this.updatedAt=nowUtc;

    }

    @PrePersist
    protected void onUpdate(){
        this.updatedupdatedAt=Instant.now();
    }

    
}