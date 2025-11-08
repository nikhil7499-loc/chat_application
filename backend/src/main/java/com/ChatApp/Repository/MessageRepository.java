package com.ChatApp.Repository;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ChatApp.Entities.ChatGroup;
import com.ChatApp.Entities.Message;
import com.ChatApp.Entities.User;

@Repository
public interface MessageRepository extends JpaRepository<Message, String> {

    // ✅ All messages sent by a user
    List<Message> findBySender(User sender);

    // ✅ All messages received by a user, ordered by send time
    List<Message> findByReceiverOrderBySentAtAsc(User receiver);

    // ✅ All messages in a specific group
    List<Message> findByGroup(ChatGroup group);

    // ✅ Group messages ordered by send time
    List<Message> findByGroupOrderBySentAtAsc(ChatGroup group);

    // ✅ Custom query for 1-to-1 conversation between two users
    @Query("""
        SELECT m 
        FROM Message m 
        WHERE 
            (m.sender.id = :senderId AND m.receiver.id = :receiverId)
            OR 
            (m.sender.id = :receiverId AND m.receiver.id = :senderId)
        ORDER BY m.sentAt ASC
        """)
    List<Message> findDirectConversation(
        @Param("senderId") String senderId, 
        @Param("receiverId") String receiverId
    );


    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
           UPDATE Message m 
              SET m.deliveredAt = :ts
            WHERE m.sender.id = :senderId 
              AND m.receiver.id = :receiverId
              AND m.deliveredAt IS NULL
           """)
    int bulkMarkDeliveredDirect(@Param("senderId") String senderId,
                                @Param("receiverId") String receiverId,
                                @Param("ts") Instant ts);

    // Mark as READ all messages sent by :senderId to :receiverId, already delivered but not yet read
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
           UPDATE Message m 
              SET m.readAt = :ts
            WHERE m.sender.id = :senderId 
              AND m.receiver.id = :receiverId
              AND m.readAt IS NULL
           """)
    int bulkMarkReadDirect(@Param("senderId") String senderId,
                           @Param("receiverId") String receiverId,
                           @Param("ts") Instant ts);
}
