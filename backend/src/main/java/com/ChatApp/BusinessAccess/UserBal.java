package com.ChatApp.BusinessAccess;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ChatApp.DataAccess.UserDal;
import com.ChatApp.Entities.User;
import com.ChatApp.Exceptions.DuplicateResourceException;

@Service
@Transactional
public class UserBal {

    private final UserDal userDal;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserBal(UserDal userDal) {
        this.userDal = userDal;
        this.passwordEncoder = new BCryptPasswordEncoder(10);
    }

    public User registerUser(User user) {
        if (userDal.existsByEmail(user.getEmail())) {
            throw new DuplicateResourceException("Email already in use: " + user.getEmail());
        }
        if (userDal.existsByUsername(user.getUsername())) {
            throw new DuplicateResourceException("Username already in use: " + user.getUsername());
        }

        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        return userDal.create(user);
    }

    public User updateUser(User user) {
        User existing = userDal.findById(user.getId());
        existing.setName(user.getName());
        existing.setProfile_picture(user.getProfile_picture());
        existing.setDate_of_birth(user.getDate_of_birth());
        existing.setGender(user.getGender());
        return userDal.update(existing);
    }

    public void updatePassword(User user, String newPassword) {
        String hashedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(hashedPassword);
        userDal.update(user);
    }

    public boolean verifyCredentials(String emailOrUsername, String plainPassword) {
        Optional<User> userOpt = userDal.findByEmail(emailOrUsername);
        if (userOpt.isEmpty()) {
            userOpt = userDal.findByUsername(emailOrUsername);
        }

        if (userOpt.isEmpty()) {
            return false;
        }

        User user = userOpt.get();
        return passwordEncoder.matches(plainPassword, user.getPassword());
    }

    public User getUserById(String id) {
        return userDal.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userDal.findByEmail(email);
    }

    public Optional<User> getUserByUsername(String username) {
        return userDal.findByUsername(username);
    }

    public List<User> getAllUsers() {
        return userDal.findAll();
    }

    public void deleteUser(String id) {
        userDal.findById(id);
        userDal.deleteById(id);
    }
}
