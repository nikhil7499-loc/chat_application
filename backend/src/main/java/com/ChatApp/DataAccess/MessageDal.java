package com.ChatApp.DataAccess;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import com.ChatApp.Entities.ChatGroup;
import com.ChatApp.Entities.Message;
import com.ChatApp.Entities.User;
import com.ChatApp.Exceptions.DatabaseOperationException;
import com.ChatApp.Repository.MessageRepository;
import com.mailjet.client.resource.Sender;

import jakarta.transaction.Transactional;


@Component
@Transactional
public class MessageDal {
    private final MessageRepository messageRepository;

    public MessageDal(MessageRepository messageRepository){
        this.messageRepository=messageRepository;
    }

    public Message save(Message message){
        try {
            return messageRepository.save(message);
        } catch ( DataIntergrityViolationException e) {
            throw new DatabaseOperationException("Constraint violation while saving message",e);
        } catch(Exception e){
            throw new daabaseOperationException("failed to save message",e);

        }
    }

    @Transactional(readOnly=true)
    public List<Message>findBySender(Sender){
        try {
            return messageRepository.findBySender(sender);
        } catch (Exception e) {
            throw new DataBaseOperationException("Failed to fetch message by sender",e);
        }
    }

    @Transactional(readOnly=true)
    public List<Message>findByReceiver(User receiver){
        try{
            return messageRepository.findByReceiverOrderBySentAtAsc(receiver);
        }catch(Exception e){
            throw new DatabaseOperationException("failed to fetch message by receiver",e);
        }
    }

    @Transactional(readOnly=true)
    public List<Message>findByGroup(ChatGroup group){
        try{
            return messageRepository.findGroupOrderBySentAtAsc(group);
        }catch(Exception e){
            throw new DatabaseOperationException("Failed to fetch  direct conversation",e);
        }
    }

    @Transactional(readOnly=true)
    public Optional<Message> findById(string id){
        try{
            return messageRepository.findById(id);
        }catch(Exception e){
            throw new DatabaseOperationException("failed to fetch message by ID",e);
        }
    }

    public void deleteById(String id){
        try{
            return messageRepository.deleteById(id);
        }catch(EmptyResultDataAccessException e){
            throw new DatabaseOperationException("Failed to delete message",e);

        }
    }

    @Transactional
    public int bulkMarkDeliveredDirect(String senderId,String recieverId ,Instant ts){
        try{
            return messageRepository.bulkMarkDeliveredDirect(senderId,recieverId,ts);
        }catch(Exception e){
            throw new DatabaseOperationException("Failed to bulk mark delivered",e);
        }
    }

    @Transactional
    public int bulkmarkreadDirect(String senderId,String revceiverId,Instant ts){
        try{
            return messageRepository.bulkMarkreadDirect(senderId,receiverId,ts);
        }catch(Exception e){
            throw new DatabaseOperationException("Failed to bulk mark read",e);
        }
    }
}
