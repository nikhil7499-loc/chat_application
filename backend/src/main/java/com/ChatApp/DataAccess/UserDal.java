package com.ChatApp.DataAccess;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import com.ChatApp.Entities.User;
import com.ChatApp.Exceptions.DatabaseOperationException;
import com.ChatApp.Exceptions.DuplicateResourceException;
import com.ChatApp.Exceptions.ResourceNotFoundException;
import com.ChatApp.Repository.UserRepository;

@Component
@jakarta.transaction.Transactional
public class UserDal {
    private final UserRepository userRepository;

    public UserDal(UserRepository userRepository){
        this.userRepository = userRepository;
    }

 public User createUser(User user) {
    try {    
        if (this.userRepository.existsByEmail(user.getEmail())) {
            throw new DuplicateResourceException("User with email " + user.getEmail() + " already exists");
        }

        if (this.userRepository.existsByUsername(user.getUsername())) {
            throw new DuplicateResourceException("User with username " + user.getUsername() + " already exists");
        }

        return this.userRepository.save(user);
    } catch (Exception e) {
        throw new DatabaseOperationException("Failed to create user", e);
    }

    }

    public User update(User user){
        try {
            if(!userRepository.existsById(user.getId())){
                throw  new ResourceNotFoundException("user no found with id: "+user.getId());
            }

            return userRepository.save(user);
        }catch(DataIntegrityViolationException e){
            throw new DuplicateResourceException ("duplicate username or email during updates",e);
        }catch(Exception e){
            throw new DatabaseOperationException("failed to update user",e);
        }
    }
    public User findById(String id){
        return userRepository.findById(id)
            .orElseThrow(()-> new ResourceNotFoundException("user not found with id: "+id));

    }

    public Optional<User> findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public Optional<User> findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public void deleteById(String id){
        try{
            userRepository.deleteById(id);
        }catch(EmptyResultDataAccessException e){
            throw new ResourceNotFoundException("cannot delete -user not found with id: "+id);
        }catch(Exception e){
            throw new DatabaseOperationException("failed to delete user",e);
        }
    }
    public boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }

    public boolean existsByUsername(String username){
        return userRepository.existsByUsername(username);
    }
}