

package com.ChatApp.BusinessAccess;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.Random;

import jakarta.servlet.http.Cookie;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ChatApp.DataAccess.OtpDal;
import com.ChatApp.Entities.Otp;
import com.ChatApp.Entities.User;
import com.ChatApp.Exceptions.DuplicateResourceException;
import com.ChatApp.Exceptions.ResourceNotFoundException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
@Transactional
public class AuthBal{
    private final UserBal userBal;
    private final OtpDal otpDal;
    private final long jwtExpirationMillis;
    private final SecretKey secretKey;
    private static final int OTP_EXPIRY_MINUTES = 10;  

    public AuthBal(
        UserBal userBal,
        OtpDal otpDal,
        @Value("${jwt.secret-key}") String secretKeyValue,
        @Value("${jwt.expiration-minutes:60}") long expirationMinutes
    ){
        this.userBal = userBal;
        this.otpDal=otpDal;
        this.jwtExpirationMillis=expirationMinutes*60*1000;
        this.secretKey = Keys.hmacShaKeyFor(secretKeyValue.getBytes(StandardCharsets.UTF_8));
    } 

    public User signup(User user){
        return userBal.registerUser(user);
    }

    public String login(String emailOrUsername, String password, HttpServletResponse response){

        if(!userBal.verifyCredentials(emailOrUsername, password)){
            throw new ResourceNotFoundException("invalid username or password");
        }
        Optional<User> optUser = userBal.getuserByEmail(emailOrUsername);
        String token = generateJwt(optUser.get().getId());
        setAuthCookie(response, token);
        return token;
    }

    public void logout(HttpServletResponse response){
        Cookie cookie = new Cookie("AUTH_TOKEN", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);

        response.addCookie(cookie);
    }

    public User getAuthenticatedUser(HttpServletRequest request){
        String token = extractTokenFromCookie(request);

        if(token==null){
            return null;
        }

        try {
            Claims claims = Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .getBody();

            String UserId = claims.getSubject();

            if(UserId == null || UserId.isBlank()){
                return null;
            }

            return userBal.getUserById(UserId);

        } catch (Exception e) {
            return null;
        }
    }

    public String forgotPassword(String email){
        Optional<User> userOpt = userBal.getuserByEmail(email);
        if(userOpt.isEmpty()){
            throw new ResourceNotFoundException("User not found");
        }

        User user = userOpt.get();

        if(otpDal.hasActiveOtp(user)){
            otpDal.deleteallOtpsOfUser(user);
        }

        String otpCode=generateOtpCode();

        Otp otp = new Otp();
        otp.setUser(user);
        otp.setCode(otpCode);
        otp.setExpiresAt(Instant.now().plusSeconds(OTP_EXPIRY_MINUTES*60));
        otp.setIsUsed(false);
        otpDal.save(otp);

        return otpCode;
    }

    public void resetPassword(String email, String otpCode, String newPassword){
        Optional<User> userOpt = userBal.getuserByEmail(email);

        if(userOpt.isEmpty()){
            throw new ResourceNotFoundException("User not found");
        }

        User user = userOpt.get();

        Optional<Otp> otpOpt = otpDal.getByCodeAndUser(otpCode, user);

        if(otpOpt.isEmpty()){
            throw new ResourceNotFoundException("otp not found please try again");
        }

        Otp otp = otpOpt.get();

        if(otp.GetIsUsed()){
            throw new DuplicateResourceException("Otp is invalid");
        }

        if(otp.getExpiresAt().isBefore(Instant.now())){
            throw new DuplicateResourceException("Otp has expired");
        }

        userBal.updatePassword(user, newPassword);

        otp.setIsUsed(true);
        otpDal.save(otp);
    }

    private String generateJwt(String id){
        Instant nowUtc = ZonedDateTime.now(ZoneOffset.UTC).toInstant();
        Instant expUtc = nowUtc.plusMillis(jwtExpirationMillis);

        return Jwts.builder()
        .setSubject(id)
        .setIssuedAt(Date.from(nowUtc))
        .setExpiration(Date.from(expUtc))
        .signWith(this.secretKey, SignatureAlgorithm.HS256)
        .compact();
    }

    private void setAuthCookie(HttpServletResponse response, String token){
        Cookie cookie = new Cookie("AUTH_TOKEN", token);
        cookie.setHttpOnly(true);
        cookie.setMaxAge((int)(jwtExpirationMillis/1000));
        cookie.setPath("/");
        cookie.setSecure(true);
        cookie.setAttribute("SameSite", "None");

        response.addCookie(cookie);
    }

    private String extractTokenFromCookie(HttpServletRequest request){
        if(request.getCookies()==null){
            return null;
        }

        for(Cookie cookie: request.getCookies()){
            if("AUTH_TOKEN".equals(cookie.getName())){
                return cookie.getValue();
            }
        }

        return null;
    }

    private String generateOtpCode(){
        Random random = new Random();
        int otp=100000+random.nextInt(900000);
        return String.valueOf(otp);
    }
}