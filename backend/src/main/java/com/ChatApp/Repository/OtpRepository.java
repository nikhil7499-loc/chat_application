package com.ChatApp.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpRepository extends JpaRepository<Otp, String> {
    Optional<Otp> findTopByUserAndIsUsedFalseOrderByCreatedAtDesc(User user);
    Optional<Otp> findByCodeAndUser(String code, User user);
    boolean existsByUserAndIsUsedFalse(User user);
    void deleteByExpiresAtBefore(Instant cutoff);
    void deleteByUser(User user);
}
