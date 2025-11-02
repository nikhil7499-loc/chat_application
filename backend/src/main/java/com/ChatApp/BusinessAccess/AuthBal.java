package com.ChatApp.BusinessAccess;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ChatApp.Entities.Otp;
import com.ChatApp.Entities.User;
import com.ChatApp.Exceptions.UnauthorizedException;
import com.ChatApp.DataAccess.OtpDal;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.crypto.SecretKey;

@Service
@Transactional
public class AuthBal {

    private final UserBal userBal;
    private final OtpDal otpDal;
    private final SecretKey secretKey;
    private final long jwtExpirationMillis;
    private static final int OTP_EXPIRY_MINUTES = 10;

    public AuthBal(
            UserBal userBal,
            OtpDal otpDal,
            @Value("${jwt.secret-key}") String secretKeyValue,
            @Value("${jwt.expiration-minutes:60}") long expirationMinutes) {

        this.userBal = userBal;
        this.otpDal = otpDal;
        this.secretKey = Keys.hmacShaKeyFor(secretKeyValue.getBytes(StandardCharsets.UTF_8));
        this.jwtExpirationMillis = expirationMinutes * 60 * 1000;
    }

    // --- Existing Auth Functions ---

    public User signup(User user) {
        return userBal.registerUser(user);
    }

    @Transactional(readOnly = true)
    public String login(String emailOrUsername, String password, HttpServletResponse response) {
        boolean isValid = userBal.verifyCredentials(emailOrUsername, password);
        if (!isValid) {
            throw new UnauthorizedException("Invalid email/username or password");
        }

        Optional<User> userOpt = userBal.getUserByEmail(emailOrUsername);
        if (userOpt.isEmpty()) {
            userOpt = userBal.getUserByUsername(emailOrUsername);
        }

        if (userOpt.isEmpty()) {
            throw new UnauthorizedException("User not found");
        }

        User user = userOpt.get();
        String token = generateJwt(user.getId());
        setAuthCookie(response, token);
        return token;
    }

    public void logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("AUTH_TOKEN", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    @Transactional(readOnly = true)
    public User getAuthenticatedUser(HttpServletRequest request) {
        String token = extractTokenFromCookie(request);
        if (token == null) {
            return null;
        }

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String userId = claims.getSubject();
            if (userId == null || userId.isBlank()) {
                return null;
            }

            return userBal.getUserById(userId);
        } catch (Exception e) {
            return null;
        }
    }

    // --- New OTP-based Password Reset Functions ---

    public String forgotPassword(String email) {
        Optional<User> userOpt = userBal.getUserByEmail(email);
        if (userOpt.isEmpty()) {
            throw new UnauthorizedException("No account found with this email.");
        }

        User user = userOpt.get();

        if (otpDal.hasActiveOtp(user)) {
            otpDal.deleteAllOtpsOfUser(user);
        }

        String otpCode = generateOtpCode();

        Otp otp = new Otp();
        otp.setUser(user);
        otp.setCode(otpCode);
        otp.setExpiresAt(Instant.now().plusSeconds(OTP_EXPIRY_MINUTES * 60)); 
        otp.setUsed(false);                                                   
        otpDal.save(otp);

        return otpCode;
    }

    public void resetPassword(String email, String otpCode, String newPassword) {
        Optional<User> userOpt = userBal.getUserByEmail(email);
        if (userOpt.isEmpty()) {
            throw new UnauthorizedException("No account found with this email.");
        }

        User user = userOpt.get();
        Optional<Otp> otpOpt = otpDal.getOtpByCodeAndUser(otpCode, user);

        if (otpOpt.isEmpty()) {
            throw new UnauthorizedException("Invalid OTP.");
        }

        Otp otp = otpOpt.get();

        if (otp.isUsed()) {                         
            throw new UnauthorizedException("This OTP has already been used.");
        }

        if (otp.getExpiresAt().isBefore(Instant.now())) { 
            throw new UnauthorizedException("This OTP has expired.");
        }



        userBal.updatePassword(user, newPassword); // ✅ must exist
        otp.setUsed(true);                          
        otpDal.save(otp);
    }

    // --- Helpers ---

    private String generateJwt(String userId) {
        Instant nowUtc = ZonedDateTime.now(ZoneOffset.UTC).toInstant();
        Instant expUtc = nowUtc.plusMillis(jwtExpirationMillis);

        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(Date.from(nowUtc))
                .setExpiration(Date.from(expUtc))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    private void setAuthCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("AUTH_TOKEN", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge((int) (jwtExpirationMillis / 1000));
        cookie.setAttribute("SameSite", "None");
        response.addCookie(cookie);
    }

    private String extractTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }
        for (Cookie cookie : request.getCookies()) {
            if ("AUTH_TOKEN".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    private String generateOtpCode() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
}
