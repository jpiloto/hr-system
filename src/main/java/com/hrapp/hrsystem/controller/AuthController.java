package com.hrapp.hrsystem.controller;

import com.hrapp.hrsystem.dto.AuthRequest;
import com.hrapp.hrsystem.service.JwtService;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public AuthController(AuthenticationManager authManager, JwtService jwtService, UserDetailsService userDetailsService) {
        this.authManager = authManager;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/login")
    public String login(@RequestBody AuthRequest request) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
        authManager.authenticate(authentication);

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        return jwtService.generateToken(userDetails);
    }
}