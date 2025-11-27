package com.ChatApp.Entities;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;



@Entity
@Table(name="user")
public class User {

    public enum Gender{
        male,
        female,
        other
    }

    @Id
    @Column(nullable=false, updatable=false, length=36)
    private String id;

    @Column(nullable=false, unique=true, length=256)
    private String username;

    @Column(nullable=false, length=256)
    private String password;

    @Column(nullable=false, unique=true)
    private String email;

    @Column(nullable=false, name="date_of_birth")
    private Date DateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name="gender")
    private Gender gender;

    @Column(length=128, name="profile_picture")
    private String ProfilePicture;

    @Column(nullable=false, columnDefinition="TIMESTAMP", name="created_at")
    private Instant CreatedAt;

    @Column(columnDefinition="TIMESTAMP", name="updated_at")
    private Instant UpdatedAt;

    @PrePersist
    public void onCreate(){
        if(this.id==null){
            this.id=UUID.randomUUID().toString();
        }

        Instant nowUtc=Instant.now();
        this.CreatedAt=nowUtc;
        this.UpdatedAt=nowUtc;
    }

    @PreUpdate
    public void onUpdate(){
        Instant nowUtc=Instant.now();
        this.UpdatedAt=nowUtc;
    }

    public String getId(){
        return this.id;
    }

    public void setId(String id){
        this.id=id;
    }

    public String getUsername(){
        return this.username;
    }

    public void setUsername(String username){
        this.username=username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDateOfBirth() {
        return DateOfBirth;
    }

    public void setDateOfBirth(Date date_of_birth) {
        this.DateOfBirth = date_of_birth;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getProfilePicture() {
        return ProfilePicture;
    }

    public void setProfilePicture(String profile_picture) {
        this.ProfilePicture = profile_picture;
    }

    public Instant getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(Instant created_at) {
        this.CreatedAt = created_at;
    }

    public Instant getUpdatedAt() {
        return UpdatedAt;
    }

    public void setUpdatedAt(Instant updated_at) {
        this.UpdatedAt = updated_at;
    }

}
