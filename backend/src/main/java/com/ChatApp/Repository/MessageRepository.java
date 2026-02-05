package com.ChatApp.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ChatApp.Entities.ChatGroups;
import com.ChatApp.Entities.Message;

@Repository
public interface  MessageRepository extends JpaRepository<Message,String>{
    List<Message>findBysende (String name);
    List<Message>findByRecieverOrderBySentAtAsc(String name);
    List<Message>findByGroupOrderBySentAsc(ChatGroups groups);
    
}
