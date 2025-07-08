package com.restaurant.orderingsystem.controller;

import com.restaurant.orderingsystem.dto.JwtResponse;
import com.restaurant.orderingsystem.dto.LoginRequest;
import com.restaurant.orderingsystem.entity.User;
import com.restaurant.orderingsystem.repository.UserRepository;
import com.restaurant.orderingsystem.security.JwtUtils;
import com.restaurant.orderingsystem.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .map(item -> item.getAuthority().replace("ROLE_", ""))
                .orElse("");

        return ResponseEntity.ok(new JwtResponse(
                jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getFullName(),
                userDetails.getEmail(),
                role));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        // JWT is stateless, so we only need to clear the client side token
        // But we can implement server-side blacklisting of tokens if needed
        return ResponseEntity.ok().body("User logged out successfully!");
    }
} 