package com.ChatApp.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ChatApp.Entities.MessageBal;


@RestController
@RequestMapping("/mssg")
public class MessageController {
    private final MessageBal messageBal;

    @Autowired
    public MessageController(MessageBal messageBal){
        this.messageBal=messageBal;
    }

    
}
