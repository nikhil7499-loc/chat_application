package com.ChatApp.Entities;
import java.time.Instant;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "message_types")
public class MessageTypes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Short id;

    @Column(nullable = false, updatable = true, length = 256)
    private String name;

    @Column(length = 256)
    private String description;

    @Column(name = "allowed_extensions", length = 255)
    private String allowedExtensions;

    @Column(nullable = false, columnDefinition = "TIMESTAMP", name = "created_at")
    private Instant createdAt;

    @PrePersist
    public void onCreate(){
        this.createdAt = Instant.now();
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
     * @return String return the allowedExtensions
     */
    public String getAllowedExtensions() {
        return allowedExtensions;
    }

    /**
     * @param allowedExtensions the allowedExtensions to set
     */
    public void setAllowedExtensions(String allowedExtensions) {
        this.allowedExtensions = allowedExtensions;
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

}
