package com.ChatApp.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ChatApp.Entities.ChatGroups;
import com.ChatApp.Entities.User;



@Repository
public interface  ChatGrouprepository extends JpaRepository<ChatGroups, String> {
    Optional<ChatGroups>findByName(String name);
    List<ChatGroups>findByCreatedBy(User createdBy);
    boolean existsByName(String name);

}
