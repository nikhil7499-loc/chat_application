package com.ChatApp.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ChatApp.Entities.ChatGroup;
import com.ChatApp.Entities.GroupMember;
import com.ChatApp.Entities.GroupMemberId;
import com.ChatApp.Entities.User;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, GroupMemberId> {

    // Get all members of a specific group
    List<GroupMember> findByGroup(ChatGroup group);

    // Get all group memberships for a user
    List<GroupMember> findByUser(User user);

    // Get a specific member entry by group and user
    Optional<GroupMember> findByGroupAndUser(ChatGroup group, User user);

    // Check if a user already belongs to a group
    boolean existsByGroupAndUser(ChatGroup group, User user);
}
