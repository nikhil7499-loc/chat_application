package com.ChatApp.DataAccess;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ChatApp.Entities.ChatGroup;
import com.ChatApp.Entities.Message;
import com.ChatApp.Entities.User;
import com.ChatApp.Exceptions.DatabaseOperationException;
import com.ChatApp.Exceptions.ResourceNotFoundException;
import com.ChatApp.Repository.MessageRepository;

@Component
@Transactional
public class MessageDal {

    private final MessageRepository messageRepository;

    public MessageDal(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    // ✅ Save or update message
    public Message save(Message message) {
        try {
            return messageRepository.save(message);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseOperationException("Constraint violation while saving message", e);
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to save message", e);
        }
    }

    // ✅ Find by sender
    @Transactional(readOnly = true)
    public List<Message> findBySender(User sender) {
        try {
            return messageRepository.findBySender(sender);
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to fetch messages by sender", e);
        }
    }

    // ✅ Find by receiver (keeps same DAL function name)
    @Transactional(readOnly = true)
    public List<Message> findByReceiver(User receiver) {
        try {
            // ✅ Use the correct property-based repo method
            return messageRepository.findByReceiverOrderBySentAtAsc(receiver);
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to fetch messages by receiver", e);
        }
    }

    // ✅ Find group messages
    @Transactional(readOnly = true)
    public List<Message> findByGroup(ChatGroup group) {
        try {
            return messageRepository.findByGroupOrderBySentAtAsc(group);
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to fetch group messages", e);
        }
    }

    // ✅ Get direct conversation
    @Transactional(readOnly = true)
    public List<Message> findDirectConversation(String userId, String otherUserId) {
        try {
            return messageRepository.findDirectConversation(userId, otherUserId);
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to fetch direct conversation", e);
        }
    }

    // ✅ Find by ID
    @Transactional(readOnly = true)
    public Optional<Message> findById(String id) {
        try {
            return messageRepository.findById(id);
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to fetch message by ID", e);
        }
    }

    // ✅ Delete
    public void deleteById(String id) {
        try {
            messageRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Message not found for deletion with ID: " + id);
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to delete message", e);
        }
    }

    @Transactional
    public int bulkMarkDeliveredDirect(String senderId, String receiverId, Instant ts) {
        try {
            return messageRepository.bulkMarkDeliveredDirect(senderId, receiverId, ts);
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to bulk mark delivered", e);
        }
    }

    @Transactional
    public int bulkMarkReadDirect(String senderId, String receiverId, Instant ts) {
        try {
            return messageRepository.bulkMarkReadDirect(senderId, receiverId, ts);
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to bulk mark read", e);
        }
    }
}
