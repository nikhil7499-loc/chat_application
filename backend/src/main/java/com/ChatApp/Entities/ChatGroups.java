package com.ChatApp.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="chat_groups")
public class ChatGroups{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id",nullable=false,length=36)
    private Short i;

    @Column(nullable=false,length=255)
    private String name;

    @Column(name="description",length=500)
    private String description;

    
}