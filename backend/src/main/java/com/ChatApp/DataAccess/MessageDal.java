package com.ChatApp.DataAccess

import org.springframework.stereotype.Component;

import com.ChatApp.Repository.MessageRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Component
@Transactional
public class MessageDal {
    private final MessageRepository messageRepository;

    publlic MessageDal(MessageRepository messageRepository){
        this.messageRepository=messageRepository;
    }
    
}
