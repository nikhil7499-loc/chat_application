package com.ChatApp.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ChatApp.Entities.Message;

@Repository
public interface  MessageRepository extends JpaRepository<Message,String>{
    Optional<Message> (String name);
    
}
