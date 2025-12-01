package com.ChatApp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.ChatApp.Security.JwtAuthenticationFilter;



@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Value("${app.cors.allowed-original}")
    private String[] allowedOrigins;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationfilter){
        this.jwtAuthenticationFilter=jwtAuthenticationfilter;
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
        .csrf(csrf->csrf.disable())
        .cors(org.springframework.security.config.Customizer.withDefaults())
        .addFilterBefore(jwtAuthenticationFilter,UsernamePasswordAuthenticationFilter.class)
        .authorizeHttpRequests(auth->auth.requestMatchers("/**").permitAll());
        
        return http.build();
    }

    @Bean 
    public WebMvcConfigurer corsConfigurer(){
        return new WebMvcConfigurer(){
            @Override
            public void addCorsMappings(CorsRegistry registry){
                registry.addMapping("/**").allowedOrigins(allowedOrigins).allowedMethods("GET","POST","PUT","DELETE","OPTIONS","PATCH").allowedHeaders("*").allowCredentials(true);
            }
        };
    }
}
