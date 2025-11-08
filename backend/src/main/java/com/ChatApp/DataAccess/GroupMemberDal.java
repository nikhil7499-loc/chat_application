package com.ChatApp.DataAccess;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ChatApp.Entities.ChatGroup;
import com.ChatApp.Entities.GroupMember;
import com.ChatApp.Entities.User;
import com.ChatApp.Exceptions.DatabaseOperationException;
import com.ChatApp.Repository.GroupMemberRepository;

@Component
@Transactional
public class GroupMemberDal {

    private final GroupMemberRepository groupMemberRepository;

    public GroupMemberDal(GroupMemberRepository groupMemberRepository) {
        this.groupMemberRepository = groupMemberRepository;
    }

    // ==========================================
    // 🔹 Save or update
    // ==========================================
    public GroupMember save(GroupMember member) {
        try {
            return groupMemberRepository.save(member);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseOperationException("Constraint violation while saving group member", e);
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to save group member", e);
        }
    }

    // ==========================================
    // 🔹 Delete a member
    // ==========================================
    public void delete(GroupMember member) {
        try {
            groupMemberRepository.delete(member);
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to delete group member", e);
        }
    }

    // ==========================================
    // 🔹 Find by group and user
    // ==========================================
    public Optional<GroupMember> findByGroupAndUser(ChatGroup group, User user) {
        try {
            return groupMemberRepository.findByGroupAndUser(group, user);
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to find group member by group and user", e);
        }
    }

    // ==========================================
    // 🔹 Find by group
    // ==========================================
    public List<GroupMember> findByGroup(ChatGroup group) {
        try {
            return groupMemberRepository.findByGroup(group);
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to find members by group", e);
        }
    }

    // ==========================================
    // 🔹 Find by user
    // ==========================================
    public List<GroupMember> findByUser(User user) {
        try {
            return groupMemberRepository.findByUser(user);
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to find memberships by user", e);
        }
    }

    // ==========================================
    // 🔹 Check if user exists in group
    // ==========================================
    public boolean existsByGroupAndUser(ChatGroup group, User user) {
        try {
            return groupMemberRepository.existsByGroupAndUser(group, user);
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to check group membership existence", e);
        }
    }
}
