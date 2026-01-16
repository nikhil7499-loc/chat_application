package com.ChatApp.DataAccess;

import org.springframework.beans.factory.annotation.Autowired;

import com.ChatApp.Repository.MessageTypesRepository;

public class MessageTypesDal {

    private MessageTypesRepository messageTypesRepository;

    @Autowired
    public MessageTypesDal(MessageTypesRepository _messageTypesRepository){
        this.messageTypesRepository = _messageTypesRepository;
    }


    
}
