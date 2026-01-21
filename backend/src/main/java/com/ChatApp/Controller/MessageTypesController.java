package com.ChatApp.Controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ChatApp.BusinessAccess.MessageTypeBal.MessageTypesBal;
import com.ChatApp.Entities.MessageTypes;

@RestController
@RequestMapping("/message-types")
public class MessageTypesController {

    private final MessageTypesBal messageTypesBal;

    public MessageTypesController(MessageTypesBal _messageTypesBal) {
        this.messageTypesBal = _messageTypesBal;
    }


    @GetMapping("/all")
    public ResponseEntity<?> getAllTypes(){
        try{
            List<MessageTypes> allTypes= messageTypesBal.getAllMessageTypes();
            return ResponseEntity.ok(allTypes);
        }catch (Exception e) {
              return ResponseEntity.internalServerError().body("something went wrong "+e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") short id){
        try{
            MessageTypes type= messageTypesBal.getMessageTypesId(id);
            return ResponseEntity.ok(type);
        }catch (Exception e) {
              return ResponseEntity.internalServerError().body("something went wrong "+e.getMessage());
        }
    }


    @GetMapping("/name/{name}")
    public ResponseEntity<?> getByName(@PathVariable("name") String name){
        try{
            MessageTypes type= messageTypesBal.getMessageTypesByName(name);
            return ResponseEntity.ok(type);
        }catch (Exception e) {
              return ResponseEntity.internalServerError().body("something went wrong "+e.getMessage());
        }
    }
    
}
