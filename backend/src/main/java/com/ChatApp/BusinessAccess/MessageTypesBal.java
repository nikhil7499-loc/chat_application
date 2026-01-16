package com.ChatApp.BusinessAccess.MessageTypeBal;
import org.springframework.stereotype.Service;

import com.ChatApp.DataAccess.MessageTypesDal;
import com.ChatApp.Repository.MessageTypesRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional(readOnly=true)
public class MessageTypesBal {
    private final MessageTypesDal messageTypesDal;

    public MeassageTypesBal(MessageTypesDal messageTypesDal){
        this.messageTypesDal=messageTypesDal;
    }
}
