package com.ChatApp.DataAccess;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import com.ChatApp.Entities.Message;
import com.ChatApp.Entities.User;
import com.ChatApp.Exceptions.DatabaseOperationException;
import com.ChatApp.Exceptions.ResourceNotFoundException;
import com.ChatApp.Repository.MessageRepository;

import jakarta.transaction.Transactional;

@Component
@Transactional
public class MessageDal {
    private final MessageRepository messageRepository;

    public MessageDal(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message save(Message message) {
        try {
            return messageRepository.save(message);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseOperationException("constraint violation while saving message", e);
        } catch (Exception e) {
            throw new DatabaseOperationException("failed to save message", e);
        }
    }

    // find by sender
    public List<Message> findBySender(User sender) {
        try {
            return messageRepository.findBySender(sender);
        } catch (Exception e) {
            throw new DatabaseOperationException("failed to fetch message by sender", e);
        }
    }

    // find by receiver (keep same DAL function name)
    public List<Message> findByReceiver(User receiver) {
        try {
            return messageRepository.findByRecieverOrderBySentAtAsc(receiver);
        } catch (Exception e) {
            throw new DatabaseOperationException("failed to fetch message by receiver",e);
        }
    }

    //get direct conversation
    public List<Message> findDirectConversation(String userId,String otherUserId){
        try{
            return messageRepository.findDirectConversation(userId,otherUserId);
        }catch(Exception e){
            throw new DatabaseOperationException("failed to fetch direct conversation",e);
        }
    }

    // find by ID
    public Optional<Message>findById(String id){
        try{
            return messageRepository.findById(id);
        }catch(Exception e){
            throw new DatabaseOperationException("Failed to fetch message by ID",e);
        }
    }

    //delete 

    public void deleteById(String id){
        try{
            messageRepository.deleteById(id);
        }catch(EmptyResultDataAccessException e){
            throw new ResourceNotFoundException("message not found for deletion with ID"+id);
        }catch(Exception e){
            throw new DatabaseOperationException("Failed to delete message",e);
        }
    }

    @Transactional
    public int bulkMarkReadDirect(String senderId,String receiverId,Instant ts){
        try{
            return messageRepository.bulkMarkReadDirect(senderId,receiverId,ts);
        }catch(Exception e){
            throw new DatabaseOperationException("Failed to bulk makr read",e);
        }
    }

}
