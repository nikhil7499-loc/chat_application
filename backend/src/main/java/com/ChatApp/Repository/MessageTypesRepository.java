package com.ChatApp.Repository;

import java.time.Instant;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ChatApp.Entities.MessageTypes;

@Repository
public interface MessageTypesRepository extends JpaRepository<MessageTypes, Short> {
    
}
