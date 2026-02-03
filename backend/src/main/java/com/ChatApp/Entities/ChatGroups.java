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

    



    /**
     * @return Short return the id
     */
    public Short getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Short id) {
        this.id = id;
    }

    /**
     * @return String return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return String return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return User return the createdBy
     */
    public User getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy the createdBy to set
     */
    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * @return Instant return the createdAt
     */
    public Instant getCreatedAt() {
        return createdAt;
    }

    /**
     * @param createdAt the createdAt to set
     */
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * @return Instant return the updatedAt
     */
    public Instant getUpdatedAt() {
        return updatedAt;
    }

    /**
     * @param updatedAt the updatedAt to set
     */
    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

}