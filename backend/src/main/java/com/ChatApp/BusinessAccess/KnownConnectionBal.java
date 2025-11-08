package com.ChatApp.BusinessAccess;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ChatApp.DataAccess.KnownConnectionDal;
import com.ChatApp.Entities.KnownConnection;
import com.ChatApp.Entities.User;
import com.ChatApp.Exceptions.ResourceNotFoundException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class KnownConnectionBal {

    private final KnownConnectionDal knownConnectionDal;

    public KnownConnectionBal(KnownConnectionDal knownConnectionDal) {
        this.knownConnectionDal = knownConnectionDal;
    }

    // ============================================
    // ✅ 1. Add or update connection on message
    // ============================================
    public void updateConnectionOnMessage(User sender, User receiver) {
        if (sender == null || receiver == null || sender.getId().equals(receiver.getId())) return;

        Instant now = Instant.now();

        // --- Sender's connection ---
        KnownConnection senderConn = knownConnectionDal
                .findByUserAndContact(sender, receiver)
                .orElseGet(() -> new KnownConnection(sender, receiver));
        senderConn.setLastMessageAt(now);
        knownConnectionDal.save(senderConn);

        // --- Receiver's connection ---
        KnownConnection receiverConn = knownConnectionDal
                .findByUserAndContact(receiver, sender)
                .orElseGet(() -> new KnownConnection(receiver, sender));
        receiverConn.setLastMessageAt(now);
        knownConnectionDal.save(receiverConn);
    }

    // ============================================
    // ✅ 2. Get known connections (for sidebar)
    // ============================================
    @Transactional(readOnly = true)
    public List<KnownConnection> getKnownConnections(User user) {
        return knownConnectionDal.findByUserOrderByFavoriteAndLastMessage(user);
    }

    // ============================================
    // ✅ 3. Mark / unmark favorite
    // ============================================
    public boolean toggleFavorite(User user, User contact, boolean favorite) {
        Optional<KnownConnection> connectionOpt = knownConnectionDal.findByUserAndContact(user, contact);
        if (connectionOpt.isEmpty()) return false;

        KnownConnection connection = connectionOpt.get();
        connection.setIsFavorite(favorite);
        knownConnectionDal.save(connection);
        return true;
    }

    // ============================================
    // ✅ 4. Remove connection
    // ============================================
    public void removeConnection(User user, User contact) {
        knownConnectionDal.deleteByUserAndContact(user, contact);
    }

    // ============================================
    // 🚫 5. Block user
    // ============================================
    public boolean blockUser(User user, User contact) {
        if (user == null || contact == null) return false;

        KnownConnection connection = knownConnectionDal
                .findByUserAndContact(user, contact)
                .orElseGet(() -> new KnownConnection(user, contact));

        connection.setIsBlocked(true);
        connection.setBlockedByUserId(user.getId());
        knownConnectionDal.save(connection);
        return true;
    }

    // ============================================
    // ✅ 6. Unblock user
    // ============================================
    public boolean unblockUser(User user, User contact) {
        Optional<KnownConnection> connectionOpt = knownConnectionDal.findByUserAndContact(user, contact);
        if (connectionOpt.isEmpty()) return false;

        KnownConnection connection = connectionOpt.get();
        connection.setIsBlocked(false);
        connection.setBlockedByUserId(null);
        knownConnectionDal.save(connection);
        return true;
    }

    // ============================================
    // 🔍 7. Check if user is blocked
    // ============================================
    @Transactional(readOnly = true)
    public boolean isBlocked(User user, User contact) {
        return knownConnectionDal.existsBlocked(user, contact);
    }

    // ============================================
    // 📋 8. Get all blocked contacts
    // ============================================
    @Transactional(readOnly = true)
    public List<KnownConnection> getBlockedConnections(User user) {
        return knownConnectionDal.findBlockedConnections(user);
    }
}
