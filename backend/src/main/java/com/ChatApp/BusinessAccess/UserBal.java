package com.ChatApp.BusinessAccess;

import java.util.List;
import java.util.Optional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.ChatApp.DataAccess.UserDal;
import com.ChatApp.Entities.User;
import com.ChatApp.Exceptions.DuplicateResourceException;
import com.sun.source.doctree.ReturnTree;

import jakarta.transaction.Transactional;

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
            throw new DuplicateResourceException("email already in use:" + user.getEmail());
        }
        if (userDal.existsByUsername(user.getUsername())) {
            throw new DuplicateResourceException("username already in use: " + user.getUsername());
        }
        String hashedpassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedpassword);

        return userDal.createUser(user);
    }

    public User updateUser(User user) {
        User existing = userDal.findById(user.getId());
        existing.setUsername(user.getUsername());
        existing.setProfilePicture(user.getProfilePicture());
        existing.setDateOfBirth(user.getDateOfBirth());
        existing.setGender(user.getGender());
        return userDal.update(existing);
    }

    public void updatePassword(User user, String newPassword) {
        String hashedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(hashedPassword);
        userDal.update(user);
    }

    public boolean verifyCredentials(String emailorUsername, String plainpassword) {
        Optional<User> userOptional = userDal.findByEmail(emailorUsername);
        if (userOptional.isEmpty()) {
            userOptional = userDal.findByUsername(emailorUsername);
        }
        if (userOptional.isEmpty()) {
            return false;
        }
        User user = userOptional.get();
        return passwordEncoder.matches(plainpassword, user.getPassword());
    }

    public User getUserById(String id) {
        return userDal.findById(id);
    }

    public Optional<User> getuserByEmail(String email) {
        return userDal.findByEmail(email);
    }

    public Optional<User> getuserByUsername(String username) {
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
