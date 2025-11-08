package com.ChatApp.Repository;

import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ChatApp.Entities.KnownConnection;
import com.ChatApp.Entities.User;

@Repository
public interface KnownConnectionRepository extends JpaRepository<KnownConnection, String> {

    // ✅ Get all known connections for a user, sorted by favorite first, then last message time (desc)
    List<KnownConnection> findByUserOrderByIsFavoriteDescLastMessageAtDesc(User user);

    // ✅ Check if a connection already exists between two users
    Optional<KnownConnection> findByUserAndContact(User user, User contact);

    // ✅ Delete a connection
    void deleteByUserAndContact(User user, User contact);

    // ✅ Reverse lookup (who has this user as a contact)
    List<KnownConnection> findByContact(User contact);

    // ✅ Fetch all blocked contacts for a user
    List<KnownConnection> findByUserAndIsBlockedTrue(User user);

    // ✅ Check if user has blocked a specific contact
    boolean existsByUserAndContactAndIsBlockedTrue(User user, User contact);
}
