package com.ChatApp.Entities;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

import jakarta.persistence.Entity;



@Entity
public class User {
    private UUID id;
    private String username;
    private String password;
    private String email;
    private Date date_of_birth;
    private String gender;
    private String profile_picture;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public UUID getId(){
        return this.id;
    }

    public void setId(UUID id){
        this.id=id;
    }
    public 



}
