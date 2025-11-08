package com.ChatApp.BusinessAccess;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ChatApp.DataAccess.MessageDal;
import com.ChatApp.DataAccess.MessageTypesDal;
import com.ChatApp.Entities.ChatGroup;
import com.ChatApp.Entities.Message;
import com.ChatApp.Entities.MessageTypes;
import com.ChatApp.Entities.User;

@Service
@Transactional
public class MessageBal {

    private final MessageDal messageDal;
    private final MessageTypesDal messageTypesDal;

    public MessageBal(MessageDal messageDal, MessageTypesDal messageTypesDal) {
        this.messageDal = messageDal;
        this.messageTypesDal = messageTypesDal;
    }

    /** Send a new message (group or direct) */
    public Message sendMessage(Message message) {
        message.setSentAt(Instant.now());
        return messageDal.save(message);
    }

    /** Reply to an existing message */
    public Message replyToMessage(Message originalMessage, Message replyMessage) {
        replyMessage.setReplyToMessage(originalMessage);
        replyMessage.setSentAt(Instant.now());
        return messageDal.save(replyMessage);
    }

    /** Get messages sent by a user */
    @Transactional(readOnly = true)
    public List<Message> getMessagesBySender(User sender) {
        return messageDal.findBySender(sender);
    }

    /** Get direct messages received by a user */
    @Transactional(readOnly = true)
    public List<Message> getMessagesByReceiver(User receiver) {
        return messageDal.findByReceiver(receiver);
    }

    /** Get group messages */
    @Transactional(readOnly = true)
    public List<Message> getGroupMessages(ChatGroup group) {
        return messageDal.findByGroup(group);
    }

    /** Get direct conversation between two users */
    @Transactional(readOnly = true)
    public List<Message> getDirectConversation(String userId, String otherUserId) {
        return messageDal.findDirectConversation(userId, otherUserId);
    }

    /** Find message by ID */
    @Transactional(readOnly = true)
    public Optional<Message> getMessageById(String messageId) {
        return messageDal.findById(messageId);
    }

    /** Delete message */
    public void deleteMessage(String messageId) {
        messageDal.deleteById(messageId);
    }

    /** ✅ Get message type by name (fixed) */
    @Transactional(readOnly = true)
    public MessageTypes getMessageTypeByName(String typeName) {
        return messageTypesDal.findByName(typeName);
    }

    public int markDeliveredDirect(String senderId, String receiverId) {
        return messageDal.bulkMarkDeliveredDirect(senderId, receiverId, Instant.now());
    }

    public int markReadDirect(String senderId, String receiverId) {
        return messageDal.bulkMarkReadDirect(senderId, receiverId, Instant.now());
    }
}
