package com.ChatApp.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ChatApp.Entities.ChatGroups;
import com.ChatApp.Entities.GroupMember;
import com.ChatApp.Entities.User;


@Repository
public interface  GroupMemberRepository extends JpaRepository<GroupMember, String>{
    List<GroupMember>findByGroup(ChatGroups group);

    List<GroupMember>findByUser(User user);
    
    Optional<GroupMember>findByGroupAndUser(ChatGroups group,User user);
    boolean exitstByGroupAndUser(ChatGroups group ,User user);
}
