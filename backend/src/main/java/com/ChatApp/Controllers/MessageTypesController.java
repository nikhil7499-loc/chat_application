package com.ChatApp.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.ChatApp.BusinessAccess.MessageTypesBal;
import com.ChatApp.Entities.MessageTypes;
import com.ChatApp.Exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/message-types")
public class MessageTypesController {

    private final MessageTypesBal messageTypesBal;

    @Autowired
    public MessageTypesController(MessageTypesBal messageTypesBal) {
        this.messageTypesBal = messageTypesBal;
    }

    /**
     * Get all available message types.
     * Example: GET /message-types
     */
    @GetMapping
    public ResponseEntity<?> getAllMessageTypes() {
        try {
            List<MessageTypes> types = messageTypesBal.getAllMessageTypes();
            return ResponseEntity.ok(types);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Failed to fetch message types", "details", e.getMessage()));
        }
    }

    /**
     * Get a message type by its ID.
     * Example: GET /message-types/1
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getMessageTypeById(@PathVariable("id") short id) {
        try {
            MessageTypes type = messageTypesBal.getMessageTypeById(id);
            return ResponseEntity.ok(type);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Failed to fetch message type by ID", "details", e.getMessage()));
        }
    }

    /**
     * Get a message type by its name.
     * Example: GET /message-types/name/text
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<?> getMessageTypeByName(@PathVariable("name") String name) {
        try {
            MessageTypes type = messageTypesBal.getMessageTypeByName(name);
            return ResponseEntity.ok(type);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Failed to fetch message type by name", "details", e.getMessage()));
        }
    }
}
