package com.ChatApp.DataAccess;

import java.time.Instant;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.ChatApp.Entities.Otp;
import com.ChatApp.Entities.User;
import com.ChatApp.Repository.OtpRepository;


public class OtpDal {
    private OtpRepository otpRepository;

    @Autowired
    public OtpDal(OtpRepository otpRepository){
        this.otpRepository = otpRepository;
    }

    public Otp save(Otp otp){
        return otpRepository.save(otp);
    }

    @Transactional(readOnly=true)
    public Optional<Otp> getLatestsUnusedOtp(User user){
        return otpRepository.findTopByUserAndIsUsedFalseOrderByCreatedAtDesc(user);
    }

    @Transactional(readOnly=true)
    public Optional<Otp>getByCodeAndUser(String code,User user){
        return otpRepository.findByCodeAndUser(code, user);
    }
    @Transactional(readOnly=true)
    public boolean  hasActiveOtp(User user){
        return otpRepository.existsByUserAndIsUsedFalse(user);
    }
    
    public void deleteExpiredOtps(Instant cutoffTime){
        otpRepository.deleteByExpiresAtBefore(cutoffTime);
    }
    @Transactional
    public void deleteallOtpsOfUser(User user){
        otpRepository.deleteByUser(user);
    }

}