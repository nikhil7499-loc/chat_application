package com.ChatApp.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ChatApp.Entities.MessageTypes;

@Repository
public interface MessageTypesRepository extends JpaRepository<MessageTypes, Short> {

    // Get a message type by its name (e.g., "text", "image")
    Optional<MessageTypes> findByName(String name);

    // JpaRepository already provides:
    // - findAll()  → get all message types
    // - findById(id) → get one by ID

    // No need for save(), delete(), etc. — just don’t call them in code
}
