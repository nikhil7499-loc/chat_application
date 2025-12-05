package com.ChatApp.Security;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import com.ChatApp.BusinessAccess.AuthBal;
import com.ChatApp.Entities.User;
import com.ChatApp.Exceptions.UnauthorizedException;

import jakarta.servlet.http.HttpServletRequest;

@Aspect
@Component
public class AuthAspect {

    private final AuthBal authBal;
    private final HttpServletRequest request;

    public AuthAspect(AuthBal authBal, HttpServletRequest request) {
        this.authBal = authBal;
        this.request = request;
    }

    @Before("@annotation(com.ChatApp.Security.IsAuthenticatedUser)")
    public void checkAuthentication() {
        User user = authBal.getAuthenticatedUser(request);
        if (user == null) {
            throw new UnauthorizedException("Not authenticated");
        }
        request.setAttribute("currentUser", user);
    }
}
