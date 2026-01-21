package com.ChatApp.Controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ChatApp.BusinessAccess.MessageTypeBal.MessageTypesBal;

@RestController
@RequestMapping("/message-types")
public class MessageTypesController {

    private final MessageTypesBal messageTypesBal;

    public MessageTypesController(MessageTypesBal _messageTypesBal) {
        this.messageTypesBal = _messageTypesBal;
    }


    
    
}
