package com.ChatApp.BusinessAccess;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ChatApp.DataAccess.ChatGroupDal;
import com.ChatApp.Entities.ChatGroup;
import com.ChatApp.Entities.User;
import com.ChatApp.Exceptions.DuplicateResourceException;
import com.ChatApp.Exceptions.ResourceNotFoundException;

@Service
@Transactional
public class ChatGroupBal {

    private final ChatGroupDal chatGroupDal;

    public ChatGroupBal(ChatGroupDal chatGroupDal) {
        this.chatGroupDal = chatGroupDal;
    }

    // ==========================
    // ✅ 1. Create a new group
    // ==========================
    public ChatGroup createGroup(String name, String description, User createdBy) {
        if (chatGroupDal.existsByName(name)) {
            throw new DuplicateResourceException("Group name already exists: " + name);
        }

        ChatGroup group = new ChatGroup();
        group.setId(UUID.randomUUID().toString());
        group.setName(name);
        group.setDescription(description);
        group.setCreatedBy(createdBy);
        group.setCreated_at(Instant.now());
        group.setUpdated_at(Instant.now());

        return chatGroupDal.save(group);
    }

    // ==========================
    // ✅ 2. Update group details
    // ==========================
    public ChatGroup updateGroup(ChatGroup group) {
        Optional<ChatGroup> existingOpt = chatGroupDal.findById(group.getId());
        if (existingOpt.isEmpty()) {
            throw new ResourceNotFoundException("Group not found with ID: " + group.getId());
        }

        group.setUpdated_at(Instant.now());
        return chatGroupDal.save(group);
    }

    // ==========================
    // ✅ 3. Delete a group
    // ==========================
    public void deleteGroup(String groupId) {
        chatGroupDal.deleteById(groupId);
    }

    // ==========================
    // ✅ 4. Get groups created by user
    // ==========================
    @Transactional(readOnly = true)
    public List<ChatGroup> getGroupsCreatedBy(User user) {
        return chatGroupDal.findByCreatedBy(user);
    }

    // ==========================
    // ✅ 5. Find group by name
    // ==========================
    @Transactional(readOnly = true)
    public Optional<ChatGroup> getGroupByName(String name) {
        return chatGroupDal.findByName(name);
    }

    // ==========================
    // ✅ 6. Find group by ID
    // ==========================
    @Transactional(readOnly = true)
    public Optional<ChatGroup> getGroupById(String id) {
        return chatGroupDal.findById(id);
    }

    // ==========================
    // ✅ 7. Get all groups
    // ==========================
    @Transactional(readOnly = true)
    public List<ChatGroup> getAllGroups() {
        return chatGroupDal.findAll();
    }
}
