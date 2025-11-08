package com.ChatApp.BusinessAccess;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ChatApp.DataAccess.GroupMemberDal;
import com.ChatApp.Entities.ChatGroup;
import com.ChatApp.Entities.GroupMember;
import com.ChatApp.Entities.GroupMember.Role;
import com.ChatApp.Entities.GroupMemberId;
import com.ChatApp.Entities.User;
import com.ChatApp.Exceptions.ResourceNotFoundException;
import com.ChatApp.Exceptions.DuplicateResourceException;

@Service
@Transactional
public class GroupMemberBal {

    private final GroupMemberDal groupMemberDal;

    public GroupMemberBal(GroupMemberDal groupMemberDal) {
        this.groupMemberDal = groupMemberDal;
    }

    // ==========================================
    // ✅ 1. Add a user to a group
    // ==========================================
    public GroupMember addMember(ChatGroup group, User user, Role role) {
        if (groupMemberDal.existsByGroupAndUser(group, user)) {
            throw new DuplicateResourceException("User already in group");
        }

        GroupMember member = new GroupMember();
        member.setId(new GroupMemberId(group.getId(), user.getId()));
        member.setGroup(group);
        member.setUser(user);
        member.setRole(role != null ? role : Role.member);
        member.setJoined_at(Instant.now());
        member.setIs_active(true);

        return groupMemberDal.save(member);
    }

    // ==========================================
    // ✅ 2. Remove a user from a group (hard delete)
    // ==========================================
    public void removeMember(ChatGroup group, User user) {
        Optional<GroupMember> memberOpt = groupMemberDal.findByGroupAndUser(group, user);
        if (memberOpt.isEmpty()) {
            throw new ResourceNotFoundException("User not found in group");
        }
        groupMemberDal.delete(memberOpt.get());
    }

    // ==========================================
    // ✅ 3. Get all members of a group
    // ==========================================
    @Transactional(readOnly = true)
    public List<GroupMember> getMembers(ChatGroup group) {
        return groupMemberDal.findByGroup(group);
    }

    // ==========================================
    // ✅ 4. Get all groups a user belongs to
    // ==========================================
    @Transactional(readOnly = true)
    public List<GroupMember> getUserMemberships(User user) {
        return groupMemberDal.findByUser(user);
    }

    // ==========================================
    // ✅ 5. Change a member's role
    // ==========================================
    public GroupMember updateMemberRole(ChatGroup group, User user, Role newRole) {
        Optional<GroupMember> memberOpt = groupMemberDal.findByGroupAndUser(group, user);
        if (memberOpt.isEmpty()) {
            throw new ResourceNotFoundException("User not in group");
        }

        GroupMember member = memberOpt.get();
        member.setRole(newRole);
        return groupMemberDal.save(member);
    }

    // ==========================================
    // ✅ 6. Deactivate a member (soft remove)
    // ==========================================
    public void deactivateMember(ChatGroup group, User user) {
        Optional<GroupMember> memberOpt = groupMemberDal.findByGroupAndUser(group, user);
        if (memberOpt.isEmpty()) {
            throw new ResourceNotFoundException("User not found in group");
        }

        GroupMember member = memberOpt.get();
        member.setIs_active(false);
        groupMemberDal.save(member);
    }

    // ==========================================
    // ✅ 7. Reactivate a member (if previously deactivated)
    // ==========================================
    public void reactivateMember(ChatGroup group, User user) {
        Optional<GroupMember> memberOpt = groupMemberDal.findByGroupAndUser(group, user);
        if (memberOpt.isEmpty()) {
            throw new ResourceNotFoundException("User not found in group");
        }

        GroupMember member = memberOpt.get();
        member.setIs_active(true);
        groupMemberDal.save(member);
    }
}
