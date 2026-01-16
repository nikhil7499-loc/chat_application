package com.ChatApp.BusinessAccess.MessageTypeBal;
import org.springframework.stereotype.Service;

import com.ChatApp.DataAccess.MessageTypesDal;
import com.ChatApp.Exceptions.ResourceNotFoundException;
import com.ChatApp.Repository.MessageTypesRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional(readOnly=true)
public class MessageTypesBal {
    private final MessageTypesDal messageTypesDal;

    public MeassageTypesBal(MessageTypesDal messageTypesDal){
        this.messageTypesDal=messageTypesDal;
    }

    public List<MEssageTypes> getAllMessageTypes(){
        return messageTypes.Dal.findAll();
    }

    public MessageType getMessageTypesId(short id){
        return messageTypesDal.findById(id);
    }

    public MessageTyps getMessageTypesByName(String name){
        if(name==null||name.isBlank()){
            throw new ResourceNotFoundException("Message type name cannot be null or blank.");
        }
        return messageTypesDal.findByName(name.toLowerCase().trim());
    }
}
