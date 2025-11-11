package com.ChatApp.BusinessAccess;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ChatApp.DataAccess.OtpDal;

@Service
@Transactional
public class AuthBal{
    public AuthBal(
        UserBal userBal,
        OtpDal otpDal,
        @Value("${jwt.secret-key}")
    ){

    }
}