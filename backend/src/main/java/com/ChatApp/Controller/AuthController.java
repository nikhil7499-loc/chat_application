package com.ChatApp.Controllers;

import java.net.ResponseCache;
import java.time.LocalDate;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import com.ChatApp.Models.Requests.UserRequests;
import com.ChatApp.BusinessAccess.AuthBal;
import com.ChatApp.Entities.User;

import jakarta.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthBal authBal;

    @Autowired
    AuthController(AuthBal authBal){
        this.authBal=authBal;
    }


    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserRequests.SignupRequest req){
        try{
            User user = new User();
            user.setUsername(req.getUsername());
            user.setEmail(req.getEmail());
            user.setPassword(req.getPassword());


                if(req.getGender()!=null){
                    try{
                        user.setGender(user.Gender.valueof(req.getGender().toLowecase()));

                    }
                    catch(IllegalArgumentException e){
                        return ResponseEntity.badRequest().body("invalid gendser value. Allowed:male,femal,other");
                    }
                }
                if(req.getDateOfBirth()!=null && !req.getDateOfBirth().isEmpty()){
                    try{
                        user.setDate_of_birth(LocalDate.parse(req.getDateOfBirth()));

                    }
                    catch(Exception e){
                        return ResponseEntity.badRequest().body("invalid date formate . use ISO 8601 (e.g. 2000-05-25)");
                    }
                }

                User createdUser=authbal.signup(user);
                return ResponseEntity.ok(new UserResponse.UserResponse(createdUser));

        }catch(Exception e){
            return ResponseEntity.badRequest().body("Error: "+e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequests.LoginRequest req,HttpServletResponse Response){
        try{
            String token=authBal.login(req.getEmailOrUsername(),req.getPassword(),response);
            return ResponseEntity.ok(Map.of(
                "message","login succesful",
                "token", token
            ));
        }
        catch(unauthorizedException e){
            return ResponseEntity.status(401).body(e.getMessage());
            
        }catch(Exception e){
            return ResponseEntity.internalserverError().body("Error:"+e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response){

        authBal.logout(response);
        return ResponseEntity.ok("logged out successfully.");
    }    

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotpassword(@RequestBody UserRequests.ForgotPasswordRequest request){
        String otp=authBal.forgotPassword(request.getEmail());
        return ResponseEntity.ok("OTP sent to your email:");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody UserRequests.ResetPasswordRequest request){
        authBal.resetPassword(request.getEmail(),request.getOtp(),request.getNewPassword());
        return ResponseEntity.ok("password reset successfully.");
        
    }

    @GetMapping("/me")
    @IsAuthenticatedUser    
    public ResponseEntity<?> getAuthenticatedUser(@Current User user){
        return ResponseEntity.ok(new UserResponses.userResponse(user));
    }
}