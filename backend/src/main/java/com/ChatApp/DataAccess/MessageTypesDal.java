package com.ChatApp.DataAccess;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.ChatApp.Entities.MessageTypes;
import com.ChatApp.Exceptions.DatabaseOperationException;
import com.ChatApp.Exceptions.ResourceNotFoundException;
import com.ChatApp.Repository.MessageTypesRepository;

public class MessageTypesDal {

    private final MessageTypesRepository messageTypesRepository;

    @Autowired
    public MessageTypesDal(MessageTypesRepository _messageTypesRepository){
        this.messageTypesRepository = _messageTypesRepository;
    }
    public List<MessageTypes> findAll(){
        try {
            return messageTypesRepository.findAll();
        } catch (Exception e) {
            throw  new  DatabaseOperationException("Failed to fetch meassage types",e);
        }
    }

    public MessageTypes findById(short id){
        try {
            return messageTypesRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Message type not found with ID: "+id));
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to fetch message type by ID",e);
        }
    }
    public MessageTypes findByName(String name){
        try {
            return messageTypesRepository.findByName(name).orElseThrow(()-> new ResourceNotFoundException("Message type not found with name: "+name));
        } catch (ResourceNotFoundException e) {
            throw e;
        }catch(Exception e){
            throw new DatabaseOperationException("failed to fetch message type by name",e);
        }
    }    
}
