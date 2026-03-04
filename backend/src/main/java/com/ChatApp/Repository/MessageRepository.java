package com.ChatApp.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ChatApp.Entities.Message;
import com.ChatApp.Entities.User;

@Repository
public interface MessageRepository extends JpaRepository<Message, String> {
    List<Message> findBySender(User user);

    List<Message> findByRecieverOrderBySentAtAsc(User user);

    // custome query for 1 to 1 conversation
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
            @Param("receiverId") String receiverId);

                @Modifying(clearAutomatically=true,flushAutomatically=true)
                @Query("""
                        update Message m 
                        set m.deliveredAt=:ts
                        where m.sender.id=:senderId
                        and m.receiver.id=:receiverId
                        and m.deliveredAt is null
                        """)
                        int bultMarkDeliveredDirect(@Param("senderId") String senderId,
                                                    @Param("receiverId") String receiverId,
                                                    @Param("ts") Instant ts);

                          // mark as read all messages sent by senderId to receiverId, already delivered but not yet read
                          @Modifying(clearAutomatically=true,flushAutomatically=true )
                          @Query("""
                                  update Message m
                                  set m.readAt=:ts
                                  where m.sender.id=:senderId
                                  and m.receiver.id=:receiverId
                                  and m.readAt is null
                                  """)
                                  int bulkMarkReadDirect(@Param("senderId") String senderId,
                                                        @Param("receivedId") String receiverId,
                                                        @Param("ts") Instant ts   );
            }
