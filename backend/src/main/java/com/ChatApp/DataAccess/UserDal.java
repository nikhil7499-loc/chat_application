package com.ChatApp.DataAccess;

import com.ChatApp.Entities.User;
import com.ChatApp.Exceptions.DatabaseOperationException;
import com.ChatApp.Exceptions.DuplicateResourceException;
import com.ChatApp.Repository.UserRepository;

public class UserDal {
    private final UserRepository userRepository;

    public UserDal(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User createUser(User user){
        
        try {    
            if (this.userRepository.existsByEmail(user.getEmail())){
                throw new DuplicateResourceException("User with email "+user.getEmail()+" already exists");
            }
    
            if(this.userRepository.existsByUsername(user.getUsername())){
                throw new DuplicateResourceException("User with username "+user.getUsername()+" already exists");
            }
    
            return this.userRepository.save(user);
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to create user", e);
        }

    }

    
}
