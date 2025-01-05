package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, 
                      PasswordEncoder passwordEncoder,
                      AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public User registerUser(String name, String email, String password) {
    System.out.println("DEBUG - RegisterUser method called");
    System.out.println("DEBUG - Name: " + name);
    System.out.println("DEBUG - Email: " + email);
    System.out.println("DEBUG - Password length: " + (password != null ? password.length() : "null"));

        // Validate inputs
        if (email == null || email.trim().isEmpty()) {
            logger.error("Email cannot be empty");
            throw new IllegalArgumentException("Email cannot be empty");
        }
        
        try {
            // Create new user
            User user = new User();
            user.setName(name.trim());
            user.setEmail(email.trim());
            user.setRoles(Set.of("USER"));
            logger.debug("Created user object for email: {}", email);
            
            // Encode password
            String encodedPassword = passwordEncoder.encode(password);
            user.setPassword(encodedPassword);
            
            logger.debug("Password encoded successfully");
            
            // Save user
            User savedUser = userRepository.save(user);
            logger.info("Successfully registered user with email: {}", email);
            
            return savedUser;
        } catch (Exception e) {
            logger.error("Error during user registration: {}", e.getMessage());
            throw new RuntimeException("Registration failed: " + e.getMessage());
        }
    }


    public Authentication login(String email, String password) {
        try {
            System.out.println("DEBUG - Login attempt for email: " + email);
            
            // Find user
            User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            System.out.println("DEBUG - User found: " + user.getName());
            System.out.println("DEBUG - Stored password hash: " + user.getPassword());
    
            // Verify password
            System.out.println("DEBUG - Verifying password...");
            if (password == null || password.isEmpty()) {
                System.out.println("DEBUG - Password is empty");
                throw new BadCredentialsException("Password cannot be empty");
            }
            
            if (!passwordEncoder.matches(password, user.getPassword())) {
                System.out.println("DEBUG - Password verification failed");
                throw new BadCredentialsException("Invalid password");
            }
            System.out.println("DEBUG - Password verified successfully");
    
            // Create authentication with encoded password
            System.out.println("DEBUG - Creating authentication token");
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.getEmail(),
                user.getPassword(), // Use encoded password
                user.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .collect(Collectors.toList())
            );
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            System.out.println("DEBUG - Authentication stored in context");
            
            return authentication;
        } catch (Exception e) {
            System.out.println("DEBUG - Authentication failed with error: " + e.getMessage());
            throw new BadCredentialsException("Authentication failed");
        }
    }
    
    public User getCurrentUser() {
        System.out.println("DEBUG - Getting current user");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null) {
            System.out.println("DEBUG - No authentication found");
            return null;
        }
        
        if (!authentication.isAuthenticated()) {
            System.out.println("DEBUG - Authentication is not authenticated");
            return null;
        }
        
        if (authentication instanceof AnonymousAuthenticationToken) {
            System.out.println("DEBUG - Anonymous authentication token");
            return null;
        }
        
        System.out.println("DEBUG - Fetching user with email: " + authentication.getName());
        return userRepository.findByEmail(authentication.getName())
            .orElseThrow(() -> {
                System.out.println("DEBUG - User not found in database");
                return new UsernameNotFoundException("User not found");
            });
    }

    public void logout(HttpSession session) {
        SecurityContextHolder.clearContext();
        if (session != null) {
            session.invalidate();
        }
    }

    public boolean isEmailTaken(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public boolean verifyPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(passwordEncoder.encode(rawPassword), encodedPassword);
    }
}