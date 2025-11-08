package com.ChatApp.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ChatApp.Entities.ChatGroup;
import com.ChatApp.Entities.User;

@Repository
public interface ChatGroupRepository extends JpaRepository<ChatGroup, String> {

    // Find a chat group by its unique name
    Optional<ChatGroup> findByName(String name);

    // Get all groups created by a specific user
    List<ChatGroup> findByCreatedBy(User createdBy);

    // Check if a group name already exists
    boolean existsByName(String name);
}
