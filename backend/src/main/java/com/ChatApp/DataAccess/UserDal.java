package com.ChatApp.DataAccess;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ChatApp.Entities.User;
import com.ChatApp.Repository.UserRepository;
import com.ChatApp.Exceptions.DuplicateResourceException;
import com.ChatApp.Exceptions.ResourceNotFoundException;
import com.ChatApp.Exceptions.DatabaseOperationException;

@Component
@Transactional
public class UserDal {

    private final UserRepository userRepository;

    public UserDal(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User create(User user) {
        try {
            if (userRepository.existsByEmail(user.getEmail())) {
                throw new DuplicateResourceException("Email already in use: " + user.getEmail());
            }

            if (userRepository.existsByUsername(user.getUsername())) {
                throw new DuplicateResourceException("Username already in use: " + user.getUsername());
            }

            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateResourceException("Duplicate entry detected", e);
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to create user", e);
        }
    }

    public User update(User user) {
        try {
            if (!userRepository.existsById(user.getId())) {
                throw new ResourceNotFoundException("User not found with ID: " + user.getId());
            }

            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateResourceException("Duplicate username or email during update", e);
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to update user", e);
        }
    }

    public User findById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void deleteById(String id) {
        try {
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Cannot delete — user not found with ID: " + id);
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to delete user", e);
        }
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
}
