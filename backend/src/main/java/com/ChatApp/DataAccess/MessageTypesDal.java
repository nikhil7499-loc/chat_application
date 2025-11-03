package com.ChatApp.DataAccess;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ChatApp.Entities.MessageTypes;
import com.ChatApp.Exceptions.ResourceNotFoundException;
import com.ChatApp.Exceptions.DatabaseOperationException;
import com.ChatApp.Repository.MessageTypesRepository;

@Component
@Transactional(readOnly = true)
public class MessageTypesDal {

    private final MessageTypesRepository messageTypesRepository;

    public MessageTypesDal(MessageTypesRepository messageTypesRepository) {
        this.messageTypesRepository = messageTypesRepository;
    }

    public List<MessageTypes> findAll() {
        try {
            return messageTypesRepository.findAll();
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to fetch message types", e);
        }
    }

    public MessageTypes findById(short id) {
        try {
            return messageTypesRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Message type not found with ID: " + id));
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to fetch message type by ID", e);
        }
    }

    public MessageTypes findByName(String name) {
        try {
            return messageTypesRepository.findByName(name)
                    .orElseThrow(() -> new ResourceNotFoundException("Message type not found with name: " + name));
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to fetch message type by name", e);
        }
    }
}
