package com.ChatApp.BusinessAccess;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ChatApp.DataAccess.MessageTypesDal;
import com.ChatApp.Entities.MessageTypes;
import com.ChatApp.Exceptions.ResourceNotFoundException;

@Service
@Transactional(readOnly=true)
public class MessageTypesBal {
    private final MessageTypesDal messageTypesDal;

    @Autowired
    public MessageTypesBal(MessageTypesDal _messageTypesDal){
        this.messageTypesDal=_messageTypesDal;
    }

    public List<MessageTypes> getAllMessageTypes(){
        return messageTypesDal.findAll();
    }

    public MessageTypes getMessageTypesId(short id){
        return messageTypesDal.findById(id);
    }

    public MessageTypes getMessageTypesByName(String name){
        if(name==null||name.isBlank()){
            throw new ResourceNotFoundException("Message type name cannot be null or blank.");
        }
        return messageTypesDal.findByName(name.toLowerCase().trim());
    }
}
