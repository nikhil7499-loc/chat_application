package com.ChatApp.Controllers;

import java.time.LocalDate;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResourceAccessException;

import com.ChatApp.BusinessAccess.AuthBal;
import com.ChatApp.Entities.User;
import com.ChatApp.Models.Requests.UserRequests;
import com.ChatApp.Models.Responses.UserResponses;
import com.ChatApp.Security.CurrentUser;
import com.ChatApp.Security.IsAuthenticatedUser;

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
                        user.setGender(User.Gender.valueOf(req.getGender().toLowerCase()));

                    }
                    catch(IllegalArgumentException e){
                        return ResponseEntity.badRequest().body("invalid gendser value. Allowed:male,femal,other");
                    }
                }
                if(req.getDateOfBirth()!=null && !req.getDateOfBirth().isEmpty()){
                    try{
                        LocalDate dob = LocalDate.parse(req.getDateOfBirth());
                        user.setDateOfBirth(java.sql.Date.valueOf(dob));
                    }
                    catch(Exception e){
                        return ResponseEntity.badRequest().body("invalid date formate . use ISO 8601 (e.g. 2000-05-25)");
                    }
                }

                User createdUser=authBal.signup(user);
                return ResponseEntity.ok(new UserResponses.UserResponse(createdUser));

        }catch(Exception e){
            return ResponseEntity.badRequest().body("Error: "+e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequests.LoginRequest req,HttpServletResponse response){
        try{
            System.out.println("request email or username: "+req.getEmailOrUsername());
            String token=authBal.login(req.getEmailOrUsername(), req.getPassword(), response);
            return ResponseEntity.ok(Map.of(
                "message","login succesful",
                "token", token
            ));
        }
        catch(ResourceAccessException e){
            return ResponseEntity.status(401).body(e.getMessage());
            
        }catch(Exception e){
            return ResponseEntity.internalServerError().body("Error:"+e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response){

        authBal.logout(response);
        return ResponseEntity.ok("logged out successfully.");
    }    

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotpassword(@RequestBody UserRequests.ForgotPasswordRequest request){
        String otp = authBal.forgotPassword(request.getEmail());
        System.out.println("this is otp: "+otp);
        return ResponseEntity.ok("OTP sent to your email");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody UserRequests.ResetPasswordRequest request){
        authBal.resetPassword(request.getEmail(),request.getOtp(),request.getNewPassword());
        return ResponseEntity.ok("password reset successfully.");
        
    }

    @GetMapping("/me")
    @IsAuthenticatedUser   
    public ResponseEntity<?> getAuthenticatedUser(@CurrentUser User user){
        return ResponseEntity.ok(new UserResponses.UserResponse(user));
    }
}