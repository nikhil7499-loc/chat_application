package com.ChatApp.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ChatApp.Entities.Message;


@RestController
@RequestMapping("/mssg")
public class MessageController {
    private final Message message;

    @Autowired
    public MessageController(Message message){
        this.message=message;
    }

    
}
