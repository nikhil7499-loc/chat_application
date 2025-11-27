package com.ChatApp.Security;

import com.ChatApp.BusinessAccess.AuthBal;
import com.ChatApp.Entities.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthBal authBal;

    public JwtAuthenticationFilter(AuthBal authBal) {
        this.authBal = authBal;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        User user = authBal.getAuthenticatedUser(request);
        if (user != null) {
            request.setAttribute("currentUser", user);
        }

        filterChain.doFilter(request, response);
    }
}
