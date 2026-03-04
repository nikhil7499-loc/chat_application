package com.ChatApp.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ChatApp.Entities.Message;
import com.ChatApp.Entities.User;

@Repository
public interface MessageRepository extends JpaRepository<Message,String>{
    List<Message>findBySender (User user);
    List<Message>findByRecieverOrderBySentAtAsc(User user);    
}
