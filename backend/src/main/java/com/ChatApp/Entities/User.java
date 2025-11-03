package com.ChatApp.Entities;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
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

    @Column(nullable=false)
    private Date date_of_birth;

    @Column(EnumType.STRING)
    private Gender gender;

    @Column(length=128)
    private String profile_picture;

    @Column(nullable=false, columnDefinition="TIMESTAMP")
    private Instant created_at;

    @Column(columnDefinition="TIMESTAMP")
    private Instant updated_at;

    @PrePersist
    public void onCreate(){
        if(this.id==null){
            this.id=UUID.randomUUID().toString();
        }

        Instant nowUtc=Instant.now();
        this.created_at=nowUtc;
        this.updated_at=nowUtc;
    }

    @PreUpdate
    public void onUpdate(){
        Instant nowUtc=Instant.now();
        this.updated_at=nowUtc;
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

    public Date getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(Date date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public Instant getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Instant created_at) {
        this.created_at = created_at;
    }

    public Instant getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Instant updated_at) {
        this.updated_at = updated_at;
    }

}
