package com.ChatApp.DataAccess

import com.ChatApp.Repository.MessageRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MessageDal {
    private final MessageRepository messageRepository;
    
}
