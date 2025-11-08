package com.ChatApp.DataAccess;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ChatApp.Entities.ChatGroup;
import com.ChatApp.Entities.User;
import com.ChatApp.Exceptions.DatabaseOperationException;
import com.ChatApp.Exceptions.ResourceNotFoundException;
import com.ChatApp.Repository.ChatGroupRepository;

@Component
@Transactional
public class ChatGroupDal {

    private final ChatGroupRepository chatGroupRepository;

    public ChatGroupDal(ChatGroupRepository chatGroupRepository) {
        this.chatGroupRepository = chatGroupRepository;
    }

    // ==========================
    // 🔹 Save (create or update)
    // ==========================
    public ChatGroup save(ChatGroup group) {
        try {
            return chatGroupRepository.save(group);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseOperationException("Constraint violation while saving group", e);
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to save group", e);
        }
    }

    // ==========================
    // 🔹 Delete by ID
    // ==========================
    public void deleteById(String groupId) {
        try {
            chatGroupRepository.deleteById(groupId);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Group not found with ID: " + groupId);
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to delete group", e);
        }
    }

    // ==========================
    // 🔹 Find by ID
    // ==========================
    public Optional<ChatGroup> findById(String id) {
        try {
            return chatGroupRepository.findById(id);
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to find group by ID", e);
        }
    }

    // ==========================
    // 🔹 Find by Name
    // ==========================
    public Optional<ChatGroup> findByName(String name) {
        try {
            return chatGroupRepository.findByName(name);
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to find group by name", e);
        }
    }

    // ==========================
    // 🔹 Find by Creator
    // ==========================
    public List<ChatGroup> findByCreatedBy(User user) {
        try {
            return chatGroupRepository.findByCreatedBy(user);
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to find groups by creator", e);
        }
    }

    // ==========================
    // 🔹 Find all
    // ==========================
    public List<ChatGroup> findAll() {
        try {
            return chatGroupRepository.findAll();
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to fetch all groups", e);
        }
    }

    // ==========================
    // 🔹 Exists by Name
    // ==========================
    public boolean existsByName(String name) {
        try {
            return chatGroupRepository.existsByName(name);
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to check if group name exists", e);
        }
    }
}
