package com.ChatApp.BusinessAccess;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ChatApp.DataAccess.MessageDal;
import com.ChatApp.DataAccess.MessageTypesDal;
import com.ChatApp.Entities.Message;
import com.ChatApp.Entities.MessageTypes;
import com.ChatApp.Entities.User;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class MessageBal {
    private final MessageDal messageDal;
    private final MessageTypesDal messageTypesDal;

    public MessageBal(MessageDal messageDal,MessageTypesDal messageTypesDal){
        this.messageDal=messageDal;
        this.messageTypesDal=messageTypesDal;
    }

    //send a new message
    public Message sendMessage(Message message){
        message.setSentAt(Instant.now());
        return messageDal.save(message);
    }

    //reply to existing message

    public Message replyToMessage(Message originalMessage,Message replyMessage){
        replyMessage.setReplyToMessage(originalMessage);
        replyMessage.setSentAt(Instant.now());
        return messageDal.save(replyMessage);
    }

    //get message sent by a user

    @Transactional(readOnly=true)
    public List<Message>getMessageByreceiver(User sender){
        return messageDal.findBySender(sender);
    }

    //get direct message received by a user
    @Transactional(readOnly=true)
    public List<Message> getMessageByReceiver(User receiver){
        return messageDal.findByReceiver(receiver);
    }

    //get direct conversation between two users
    @Transactional(readOnly=true)
    public List<Message>getDirectConversation(String userId,String otherUserId){
        return messageDal.findById(messageId);
    }

    //find message by ID
    @Transactional(readOnly=true)
    public Optional<Message>getMessageById(String messageId){
        return messageDal.findById(messageId);
    }
    //delete message

    public void deleteMessage(String messageId){
        messageDal.deleteById(messageId);
    }

    //get message type by name
    @Transactional(readOnly=true)
    public MessageTypes getMessageTypeByName(String typeName){
        return messageTypesDal.findByName(typeName);
    }

    public int markDeliveredDirect(String senderId,String receiverId){
        return messageDal.bulkMarkReadDirect(senderId, receiverId,Instant.now());
    }

    public int markreadDirect(String senderId,String receiverId){
        return messageDal.bulkMarkReadDirect(senderId, receiverId,Instant.now());
    }




}
