package com.ChatApp.DataAccess;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ChatApp.Entities.KnownConnection;
import com.ChatApp.Entities.User;
import com.ChatApp.Exceptions.DatabaseOperationException;
import com.ChatApp.Repository.KnownConnectionRepository;

@Component
@Transactional
public class KnownConnectionDal {

    private final KnownConnectionRepository knownConnectionRepository;

    public KnownConnectionDal(KnownConnectionRepository knownConnectionRepository) {
        this.knownConnectionRepository = knownConnectionRepository;
    }

    // ==========================================
    // 🔹 Save or update connection
    // ==========================================
    public KnownConnection save(KnownConnection connection) {
        try {
            return knownConnectionRepository.save(connection);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseOperationException("Constraint violation while saving known connection", e);
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to save known connection", e);
        }
    }

    // ==========================================
    // 🔹 Find by user and contact
    // ==========================================
    public Optional<KnownConnection> findByUserAndContact(User user, User contact) {
        try {
            return knownConnectionRepository.findByUserAndContact(user, contact);
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to fetch known connection", e);
        }
    }

    // ==========================================
    // 🔹 Find all known connections (for sidebar)
    // ==========================================
    public List<KnownConnection> findByUserOrderByFavoriteAndLastMessage(User user) {
        try {
            return knownConnectionRepository.findByUserOrderByIsFavoriteDescLastMessageAtDesc(user);
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to fetch known connections for user", e);
        }
    }

    // ==========================================
    // 🔹 Delete connection
    // ==========================================
    public void deleteByUserAndContact(User user, User contact) {
        try {
            knownConnectionRepository.deleteByUserAndContact(user, contact);
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to delete known connection", e);
        }
    }

    // ==========================================
    // 🔹 Check if blocked
    // ==========================================
    public boolean existsBlocked(User user, User contact) {
        try {
            return knownConnectionRepository.existsByUserAndContactAndIsBlockedTrue(user, contact);
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to check block status", e);
        }
    }

    // ==========================================
    // 🔹 Find all blocked connections
    // ==========================================
    public List<KnownConnection> findBlockedConnections(User user) {
        try {
            return knownConnectionRepository.findByUserAndIsBlockedTrue(user);
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to fetch blocked connections", e);
        }
    }
}
